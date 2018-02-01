package com.youthlive.youthlive.Activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.youthlive.youthlive.Adapter.MassageAdapter;
import com.youthlive.youthlive.HotAdapter;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.MainActivity;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.allMessagePOJO.Datum;
import com.youthlive.youthlive.allMessagePOJO.allMessageBean;
import com.youthlive.youthlive.bean;
import com.youthlive.youthlive.followListPOJO.followListBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MessaageActivity extends AppCompatActivity {

    MassageAdapter holder;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progress;
    List<Datum> list;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaage);


        recyclerView = (RecyclerView) findViewById(R.id.messagerecycler);
        progress = (ProgressBar) findViewById(R.id.progress);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("My Messages");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        list = new ArrayList<>();

        holder = new MassageAdapter(this, list);
        recyclerView.setAdapter(holder);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<allMessageBean> call = cr.allMessageList(b.userId);

        call.enqueue(new Callback<allMessageBean>() {
            @Override
            public void onResponse(Call<allMessageBean> call, Response<allMessageBean> response) {

                Toast.makeText(MessaageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                holder.setgrid(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<allMessageBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


}
