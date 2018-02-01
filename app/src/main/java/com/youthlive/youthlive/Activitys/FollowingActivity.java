package com.youthlive.youthlive.Activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.youthlive.youthlive.Adapter.following_adapter;
import com.youthlive.youthlive.Fragments.FollowingFragment;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.LiveScreen;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.bean;
import com.youthlive.youthlive.followListPOJO.Datum;
import com.youthlive.youthlive.followListPOJO.followListBean;
import com.youthlive.youthlive.followPOJO.Data;
import com.youthlive.youthlive.followPOJO.followBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FollowingActivity extends AppCompatActivity {
    RecyclerView recycler_following;
    following_adapter recAdapter;
    LinearLayoutManager layoutmanager;

    Toolbar toolbar;
    List<Datum> list;
    public ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        list = new ArrayList<>();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = (ProgressBar)findViewById(R.id.progress);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("My Followings");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler_following = (RecyclerView)findViewById(R.id.recycler_following);
        layoutmanager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        recAdapter = new following_adapter(this , list);
        recycler_following.setLayoutManager(layoutmanager);
        recycler_following.setAdapter(recAdapter);
        recycler_following.setHasFixedSize(true);




    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData()
    {
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<followListBean> call = cr.followList(b.userId);

        call.enqueue(new Callback<followListBean>() {
            @Override
            public void onResponse(Call<followListBean> call, Response<followListBean> response) {

                Toast.makeText(FollowingActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                recAdapter.setGridData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<followListBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


}
