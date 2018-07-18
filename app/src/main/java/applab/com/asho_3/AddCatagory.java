package applab.com.asho_3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCatagory extends AppCompatActivity {



    EditText catText;
    Button saveCat;

    private Firebase mRootRef;

    private Button mSelectImage;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT=2;
    String catagoryValue;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_catagory);

         mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        mStorage= FirebaseStorage.getInstance().getReference();
        mSelectImage = (Button)findViewById(R.id.addLogo);
        catText= (EditText) findViewById(R.id.cat_text);
        saveCat= (Button)findViewById(R.id.save_cat);


        mSelectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });


        saveCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your toast message.",
                        Toast.LENGTH_SHORT).show();

                catagoryValue= catText.getText().toString();

                Intent intent = new Intent(AddCatagory.this, EmployeeListActivity.class);
                intent.putExtra("newCatagory", catagoryValue);
                startActivityForResult(intent, 500);


                finish();

            }
        });

    }



    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                       // Log.e(TAG, "signInAnonymously:FAILURE", exception);
                        System.out.println("signInAnonymously:FAILURE");
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT &&  resultCode==RESULT_OK)
        {

            System.out.println("chosen");
            Uri uri=data.getData();
            //StorageReference Logofilepath=mStorage.child("Work_Catagories").child(catagoryValue).child("Logo");
            StorageReference Logofilepath=mStorage.child("Photos");//child(uri.getLastPathSegment());
            Logofilepath.child("photo1").putFile(uri);
//            Logofilepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(AddCatagory.this, "upload done", Toast.LENGTH_SHORT).show();
//                }
//            });


        }
    }
}