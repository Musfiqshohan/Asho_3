package applab.com.asho_3;

import android.content.Intent;
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

    //String name,email,address,phone,NID, usefulLinks, DOB;
    String Phone,Name,Email,Address,Dob,Usefullinks,Nid;
    public String currCatagory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editworker);

        Intent intent = getIntent();
        currCatagory =intent.getStringExtra("currentCatagory");


    }


    public void submitClicked(View v)
    {

        Name=((EditText)findViewById(R.id.w_name)).getText().toString();
        Email=((EditText)findViewById(R.id.w_email)).getText().toString();
        Address=((EditText)findViewById(R.id.w_address)).getText().toString();
        Phone=((EditText)findViewById(R.id.w_phone)).getText().toString();
        Nid=((EditText)findViewById(R.id.w_NID)).getText().toString();
        Usefullinks=((EditText)findViewById(R.id.w_link)).getText().toString();
        Dob=((EditText)findViewById(R.id.w_DOB)).getText().toString();

        EmployeeProfile employeeProfile= new EmployeeProfile(Name,Email,Address,Phone,Nid,Usefullinks,Dob);
        Firebase FDataBaseRef=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
        Firebase workCat= FDataBaseRef.child(currCatagory);
        Firebase employeeList= workCat.child("EmployeeList");

        Firebase details= workCat.child("Details");
        Firebase employees= employeeList.child(Nid);
        employees.child("Name").setValue(Name);
        employees.child("Email").setValue(Email);
        employees.child("Address").setValue(Address);
        employees.child("Phone").setValue(Phone);
        employees.child("Nid").setValue(Nid);
        employees.child("Usefullinks").setValue(Usefullinks);
        employees.child("Dob").setValue(Dob);



        finish();

    }


}
