package com.youthlive.youthlive.Activitys;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.youthlive.youthlive.Adapter.Pager2Adapter;
import com.youthlive.youthlive.Adapter.PagerAdapter;
import com.youthlive.youthlive.Address;
import com.youthlive.youthlive.Career;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.MyVLOGs;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.bean;
import com.youthlive.youthlive.followPOJO.followBean;
import com.youthlive.youthlive.loginResponsePOJO.CoverImage;
import com.youthlive.youthlive.loginResponsePOJO.Data;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyVlog extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager pager;
    ProgressBar progress;
    CircleImageView profile;

    static String userid;

    TextView fans;
    TextView followings;

    ViewPager coverPager;
    CircleIndicator indicator;

    public Context appContext, myContext;
    public FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vlog);
        toolbar = findViewById(R.id.toolbar);
        pager = findViewById(R.id.pager);
        progress = findViewById(R.id.progress);
        profile = findViewById(R.id.profile);


        userid = getIntent().getStringExtra("userId");


        coverPager = findViewById(R.id.cover_pager);
        indicator = findViewById(R.id.indicator);

        fans = findViewById(R.id.fans);
        followings = findViewById(R.id.followings);

        appContext = getApplicationContext();
        myContext = this;
        fm = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }


    public void loadData() {


        progress.setVisibility(View.VISIBLE);

        bean b = (bean) appContext;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<loginResponseBean> call = cr.getProfile(userid);

        call.enqueue(new retrofit2.Callback<loginResponseBean>() {
            @Override
            public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {


                if (Objects.equals(response.body().getStatus(), "1")) {

                    CoverPager pageAdapter = new CoverPager(fm, response.body().getData().getCoverImage());
                    coverPager.setAdapter(pageAdapter);
                    indicator.setViewPager(coverPager);


                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(response.body().getData().getUserImage(), profile);

                    toolbar.setTitle(response.body().getData().getUserName());
                    toolbar.setSubtitle(Html.fromHtml("Youth Live ID: <b>" + response.body().getData().getYouthLiveId() + "</b>"));

                    fans.setText(String.valueOf(response.body().getData().getFans()));
                    followings.setText(String.valueOf(response.body().getData().getFollowings()));

                    FragStatePAgerAdapter adapter = new FragStatePAgerAdapter(fm, response.body().getData());
                    pager.setAdapter(adapter);


                } else {
                    Toast.makeText(myContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<loginResponseBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    public class CoverPager extends FragmentStatePagerAdapter {

        List<CoverImage> list = new ArrayList<>();

        public CoverPager(FragmentManager fm, List<com.youthlive.youthlive.loginResponsePOJO.CoverImage> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            com.youthlive.youthlive.CoverImage frag = new com.youthlive.youthlive.CoverImage();
            Bundle b = new Bundle();
            b.putString("url", list.get(position).getImage());
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    /*public class CoverImage extends Fragment
    {

        String url;
        ImageView image;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.cober_image_layout , container , false);

            url = getArguments().getString("url");
            image = (ImageView)view.findViewById(R.id.image);

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url , image);


            return  view;
        }
    }*/


    public class FragStatePAgerAdapter extends FragmentStatePagerAdapter {

        Data data;

        public FragStatePAgerAdapter(FragmentManager fm, Data data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            return new MyVLOGs();

        }

        @Override
        public int getCount() {
            return 1;
        }

    }

}
