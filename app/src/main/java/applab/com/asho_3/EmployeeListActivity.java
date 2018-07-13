package applab.com.asho_3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EmployeeListActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerAdapter adapter;
    RecyclerView.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);
        // setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        System.out.println(value);


        getSupportActionBar().setTitle("Barber");

        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this);


        recyclerView.setAdapter(adapter);


    }

    public  void addEmpClicked(View v)  // I will be able to create a new task
    {
        Intent intent = new Intent(EmployeeListActivity.this, EditWorkerActivity.class);
        startActivityForResult(intent, 500);

    }


//    public void loadWorkerList()
//    {
//        Firebase FDataBaseRef=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
//        Firebase workCat= FDataBaseRef.child("Programmer");
//        final Firebase employeeList= workCat.child("EmployeeList");
//
//
//        employeeList.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot dsp: dataSnapshot.getChildren()){
//
//                    EmployeeProfile employeeProfile= dsp.getValue(EmployeeProfile.class);
//                    adapter.updateWorkerList(employeeProfile);
//                    adapter.notifyDataSetChanged();
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//
//    }





}
