package applab.com.asho_3;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit rawat on 11/27/2017.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.Holderview> {
    private List<Item> productlist;
  //  private Uri downloadUri;
    private Context context;


    public MainActivityAdapter(List<Item> productlist, Context context) {
        this.productlist = productlist;
        this.context = context;
    }

    @Override
    public Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customitem, parent, false);
        return new Holderview(layout);
    }

    @Override
    public void onBindViewHolder(final Holderview holder, final int position) {
        holder.v_name.setText(productlist.get(position).getName());

        //holder.v_image.setImageResource(productlist.get(position).getPhoto());
        System.out.println("pos: " + position + "  ->" + productlist.get(position).getdownloadUri().toString());
        Picasso.with(context)
                .load(productlist.get(position).getdownloadUri())
                .fit()
                .centerCrop()
                .into(holder.v_image);

        ///This is for detecting click on catagory option menu
        holder.catagory_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.catagory_options);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_option_menu);
                //adding click listener


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            ///when clicked on options
                            case R.id.Edit:

//                                Intent intent = new Intent(context, EditFieldClass.class);
//                                ((MainActivity)mCtx).startActivityForResult(intent, Intent_Constants.INTENT_REQUEST_CODE);
//                                DeleteFromList(i,2);

                                ///on construction  need edit here
                                Intent intent = new Intent(context, AddCatagory.class);
                                context.startActivity(intent);
                                break;

                            case R.id.Delete:
                                DeleteFromList(position);
                                Toast.makeText(context, "Catagory Deleted", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        return false;
                    }
                });

                //displaying the popup
                popup.show();

            }
        });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click on " + productlist.get(position).getName(),
                        Toast.LENGTH_LONG).show();


                Intent myIntent = new Intent(context, EmployeeListActivity.class);
                myIntent.putExtra("newCatagory", productlist.get(position).getName()); //Optional parameters
                context.startActivity(myIntent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public void setfilter(List<Item> listitem) {
        productlist = new ArrayList<>();
        productlist.addAll(listitem);
        notifyDataSetChanged();
    }

    class Holderview extends RecyclerView.ViewHolder {
        public ImageView v_image;
        public TextView v_name;
        public TextView catagory_options;

        Holderview(View itemview) {
            super(itemview);
            v_image = (ImageView) itemview.findViewById(R.id.catagory_logo);
            v_name = (TextView) itemView.findViewById(R.id.catagory_title);
            catagory_options = (TextView) itemView.findViewById(R.id.edit_options);

        }
    }



    public  void DeleteFromList(int pos)
    {
        Log.d("RecyclerAdapter","in Rec want to delete "+pos);

        deleteRecord(pos);
        productlist.remove(pos);
        this.notifyDataSetChanged();
    }


    //removing deleted catagory
    void deleteRecord(int pos)
    {
        String catagoryName=productlist.get(pos).getName();
        ///this part for deleting whole information from firedatabase
        Firebase catagoryList=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories/");
      //  System.out.println("catagory-> "+Catagory+" deleted Nid-> "+NID.get(pos)+" ref"+employeeList.child(NID.get(pos)));
        catagoryList.child(catagoryName).removeValue();


        ///This part for deleting image of unique nid form firebase storage
        StorageReference storageReference;
        FirebaseStorage storage;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference photoRef = storageReference.child("catagoryLogo/"+catagoryName);


        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("ok", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("notok", "onFailure: did not delete file");
            }
        });

    }


    public void updateWorkerList(Item newItem) {
       // System.out.println("uri-> " + downloadUri.toString());
        productlist.add(newItem);

    }


}