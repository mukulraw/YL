package com.youthlive.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youthlive.youthlive.Activitys.MessaageActivity;
import com.youthlive.youthlive.Activitys.PersonalInfo;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.followPOJO.followBean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;
import com.youthlive.youthlive.sendMessagePOJO.sendMessageBean;
import com.youthlive.youthlive.timelineProfilePOJO.Data;
import com.youthlive.youthlive.timelineProfilePOJO.timelineProfileBean;

import java.util.ArrayList;
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

public class TimelineProfile extends AppCompatActivity {


    Toolbar toolbar;
    TabLayout tabs;
    ViewPager pager;
    ProgressBar progress;
    CircleImageView profile;

    static String userid;

    TextView fans;
    TextView followings;

    Button following , messgae;

    ViewPager coverPager;
    CircleIndicator indicator;

    Button follow;

    public Context appContext, myContext;
    public FragmentManager fm;

    String userName , userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_profile);
        toolbar = findViewById(R.id.toolbar);
        tabs = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);
        progress = findViewById(R.id.progress);
        profile = findViewById(R.id.profile);

        follow = (Button)findViewById(R.id.follow);
        following = (Button)findViewById(R.id.following);
        messgae = (Button)findViewById(R.id.message);

        userid = getIntent().getStringExtra("userId");


        messgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(TimelineProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.send_message_dialog);
                dialog.setCancelable(true);
                dialog.show();

                final EditText comment = (EditText)dialog.findViewById(R.id.message);
                final ProgressBar bar = (ProgressBar)dialog.findViewById(R.id.progress);
                Button submit = (Button)dialog.findViewById(R.id.send);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bar.setVisibility(View.VISIBLE);

                        String comm = comment.getText().toString();

                        if (comm.length() > 0)
                        {

                            progress.setVisibility(View.VISIBLE);

                            bean b = (bean) getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                            Call<sendMessageBean> call = cr.sendMessage(b.userId , userid , comm);

                            call.enqueue(new Callback<sendMessageBean>() {
                                @Override
                                public void onResponse(Call<sendMessageBean> call, Response<sendMessageBean> response) {


                                    dialog.dismiss();


                                    Intent intent = new Intent(TimelineProfile.this, MessaageActivity.class);
                                    startActivity(intent);



                                    bar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<sendMessageBean> call, Throwable t) {

                                    bar.setVisibility(View.GONE);

                                }
                            });

                        }

                    }
                });





            }
        });


        bean b = (bean)getApplicationContext();

        if (Objects.equals(userid, b.userId))
        {
            follow.setVisibility(View.GONE);
        }
        else
        {
            follow.setVisibility(View.VISIBLE);
        }


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<followBean> call = cr.follow(b.userId , userid);

                call.enqueue(new Callback<followBean>() {
                    @Override
                    public void onResponse(Call<followBean> call, Response<followBean> response) {

                        try {
                            Toast.makeText(TimelineProfile.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                            loadData();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                        }



                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<followBean> call = cr.follow(b.userId , userid);

                call.enqueue(new Callback<followBean>() {
                    @Override
                    public void onResponse(Call<followBean> call, Response<followBean> response) {

                        try {
                            Toast.makeText(TimelineProfile.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                            loadData();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });

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


        tabs.addTab(tabs.newTab().setText("Personal"));
        tabs.addTab(tabs.newTab().setText("Education"));
        tabs.addTab(tabs.newTab().setText("Career"));


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


        Call<timelineProfileBean> call = cr.getProfile2(b.userId , userid);

        call.enqueue(new retrofit2.Callback<timelineProfileBean>() {
            @Override
            public void onResponse(Call<timelineProfileBean> call, retrofit2.Response<timelineProfileBean> response) {

                try {
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


                        tabs.setupWithViewPager(pager);



                        if (Objects.equals(response.body().getData().getFriendStatus().getFriend(), "true"))
                        {
                            messgae.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            messgae.setVisibility(View.GONE);
                        }


                        if (Objects.equals(response.body().getData().getFriendStatus().getFollow(), "true"))
                        {
                            following.setVisibility(View.VISIBLE);
                            follow.setVisibility(View.GONE);
                        }
                        else
                        {
                            following.setVisibility(View.GONE);
                            follow.setVisibility(View.VISIBLE);
                        }


                        if (Objects.equals(response.body().getData().getFriendStatus().getFriend(), "true"))
                        {
                            messgae.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            messgae.setVisibility(View.GONE);
                        }


                    } else {
                        Toast.makeText(myContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                }



                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<timelineProfileBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }


    public class CoverPager extends FragmentStatePagerAdapter {

        List<com.youthlive.youthlive.timelineProfilePOJO.CoverImage> list = new ArrayList<>();

        public CoverPager(FragmentManager fm, List<com.youthlive.youthlive.timelineProfilePOJO.CoverImage> list) {
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
        String title[] = {"Personal", "Education", "Career"};

        public FragStatePAgerAdapter(FragmentManager fm, Data data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                Address2 frag = new Address2();
                Bundle b = new Bundle();

                b.putString("userId", data.getUserId());
                b.putString("phone", data.getPhone());
                b.putString("user", data.getUserName());
                b.putString("youth", data.getYouthLiveId());
                b.putString("gender", data.getGender());
                b.putString("birth", data.getBirthday());
                b.putString("bio", data.getBio());

                frag.setArguments(b);

                return frag;

            } else if (position == 1) {

                Education2 frag = new Education2();
                Bundle b = new Bundle();

                b.putString("userId", data.getUserId());
                frag.setArguments(b);
                frag.setData(data.getEducation());
                return frag;

            } else if (position == 2) {
                Career2 frag = new Career2();
                Bundle b = new Bundle();

                b.putString("userId", data.getUserId());
                frag.setArguments(b);
                frag.setData(data.getCareer());
                return frag;

            } else {
                return null;
            }


        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }


}
