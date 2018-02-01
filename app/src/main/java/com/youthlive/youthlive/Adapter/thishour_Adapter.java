package com.youthlive.youthlive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.R;

/**
 * Created by Designer 3 on 14-10-2017.
 */

public class thishour_Adapter extends  RecyclerView.Adapter<thishour_Adapter.ThishourAdapter>{
    Context context;
    public thishour_Adapter(Context context)
    {
        this.context =context;

    }



    @Override
    public ThishourAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tabratting_adapterlayout , parent , false);
        return new ThishourAdapter(view);

    }

    @Override
    public void onBindViewHolder(ThishourAdapter thishourAdapter, int position) {

    }

    @Override
    public int getItemCount() {
        return 19;
    }

    public class ThishourAdapter extends RecyclerView.ViewHolder {
        public ThishourAdapter(View itemView) {
            super(itemView);
        }
    }
}
