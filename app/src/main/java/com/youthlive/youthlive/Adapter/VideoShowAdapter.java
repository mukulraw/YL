package com.youthlive.youthlive.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.VideoView;

import com.youthlive.youthlive.R;

import java.util.ArrayList;

/**
 * Created by Designer 3 on 26-10-2017.
 */

public class VideoShowAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> Video;
    public VideoShowAdapter(
            Context context2,
            ArrayList<String> video
    ) {
        this.context=context2;
        this.Video = video;
    }
    public int getCount() {
        return Video.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View child, ViewGroup parent) {
        VideoShowAdapter.Holder holder;
        LayoutInflater layoutInflater;
        View v;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.item_recyclerview_video, null);
        holder = new VideoShowAdapter.Holder();
        holder.video1 = (VideoView) v.findViewById(R.id.ivGridVideoItem);
        v.setTag(holder);
        holder = (VideoShowAdapter.Holder) v.getTag();
        System.out.println("videos: "+Video);
        holder.video1.setVideoURI(Uri.parse(Video.get(position)));
        return v;
    }
    public class Holder {
        VideoView video1;
    }
}
