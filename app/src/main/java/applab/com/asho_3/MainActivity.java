package applab.com.asho_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    EditText initSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setContentView(R.layout.activity_main);

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

}