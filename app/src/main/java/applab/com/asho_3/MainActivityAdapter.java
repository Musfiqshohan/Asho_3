package applab.com.asho_3;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/** * Created by amit rawat on 11/27/2017. */
public class MainActivityAdapter  extends
        RecyclerView.Adapter<MainActivityAdapter.Holderview>{
    private List<Item> productlist;
    private Uri downloadUri;
    private Context context;
    public MainActivityAdapter(List<Item> productlist, Context context) {
        this.productlist = productlist;
        this.context = context;
    }
    @Override    public Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customitem,parent,false);
        return new Holderview(layout);
    }
    @Override    public void onBindViewHolder(Holderview holder, final int position) {
        holder.v_name.setText(productlist.get(position).getName());
        //holder.v_image.setImageResource(productlist.get(position).getPhoto());
        Picasso.with(context).load(downloadUri).fit().centerCrop().into(holder.v_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                Toast.makeText(context, "click on " + productlist.get(position).getName(),
                        Toast.LENGTH_LONG).show();



                Intent myIntent = new Intent(context, EmployeeListActivity.class);
                myIntent.putExtra("newCatagory", productlist.get(position).getName()); //Optional parameters
                context.startActivity(myIntent);



            }
        });
    }
    @Override    public int getItemCount() {
        return productlist.size();
    }
    public void setfilter(List<Item> listitem)
    {
        productlist=new ArrayList<>();
        productlist.addAll(listitem);
        notifyDataSetChanged();
    }
    class Holderview extends RecyclerView.ViewHolder
    {
        ImageView v_image;
        TextView v_name;
        Holderview(View itemview)
        {
            super(itemview);
            v_image=(ImageView) itemview.findViewById(R.id.catagory_logo);
            v_name = (TextView) itemView.findViewById(R.id.catagory_title);
        }
    }


    public void updateWorkerList(Item newItem, Uri downloadUri)
    {
        System.out.println("uri-> "+downloadUri.toString());
        this.downloadUri=downloadUri;
        productlist.add(newItem);

    }




}