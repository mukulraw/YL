package com.youthlive.youthlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.engineLiveUsersPOJO.IncomingStream;
import com.youthlive.youthlive.engineLiveUsersPOJO.engineLiveUsersBean;
import com.youthlive.youthlive.getLivePOJO.Result;
import com.youthlive.youthlive.getLivePOJO.getLiveBean;
import com.youthlive.youthlive.wowzaAPIPOJO.wowzaAPIBean;
import com.youthlive.youthlive.wowzaLiveStreamsPOJO.LiveStream;
import com.youthlive.youthlive.wowzaLiveStreamsPOJO.getWowzaStreamBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Live extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    ProgressBar progress;
    List<wowzaAPIBean> list;
    List<liveBean> list2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_layout, container, false);

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        grid = (RecyclerView) view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext(), 2);

        progress = (ProgressBar) view.findViewById(R.id.progress);


        adapter = new LiveAdapter(getContext(), list);
        adapter2 = new LiveAdapter2(getContext(), list2);

        grid.setAdapter(adapter2);
        grid.setLayoutManager(manager);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getContext().getApplicationContext();


/*
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
*/


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                //.client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<List<liveBean>> call = cr.getLives(b.userId);

        call.enqueue(new Callback<List<liveBean>>() {
            @Override
            public void onResponse(Call<List<liveBean>> call, Response<List<liveBean>> response) {

                        adapter2.setGridData(response.body());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<liveBean>> call, Throwable throwable) {
                progress.setVisibility(View.GONE);
                throwable.printStackTrace();
            }
        });


        /*progress.setVisibility(View.VISIBLE);



        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.3:8087/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<String> call = cr.getEngineLiveList();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                Log.d("asdasd" , "data");


                list2.clear();

                try {

                    //for (int i = 0; i < response.body().getIncomingStreams().size(); i++) {
                        //if (!Objects.equals(response.body().get(i).getUserStatus(), "stopped")) {
                    //    list2.add(response.body().getIncomingStreams().get(i));
                        //}
                    //}

                    Log.d("response" , response.body());

                    try {


                        adapter2.setGridData(list2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }catch (Exception e)
                {

                    Toast.makeText(getContext() , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }



                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);

                Log.d("asdasd" , t.toString());

            }
        });
*/



    }


    public class LiveAdapter2 extends RecyclerView.Adapter<LiveAdapter2.ViewHolder> {

        Context context;
        List<liveBean> list = new ArrayList<>();

        public LiveAdapter2(Context context, List<liveBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<liveBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.live_list_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final liveBean item = list.get(position);

            //holder.title.setText(item.getTitle());

            //ImageLoader loader = ImageLoader.getInstance();

            //loader.displayImage(item.getUserImage() , holder.image);

            holder.viewCount.setText(item.getLiveUsers());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String name = item.getName();
                    String[] dd = name.split("-");


                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("uri", name);
                    intent.putExtra("liveId", name);
                    intent.putExtra("timelineId", String.valueOf(item.getUserId()));
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title , viewCount;

            public ViewHolder(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
                viewCount = (TextView) itemView.findViewById(R.id.view_count);

            }
        }
    }


    public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

        Context context;
        List<wowzaAPIBean> list = new ArrayList<>();

        public LiveAdapter(Context context, List<wowzaAPIBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<wowzaAPIBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.live_list_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final wowzaAPIBean item = list.get(position);

            holder.title.setText(item.getName());

            ImageLoader loader = ImageLoader.getInstance();

            //loader.displayImage(item.getUserImage() , holder.image);

            //holder.viewCount.setText(item.getLiveUsers());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("uri", item.getPlayerHlsPlaybackUrl());
                    intent.putExtra("liveId", item.getLiveId());
                    intent.putExtra("timelineId", item.getUserId());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title , viewCount;

            public ViewHolder(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
                viewCount = (TextView) itemView.findViewById(R.id.view_count);

            }
        }
    }

}
