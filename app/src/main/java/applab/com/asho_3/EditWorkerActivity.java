package applab.com.asho_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by musfiq on 7/10/18.
 */

public class EditWorkerActivity extends AppCompatActivity {

    String name,email,address,phone,NID, usefulLinks, DOB;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editworker);

    }


    public void submitClicked(View v)
    {

        name=((EditText)findViewById(R.id.w_name)).getText().toString();
        email=((EditText)findViewById(R.id.w_email)).getText().toString();
        address=((EditText)findViewById(R.id.w_address)).getText().toString();
        phone=((EditText)findViewById(R.id.w_phone)).getText().toString();
        NID=((EditText)findViewById(R.id.w_NID)).getText().toString();
        usefulLinks=((EditText)findViewById(R.id.w_link)).getText().toString();
        DOB=((EditText)findViewById(R.id.w_DOB)).getText().toString();

        EmployeeProfile employeeProfile= new EmployeeProfile(name,email,address,phone,NID,usefulLinks,DOB);
        Firebase FDataBaseRef=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
        Firebase workCat= FDataBaseRef.child("Programmer");
        Firebase employeeList= workCat.child("EmployeeList");

        Firebase details= workCat.child("Details");
        Firebase employees= employeeList.child(NID);
        employees.child("name").setValue(name);
        employees.child("email").setValue(email);
        employees.child("address").setValue(address);
        employees.child("phone").setValue(phone);
        employees.child("NID").setValue(NID);
        employees.child("usefulLinks").setValue(usefulLinks);
        employees.child("DOB").setValue(DOB);



        finish();

    }


}
