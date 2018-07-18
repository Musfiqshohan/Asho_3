package applab.com.asho_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by musfiq on 7/17/18.
 */

public class ProfileAdapter extends ParentRecyclerAdapter {

    public ProfileAdapter(Context mainActivity) {
        super(mainActivity);
    }





        @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.profilecardlayout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void updateWorkerList(String key, String val)
    {
        titles.add(key+":   ");
        details.add(val);
    }




}
