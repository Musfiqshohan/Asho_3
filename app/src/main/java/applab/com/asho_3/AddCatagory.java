package applab.com.asho_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCatagory extends AppCompatActivity {



    EditText catText;
    Button saveCat;

    private Firebase mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_catagory);


        catText= (EditText) findViewById(R.id.cat_text);
        saveCat= (Button)findViewById(R.id.save_cat);

       // Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://newsfeed-5e0ae.firebaseio.com/");

        saveCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your toast message.",
                        Toast.LENGTH_SHORT).show();

                String catValue= catText.getText().toString();


                Firebase catRef= mRootRef.child("Work_Catagories");
                Firebase workRef= catRef.child(catValue);
                Firebase employeeRef= workRef.child("Employee");

              //  EmployeeProfile employeeProfile= new EmployeeProfile("Demo","D-5");
                //employeeRef.setValue(employeeProfile);

                finish();

            }
        });


    }

}