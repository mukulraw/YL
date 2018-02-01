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

public class Last7DaysAdapter extends RecyclerView.Adapter<Last7DaysAdapter.Last7Holder> {


    Context context;
    public Last7DaysAdapter(Context context)
    {
        this.context =context;

    }

    @Override
    public Last7Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tabratting_adapterlayout , parent , false);
        return new Last7Holder(view);

    }

    @Override
    public void onBindViewHolder(Last7Holder last7Holder, int Position) {

    }


    @Override
    public int getItemCount() {
        return 19;
    }

    public class Last7Holder extends RecyclerView.ViewHolder {
        public Last7Holder(View itemView) {
            super(itemView);
        }
    }


}
