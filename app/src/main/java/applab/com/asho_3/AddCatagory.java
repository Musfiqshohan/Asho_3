package applab.com.asho_3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCatagory extends AppCompatActivity {


    EditText catText;
    Button saveCat;
    ImageView imageView;

    private Firebase mRootRef;

    private Button mSelectImage;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgress;
    //private static final int PICK_IMAGE_REQUEST=71;
    String catagoryValue;

    private Uri filePath;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_catagory);


        // signIn();


        mProgress = new ProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.imgView);
        mSelectImage = (Button) findViewById(R.id.addLogo);
        catText = (EditText) findViewById(R.id.cat_text);
        saveCat = (Button) findViewById(R.id.save_cat);


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        saveCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your toast message.",
                        Toast.LENGTH_SHORT).show();

                catagoryValue = catText.getText().toString();
                uploadImage(catagoryValue);

                Intent intent = new Intent(AddCatagory.this, EmployeeListActivity.class);
                intent.putExtra("newCatagory", catagoryValue);
                startActivityForResult(intent, 500);


                finish();

            }
        });

    }


    private void chooseImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    private void uploadImage(final String catagoryValue) {

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
            StorageReference ref = storageReference.child("catagoryLogo/" + catagoryValue);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // progressDialog.dismiss();
                    Toast.makeText(AddCatagory.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    // mProgress.dismiss();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    ///Here I am getting the url and storing it into firebase idea from moon
                    storeImageUrl(downloadUri, catagoryValue);

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //  progressDialog.dismiss();
                    Toast.makeText(AddCatagory.this, "Failed", Toast.LENGTH_SHORT).show();

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

    void signIn() {
        ///This part does not require now, let see if requires later

//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            // do your stuff
//        } else {
//            signInAnonymously();
//        }

    }

//    private void signInAnonymously() {
//        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                // do your stuff
//            }
//        })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                       // Log.e(TAG, "signInAnonymously:FAILURE", exception);
//                        System.out.println("signInAnonymously:FAILURE");
//                    }
//                });
//    }


    public void storeImageUrl(Uri downloadUri, String currCatagory) {
        Firebase FDataBaseRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
        Firebase workCat = FDataBaseRef.child(currCatagory);
        String urid = downloadUri.toString();
        Firebase catagoryLogoRef = workCat.child("catagoryLogo");
        catagoryLogoRef.setValue(urid);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}