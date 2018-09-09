package applab.com.asho_3;

/**
 * Created by musfiq on 8/10/18.
 */
import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.location.Location;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BGMapService extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;  //changed here shouuld be 10f

    private String currCatagory,Nid;



    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Posts").push();
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            ref.push().child("name").setValue(provider);

            getCurrentLocation();
        }


        public void getCurrentLocation(){
            Log.d(TAG, "getCurrentLocation: "+mLastLocation.getLatitude()+mLastLocation.getLongitude());
        }

        @Override
        public void onLocationChanged(Location location)
        {


            Log.e(TAG, "onLocationChanged: " + location);
            Toast.makeText(BGMapService.this, "loaction: "+location.getLongitude()+"+"+location.getLatitude(), Toast.LENGTH_LONG).show();
            mLastLocation.set(location);

            //DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Posts").push();
            //ref.push().child("name").setValue(location.getLatitude()+"+"+location.getLongitude());

            String geoLocation=location.getLatitude()+"+"+location.getLongitude();

            Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
            Firebase workCat = FDataBaseRef.child(currCatagory);
            Firebase employeeList = workCat.child("EmployeeList");
            Firebase employees = employeeList.child(Nid);

            employees.child("currGeoLocation").setValue(geoLocation);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Toast.makeText(BGMapService.this, "providerdisabled", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Toast.makeText(BGMapService.this, "providerenabled", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Toast.makeText(BGMapService.this, "status changed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {


        ///Getting nid & current catagory from edit worker activity
        currCatagory=intent.getStringExtra("currCatagory");
        Nid=intent.getStringExtra("Nid");


        Log.d(TAG, "onStartCommand: in my service log");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreatexx");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy: service destroying");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}