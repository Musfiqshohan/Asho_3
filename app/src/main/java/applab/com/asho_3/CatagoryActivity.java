package applab.com.asho_3;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class CatagoryActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView listshowrcy;
    List<Item> productlists = new ArrayList<>();
    MainActivityAdapter adapter;

    EditText initSearch;
    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catagory);


        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        System.out.println(value);






//        //adding product name and image in array list of Item type
//        productlists.add(new Item("Harley Davidson Street 750 2016 Std",
//                R.drawable.harleydavidson));
//        productlists.add(new Item("Triumph Street Scramble 2017 Std",
//                R.drawable.streetscramble1));
//        productlists.add(new Item("Suzuki GSX R1000 2017 STD",
//                R.drawable.suzukigsx1000));
//        productlists.add(new Item("Suzuki GSX R1000 2017 R",
//                R.drawable.suzukigsx1000r));
//        productlists.add(new Item("Suzuki Gixxer 2017 SP",
//                R.drawable.suzukigixxer2017));
//        productlists.add(new Item("Suzuki Gixxer 2017 SF 2017 Fuel injected ABS",
//                R.drawable.suzukigixxer2017sf));
//        productlists.add(new Item("BMW R 1200 R 2017",
//                R.drawable.bmwr1200r));
//        productlists.add(new Item("BMW R 1200 RS 2017",
//                R.drawable.bmwr1200rs));
//        productlists.add(new Item("BMW R 1200 GSA 2017",
//                R.drawable.bmwr120gsa));
//        productlists.add(new Item("Royal Enfield Classic 350 2017 Gunmental Grey",
//                R.drawable.class350gungrey));
//        productlists.add(new Item("Honda MSX125 Grom 2018 STD",
//                R.drawable.hondagrom));
//        productlists.add(new Item("UM Motorcycles Renegade 2017 classic",
//                R.drawable.renegadecommandoclassic));
//        productlists.add(new Item("Ducati Scrambler 2017 Mach 2.0",
//                R.drawable.scramblermach));
//        productlists.add(new Item("yamaha Fazer 2017 25",
//                R.drawable.yamahafazer));



        System.out.println("cat-> finished");

        listshowrcy = (RecyclerView) findViewById(R.id.listshow);
        listshowrcy.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listshowrcy.setLayoutManager(linearLayoutManager);
        adapter = new MainActivityAdapter(productlists, CatagoryActivity.this);
        listshowrcy.setAdapter(adapter);

        loadCatagoryList();



//       listshowrcy.setAlpha(0);
//
//        initSearch= (EditText) findViewById(R.id.search_text2);
//        initSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Your toast message.",
//                        Toast.LENGTH_SHORT).show();
//
//                initSearch.setAlpha(0);
//                ImageView img= findViewById(R.id.imageView2);
//                img.setAlpha(0);
//                listshowrcy.setAlpha(1);
//
//            }
//        });



    }



    public void loadCatagoryList()
    {
        Firebase workCatList=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");


        workCatList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp: dataSnapshot.getChildren()){

                    String catName=dsp.getKey();
                    System.out.println("cat-> "+dsp.getKey());

                    String downloadLink=dsp.child("catagoryLogo").getValue().toString();
                    Uri downloadUri=Uri.parse(downloadLink);

                    //Here storing catagory name and Logo download URI
                    adapter.updateWorkerList(new Item(catName, downloadUri));
                    adapter.notifyDataSetChanged();

                   // productlists.add(new Item(catName,R.drawable.bmwr120gsa));

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }



    public void addCatClicked(View v)
    {
        Intent intent = new Intent(CatagoryActivity.this, AddCatagory.class);

        startActivityForResult(intent, 05);
    }


    @Override    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchfile, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                final  List<Item> filtermodelist=filter(productlists,newText);
                adapter.setfilter(filtermodelist);
                return true;
            }
        });
        return true;
    }
    private List<Item> filter(List<Item> pl,String query)
    {
        query=query.toLowerCase();
        final List<Item> filteredModeList=new ArrayList<>();
        for (Item model:pl)
        {
            final String text=model.getName().toLowerCase();
            if (text.startsWith(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }
    //for changing the text color of searchview
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
}