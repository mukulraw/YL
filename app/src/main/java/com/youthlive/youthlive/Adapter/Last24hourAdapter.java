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
public class Last24hourAdapter extends RecyclerView.Adapter<Last24hourAdapter.Last24Holder>{


    Context context;
    public Last24hourAdapter(Context context)
    {
        this.context =context;

    }

    @Override
    public Last24Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tabratting_adapterlayout , parent , false);
        return new Last24Holder(view);
    }

    @Override
    public void onBindViewHolder(Last24Holder last24Holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 19;
    }

    public class Last24Holder extends RecyclerView.ViewHolder {
        public Last24Holder(View itemView) {
            super(itemView);
        }
    }
}
