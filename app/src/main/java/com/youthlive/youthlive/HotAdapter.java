package com.youthlive.youthlive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youthlive.youthlive.Activitys.Videocomment_Activity;
import com.youthlive.youthlive.vlogListPOJO.Datum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HotAdapter extends RecyclerView.Adapter<HotAdapter.MyViewHolder> {

    Context context;
    List<Datum> list = new ArrayList<>();


    public HotAdapter(Context context , List<Datum> list){
        this.context = context;
        this.list = list;
    }

    public void setGridData(List<Datum> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hot_list_model , parent , false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Datum item = list.get(position);
        holder.title.setText(item.getTimelineName());
        holder.likes.setText(item.getLikesCount());
        Glide.with(context).load(item.getTimelineProfileImage()).into(holder.profile);
        Glide.with(context).load(item.getVideoURL()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,SingleVideoActivity.class);
                intent.putExtra("videoId" , item.getVideoId());
                intent.putExtra("url" , item.getVideoURL());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title , likes;
        ImageView image;
        CircleImageView profile;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            likes = (TextView)itemView.findViewById(R.id.likes);
            image = (ImageView)itemView.findViewById(R.id.image);
            profile = (CircleImageView)itemView.findViewById(R.id.profile);
        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
