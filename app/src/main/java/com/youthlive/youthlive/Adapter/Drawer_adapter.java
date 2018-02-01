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

public class Drawer_adapter  extends RecyclerView.Adapter<Drawer_adapter.Draweradp>{

    Context context;
    public Drawer_adapter(Context context)
    {
        this.context =context;

    }


    @Override
    public Draweradp onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_adapterxml , parent , false);
        return new Draweradp(view);
    }

    @Override
    public void onBindViewHolder(Draweradp draweradp, int position) {
    }
    @Override
    public int getItemCount() {
        return 20;
    }
    public class  Draweradp extends RecyclerView.ViewHolder {
        public Draweradp(View itemView) {
            super(itemView);
        }
    }
}
