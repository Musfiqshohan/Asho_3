package applab.com.asho_3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import applab.com.asho_3.modelsGooglePlace.PlaceInfo;


/**
 * Created by musfiq on 7/24/18.
 */

public class NearWorkerMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleApiClient.OnConnectionFailedListener{

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready here");

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            ///for distance
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMarkerDragListener(this);

            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);

            init();
        }

    }

    private static final String TAG = "MapActivity";

    public static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE= 1234;
    public static final float DEFAULT_ZOOM=15;
    private static final int RESULT_OK = 3;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    ///vars
    private boolean mLocationPermissionGranted= false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    private GoogleApiClient mGoogleApiClient;
    ///xml widgets
    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;

    private ImageView mGps;
    private Button location_save_button;
    private Button mRequest;
    //this is for marking the place
    private MarkerOptions markerOptions;
    private Marker pickupMarker;


    ///these are for distance calculate
    private double endLatitude,endLongitude, stLatitude,stLongitude;

    //my current Location
    private Location mLastLocation;
    private LatLng pickupLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_worker_map);

        Intent intent = getIntent();
        final String currCatagory = intent.getStringExtra("currentCatagory");
        final String Nid = intent.getStringExtra("NID");
        mSearchText= (AutoCompleteTextView)findViewById(R.id.input_search);
        mGps= (ImageView)findViewById(R.id.ic_gps);


        location_save_button= (Button) findViewById(R.id.location_save_button);
        mRequest= (Button) findViewById(R.id.request);
        getLocationPermission();


        location_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closingMapActivity();
            }
        });



        //testing


        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d(TAG, "onClick: userId "+userId);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
                GeoFire geoFire = new GeoFire(ref);


                geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                        new GeoFire.CompletionListener() {

                    public void onComplete(String key, DatabaseError error) {

                    }
                });


                pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
               // createFakeWorkers(pickupLocation);
                //  pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here").icon(BitmapDescriptorFactory.fromResource(android.R.mipmap)));

                mRequest.setText("Getting your Driver....");

                 getClosestDriver();

            }
        });


    }

    public LatLng getRandomLocation(LatLng point, int radius) {

        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for(int i = 0; i<10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }

    private void createFakeWorkers(LatLng lastLocation){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("workersAvailable");
        GeoFire geoFire = new GeoFire(ref);

        for(int fId=0;fId<10;fId++) {

            String userIdx= String.valueOf(fId);
            LatLng curDriver= getRandomLocation(lastLocation,5);
            geoFire.setLocation(userIdx, new GeoLocation(curDriver.latitude,curDriver.longitude),
                    new GeoFire.CompletionListener() {

                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
        }


    }


    GeoQuery geoQuery;
    private int radius=1;
    private int driverFound=0;
    private int targetDriver=8;
    private ArrayList<String>driverFoundIDs= new ArrayList<String>();

    private void  getClosestDriver(){

        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("workersAvailable");
        GeoFire geoFire = new GeoFire(driverLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);

        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if(driverFound<targetDriver){
                    driverFound++;
                    driverFoundIDs.add(key);
                    Log.d(TAG, "onGeoQueryReady: driverFound at"+radius);

                    LatLng driverLL= new LatLng(location.latitude,location.longitude);
                    mMap.addMarker(new MarkerOptions().position(driverLL).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker)));
                    Log.d(TAG, "onKeyEntered: Added another marker at "+driverLL.latitude+" "+driverLL.longitude);

                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if(driverFoundIDs.size()<targetDriver)
                {
                    Log.d(TAG, "onGeoQueryReady: not yet radius: "+radius);
                    radius++;
                    getClosestDriver();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }

        });



    }


    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);


        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionid, KeyEvent keyEvent) {
                if(actionid== EditorInfo.IME_ACTION_SEARCH
                        || actionid==EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction()==keyEvent.ACTION_DOWN
                        || keyEvent.getAction()==keyEvent.KEYCODE_ENTER){

                    geoLocate();
                }
                return false;
            }
        });


        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on gps location");
                getDeviceLocation();
            }
        });

        hideSoftKeyBoard();
    }





    private void geoLocate(){


        Log.d(TAG, "geoLocate: geoLocating");
        String searchString =mSearchText.getText().toString();
        Geocoder geocoder= new Geocoder(NearWorkerMapActivity.this);
        List<Address> list= new ArrayList<>();
        try {
            list= geocoder.getFromLocationName(searchString,1);

        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOException: "+e.getMessage());
        }

        if(list.size()>0){
            Address address =list.get(0);

            Log.d(TAG, "geoLocate: found a location : "+address.toString());
            // Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));


        }


    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device's current location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try {
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: founnd location");
                            Location currentLocation= (Location)task.getResult();
                            mLastLocation = currentLocation;
                            Log.d(TAG, "onComplete: mLastLocation");

                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                                    .radius(1000)
                                    .strokeColor(Color.argb(66, 0, 0, 0))
                                    .fillColor(Color.argb(66, 255, 0, 255)));


                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My location");
                        }else{
                            Log.d(TAG, "onComplete: current location null");
                            Toast.makeText(NearWorkerMapActivity.this, "unable get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: SecurityException "+e.getMessage());
        }

    }


    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + " long:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //if (!title.equals("My location")) {
        markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        markerOptions.draggable(true);
        mMap.addMarker(markerOptions);

        // }
        hideSoftKeyBoard();
    }


    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(NearWorkerMapActivity.this);
        Log.d(TAG, "initMap: done initializing map");


        //adding new things
