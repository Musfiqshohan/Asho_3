package applab.com.asho_3;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;

import applab.com.asho_3.SideClasses.Global;

/**
 * Created by musfiq on 7/10/18.
 */

public class EditWorkerActivity extends AppCompatActivity {

    //String name,email,address,phone,NID, usefulLinks, DOB;
    String Phone, Name, Email, Dob, Usefullinks, Nid,Payment;
    String Address;
    public String currCatagory;

    //For choosing image from gallery
    ImageView proImgView;
    private Button addProPic;
    private static final int GALLERY_INTENT = 2;
    private static final int MAP_INTENT = 3;

    ///these are needed for uploading in firestorage
    private Uri filePath;
    private StorageReference storageReference;
    private FirebaseStorage storage;


    ///For map
    private EditText addressField;
    private ImageView address_map_marker;
    private static final String TAG = "EditWorkerActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    ///For labour unit
    Spinner unitSpinner;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editworker);

        Intent intent = getIntent();
        currCatagory = intent.getStringExtra("currentCatagory");
        Nid = intent.getStringExtra("NID");
        proImgView = (ImageView) findViewById(R.id.proImgView);
        addProPic = (Button) findViewById(R.id.addProPic);
        addressField = (EditText) findViewById(R.id.w_address);
        address_map_marker = (ImageView) findViewById(R.id.address_map_marker);

        //spinner for labour unit
        setSpinner();

        ///Edit mode
        if (Nid != "-1")
            loadAllFields(currCatagory, Nid);


        ///upload Profile image with this button
        addProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        address_map_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Map activity
                if (isServiceOk()) {
                   // init();  going to the map activity
                    Intent intent= new Intent(EditWorkerActivity.this, MapActivity.class);
                    startActivityForResult(intent, MAP_INTENT);
                }

            }
        });


    }

    private void setSpinner(){
        unitSpinner=(Spinner)findViewById(R.id.unit_spinner);       /// for dropdown unit items
        ArrayAdapter<String>unitAdapter= new ArrayAdapter<String>(EditWorkerActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.labour_units));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);
    }


    public void loadAllFields(String currCatagory, String Nid) {

//        Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories/"+currCatagory+"/EmployeeList/"+Nid);
        Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
        Firebase workCat = FDataBaseRef.child(currCatagory);
        Firebase employeeList = workCat.child("EmployeeList");
        Firebase workerInfo = employeeList.child(Nid);


        workerInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String key = dsp.getKey();
                    String val = dsp.getValue(String.class);

                    if (key == "Name") ((EditText) findViewById(R.id.w_name)).setText(val);
                    else if (key == "Email") ((EditText) findViewById(R.id.w_email)).setText(val);
                    else if (key == "Address")
                        ((EditText) findViewById(R.id.w_address)).setText(val);
                    else if (key == "Phone") ((EditText) findViewById(R.id.w_phone)).setText(val);
                    else if (key == "Nid") ((EditText) findViewById(R.id.w_NID)).setText(val);
                    else if (key == "Usefullinks")
                        ((EditText) findViewById(R.id.w_link)).setText(val);
                    else if (key == "Dob") ((EditText) findViewById(R.id.w_DOB)).setText(val);
                    else if (key == "ProfilePicture") loadProfileImage(val);


                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    ///when I need to load image in edit mode
    public void loadProfileImage(String proPicUrl) {
        Uri downloadUri = Uri.parse(proPicUrl);
        //setting the loaded image as profile pic
        Picasso.with(getApplicationContext()).load(downloadUri).fit().centerCrop().into(proImgView);
    }



    ///When i want to choose location from google map
    private void init()
    {
        Button btnMap=(Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(EditWorkerActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }

    ///checking if google map permission is okay?
    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: checking google service version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(EditWorkerActivity.this);
        if(available== ConnectionResult.SUCCESS)
        {
            //everything is fine user can make map request
            Log.d(TAG, "isServiceOk: google play working");
            return true ;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occur but can resolve
            Log.d(TAG, "isServiceOk: an error occured but can solve it");
            Dialog dialog= GoogleApiAvailability.getInstance().getErrorDialog(EditWorkerActivity.this,available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "You cant  make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    ///when all is done and submit button clicked
    public void submitClicked(View v) {


        Name = ((EditText) findViewById(R.id.w_name)).getText().toString();
        Email = ((EditText) findViewById(R.id.w_email)).getText().toString();
        Address = ((EditText) findViewById(R.id.w_address)).getText().toString();
        Phone = ((EditText) findViewById(R.id.w_phone)).getText().toString();
        Nid = ((EditText) findViewById(R.id.w_NID)).getText().toString();
        Usefullinks = ((EditText) findViewById(R.id.w_link)).getText().toString();
        Dob = ((EditText) findViewById(R.id.w_DOB)).getText().toString();
        Payment=((EditText) findViewById(R.id.w_payment)).getText().toString()+"/"+((Spinner) findViewById(R.id.unit_spinner)).getSelectedItem().toString();  //*

        //Storing everything without the download uri string cz later uploading it
        EmployeeProfileClass employeeProfile = new EmployeeProfileClass(Name, Email, Address, Phone, Nid, Usefullinks, Dob, Payment, "uriString coming");  //*
        Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
        Firebase workCat = FDataBaseRef.child(currCatagory);
        Firebase employeeList = workCat.child("EmployeeList");

       // Firebase details = workCat.child("Details");
        Firebase employees = employeeList.child(Nid);
        employees.child("Name").setValue(Name);
        employees.child("Email").setValue(Email);
        employees.child("Address").setValue(Address);        //experimenting a bit here
        employees.child("Phone").setValue(Phone);
        employees.child("Nid").setValue(Nid);
        employees.child("Usefullinks").setValue(Usefullinks);
        employees.child("Dob").setValue(Dob);
        employees.child("Payment").setValue(Payment);  //*


        uploadImage(currCatagory, Nid);

        //Start the location update service
//        if(Global.isServiceRunning==false) {
//            Intent mapserviceIntent = new Intent(getBaseContext(),BGMapService.class);
//            mapserviceIntent.putExtra("currCatagory", currCatagory);
//            mapserviceIntent.putExtra("Nid", Nid);
//            this.startService(mapserviceIntent);
//          //  startService(new Intent(getBaseContext(), BGMapService.class));
//            Global.isServiceRunning = true;
//        }


        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        ///after choosing image form gallery saved in the reference
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                proImgView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else if(resultCode== MAP_INTENT){  //got the latlng value  // got notthing
            String latitude= data.getStringExtra("latitude");
            String longitude= data.getStringExtra("longitude");

            Log.d(TAG, "onActivityResult: lat and long: "+ latitude+"+"+longitude);
            ((EditText) findViewById(R.id.w_address)).setText(latitude+"+"+longitude);
           // Address= latitude+"+"+longitude;

        }
    }




    private void chooseImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    private void uploadImage(final String catagoryValue, final String Nidkey) {

        ///This part for getting reference of firebase and uploading at that reference
//        mProgress.setMessage("Uploading image.....");
//        mProgress.show();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {
//            final ProgressDialog progressDialog= new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();


            ///creating a child under root named catagory logo which has a child named after catagoryName
            StorageReference ref = storageReference.child("ProfilePictures/" + catagoryValue + "/" + Nidkey);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // progressDialog.dismiss();
                    Toast.makeText(EditWorkerActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    // mProgress.dismiss();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    ///Here I am getting the url and storing it into firebase idea from moon
                    storeImageUrl(downloadUri, catagoryValue, Nidkey);

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //  progressDialog.dismiss();
                    Toast.makeText(EditWorkerActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress =(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }


    }

    //storing image in firedatabase the link of each worker where pk is NID
    public void storeImageUrl(Uri downloadUri, String currCatagory, String Nid) {
        Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories/" + currCatagory + "/EmployeeList/" + Nid);
//        Firebase workCat = FDataBaseRef.child(currCatagory);
//        Firebase workerInfo = workCat.child(Nid);
        String urid = downloadUri.toString();
        Firebase catagoryLogoRef = FDataBaseRef.child("ProfilePicture");
        catagoryLogoRef.setValue(urid);

    }


}
