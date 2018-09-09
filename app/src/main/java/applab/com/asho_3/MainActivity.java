package applab.com.asho_3;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.leakcanary.LeakCanary;

import applab.com.asho_3.Authentication.AuthActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    
    
    
    EditText initSearch;
    Button logout_button;

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


        ///BGS testing

        Log.d(TAG, "onCreate: calling startService");


        //startService(new Intent(getBaseContext(), BGMapService.class));
        //startService(new Intent(getBaseContext(), MyService.class));
        Log.d(TAG, "onCreate: called?");





        setContentView(R.layout.activity_main);   /// this is the main layout
        initSearch = (EditText) findViewById(R.id.search_text);
        logout_button=(Button) findViewById(R.id.logout_button);

        initSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///bgs testing
                //stopService(new Intent(getBaseContext(), BGMapService.class));
               // stopService(new Intent(MainActivity.this, BGMapService.class));
                //stopService(new Intent(MainActivity.this, MyService.class));

                Toast.makeText(getApplicationContext(), "Your toast message.",
                        Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(MainActivity.this, CatagoryActivity.class);
                myIntent.putExtra("key", "hello"); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

//                Intent myIntent = new Intent(MainActivity.this, AuthActivity.class);
//                myIntent.putExtra("key", "hello"); //Optional parameters
//                MainActivity.this.startActivity(myIntent);
//                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


        ///added log out button , here may cause problem
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                prefs.edit().putBoolean("Islogin", false).commit(); // islogin is a boolean value of your login status
                finish();
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