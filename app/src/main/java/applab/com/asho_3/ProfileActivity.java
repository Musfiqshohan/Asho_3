package applab.com.asho_3;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProfileAdapter adapter;

    private static final int REQUEST_CALL = 1;
    private FloatingActionButton fab;
    private String phoneNumber="01521326618";


    TextView nameField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String Catagory = intent.getStringExtra("Catagory");
        String NID = intent.getStringExtra("NID");


        nameField=findViewById(R.id.name);

        fab= findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });




        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfileAdapter(ProfileActivity.this);
        recyclerView.setAdapter(adapter);

        loadWorkerList(Catagory,NID);  /// neeed work


    }


    private void makePhoneCall() {
        String number =phoneNumber; // mEditTextNumber.getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(ProfileActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }





    public void loadWorkerList(String catagoryName,final String NID)
    {
        Firebase FDataBaseRef=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
        Firebase workCat= FDataBaseRef.child(catagoryName);
        Firebase employeeList= workCat.child("EmployeeList");
        Firebase workerInfo= employeeList.child(NID);




        System.out.println();


        workerInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp: dataSnapshot.getChildren()){

                    String key=dsp.getKey();
                    String val=dsp.getValue(String.class);

                    if(key=="Name")
                    {
                        nameField.setText(val);
                    }
                    else if(key=="Phone")
                        phoneNumber=val;

                    System.out.println(key+"->->"+val);
                    adapter.updateWorkerList(key,val);
                    adapter.notifyDataSetChanged();


//                    if(dsp.getKey()==NID) {
//
//                        EmployeeProfileClass employeeProfile=dsp.getValue(EmployeeProfileClass.class);
//
//                        adapter.updateWorkerList(employeeProfile);
//                        adapter.notifyDataSetChanged();
//
//                        System.out.println("adding: "+employeeProfile);
//                    }


                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


}
