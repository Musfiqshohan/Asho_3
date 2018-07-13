package applab.com.asho_3;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    Context context;

    public RecyclerAdapter(Context mainActivity) {
        context = mainActivity;

        titles.add("hello test");
        details.add("hello test details");
    }

    ArrayList<String> titles,details;

//    private String[] titles = {"Chapter One",
//            "Chapter Two",
//            "Chapter Three",
//            "Chapter Four",
//            "Chapter Five",
//            "Chapter Six",
//            "Chapter Seven",
//            "Chapter Eight"};
//
//    private String[] details = {"Item one details",
//            "Item two details", "Item three details",
//            "Item four details", "Item file details",
//            "Item six details", "Item seven details",
//            "Item eight details"};

    private int[] images = {
            R.drawable.android_image_1,
            R.drawable.android_image_1,
            R.drawable.android_image_1,
            R.drawable.android_image_1,
            R.drawable.android_image_1,
            R.drawable.android_image_1,
            R.drawable.android_image_1,
            R.drawable.android_image_1 };


    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNumber;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public ImageView image_call;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =(TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    Intent myIntent = new Intent(context, ProfileActivity.class);
                    myIntent.putExtra("key", "hello"); //Optional parameters
                    context.startActivity(myIntent);

                }
            });

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles.get(i));
        viewHolder.itemDetail.setText(details.get(i));
        viewHolder.itemImage.setImageResource(images[i]);
    }



    @Override
    public int getItemCount() {
        return titles.size();
    }


    public void updateWorkerList(EmployeeProfile employeeProfile)
    {
        titles.add(employeeProfile.name);
        details.add(employeeProfile.email);

    }

}