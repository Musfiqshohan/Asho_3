package applab.com.asho_3;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.leakcanary.LeakCanary;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    
    
    
    
    EditText initSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(getApplication());
        // Normal app init code...

        setContentView(R.layout.test);   ///this is not main layout

        //Map activity
//        if(isServiceOk()){
//            init();
//        }






        setContentView(R.layout.activity_main);   /// this is the main layout
        initSearch = (EditText) findViewById(R.id.search_text);


        initSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your toast message.",
                        Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(MainActivity.this, CatagoryActivity.class);
                myIntent.putExtra("key", "hello"); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


        Firebase FDataBaseRef;
        FDataBaseRef= new Firebase("https://newsfeed-5e0ae.firebaseio.com/");
        Firebase PostDivision = FDataBaseRef.child("Work_Catagories");

        PostDivision.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                for(com.firebase.client.DataSnapshot dsp: dataSnapshot.getChildren()){

                    System.out.println( dsp.getKey()+"->  "+dsp.getValue());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void init()
    {
        Button btnMap=(Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }


    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: checking google service version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS)
        {
            //everything is fine user can make map request
            Log.d(TAG, "isServiceOk: google play working");
            return true ;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occur but can resolve
            Log.d(TAG, "isServiceOk: an error occured but can solve it");
            Dialog dialog= GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "You cant  make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}