//        LatLng latLngCenter = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//        Log.d(TAG, "initMap: latlng->"+latLngCenter.latitude+" "+latLngCenter.longitude);




//        this.searchCircle = this.map.addCircle(new CircleOptions().center(latLngCenter).radius(1000));
//        this.searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
//        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
//        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
//        this.map.setOnCameraChangeListener(this);

    }

    private void getLocationPermission()
    {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String [] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
            }


        }else{
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted=false;


        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){

                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: on request permisson failed");
                            return;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: location permisson granted");
                    mLocationPermissionGranted=true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


    private void hideSoftKeyBoard(){  ///not working
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }


    /*  google place api autocomplete suggestion


     */


    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyBoard();

            final AutocompletePrediction item= mPlaceAutocompleteAdapter.getItem(i);
            final String placeId=item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient,placeId);


            //here we will put the request
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);




        }
    };


    /// here we will get the object we are looking for
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback= new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place =places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: places: "+mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException"+e.getMessage() );
            }

            // moveCamera(mPlace.getLatLng(), DEFAULT_ZOOM, mPlace.getName());
            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());

            places.release();

        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {  //when anychange will occur

        if(getApplicationContext()!=null){
            mLastLocation=location;

            LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        stLatitude= marker.getPosition().latitude;
        stLongitude=marker.getPosition().longitude;
        marker.setDraggable(true);
        Log.d(TAG, "onMarkerDragEnd: myLatitude: "+stLatitude+"  endLongitude: "+stLongitude);

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }


    @Override
    public void onMarkerDragEnd(Marker marker) {

        endLatitude= marker.getPosition().latitude;
        endLongitude=marker.getPosition().longitude;



        Log.d(TAG, "onMarkerDragEnd: endLatitude: "+endLatitude+ "  endLongitude: "+endLongitude);

    }

    ///no need for this class
    private void saveDataInFirebase(String currCatagory, String Nid){
        Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories/" + currCatagory + "/EmployeeList/" + Nid);
        FDataBaseRef.child("address").setValue(markerOptions.getClass());
        Log.d(TAG, "saveDataInFirebase: data saved");

    }


    private void closingMapActivity(){
        Intent resultIntent=new Intent();

        String latitude=String.valueOf(markerOptions.getPosition().latitude);
        String longitude=String.valueOf(markerOptions.getPosition().longitude);
        resultIntent.putExtra("latitude",latitude);  //
        resultIntent.putExtra("longitude",longitude);  //
        setResult(RESULT_OK, resultIntent);
        Log.d(TAG, "onClick: saved address: "+markerOptions.getPosition());
        finish();
    }


}
