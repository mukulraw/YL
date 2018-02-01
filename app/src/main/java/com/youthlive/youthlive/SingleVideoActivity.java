package com.youthlive.youthlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import com.youthlive.youthlive.Activitys.PersonalInfo;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.commentPOJO.commentBean;
import com.youthlive.youthlive.sharePOJO.shareBean;
import com.youthlive.youthlive.singleVideoPOJO.Comment;
import com.youthlive.youthlive.singleVideoPOJO.singleVideoBean;
import com.youthlive.youthlive.vlogListPOJO.vlogListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import uk.co.jakelee.vidsta.VidstaPlayer;

public class SingleVideoActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView profile;
    TextView name, time, views, comments, likes, share;
    RecyclerView grid;
    LinearLayoutManager manager;
    EditText comment;
    CommentsAdapter adapter;
    List<Comment> list;
    ProgressBar progress;
    String videoId, url;

    int count = 0;

    String timelineId;

    FloatingActionButton send;

    private int mSeekPosition;

    VidstaPlayer player;
    ImageButton play;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);
        videoId = getIntent().getStringExtra("videoId");
        url = getIntent().getStringExtra("url");
        list = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        player = (VidstaPlayer) findViewById(R.id.player);

        player.setVideoSource(url);

        player.setAutoLoop(true);
        player.setAutoPlay(true);

        player.setFullScreenButtonVisible(false);
        player.setFullScreen(false);

        play = (ImageButton) findViewById(R.id.play);

        share = (TextView) findViewById(R.id.share);

        progress = (ProgressBar) findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
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
        profile = (CircleImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        views = (TextView) findViewById(R.id.views);
        comments = (TextView) findViewById(R.id.comments);
        likes = (TextView) findViewById(R.id.like);
        comment = (EditText) findViewById(R.id.comment);
        grid = (RecyclerView) findViewById(R.id.grid);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentsAdapter(this, list);
        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);

        send = (FloatingActionButton) findViewById(R.id.send);

        likes.setOnClickListener(new View.OnClickListener() {
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
                Call<singleVideoBean> call = cr.likeVideo(b.userId, videoId);
                call.enqueue(new Callback<singleVideoBean>() {
                    @Override
                    public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {

                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<singleVideoBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        });
        /*comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getRawX() >= (comment.getRight() - comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60)
                {
                    // your action here

                    //og.d("asdasd" , "clicked");



                    String mess = comment.getText().toString();

                    if (mess.length() > 0)
                    {
                        progress.setVisibility(View.VISIBLE);

                        final bean b = (bean) getApplicationContext();

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<commentBean> call = cr.comment(b.userId , videoId , mess);

                        call.enqueue(new Callback<commentBean>() {
                            @Override
                            public void onResponse(Call<commentBean> call, Response<commentBean> response) {


                                if (Objects.equals(response.body().getMessage(), "Video Comment Success"))
                                {
                                    comment.setText("");
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<commentBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                                Log.d("Video upload find " , t.toString());
                            }
                        });
                    }


                    return true;
                }
                return false;
            }
        });
*/


        share.setOnClickListener(new View.OnClickListener() {
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


                Call<shareBean> call = cr.share(b.userId, videoId);

                call.enqueue(new Callback<shareBean>() {
                    @Override
                    public void onResponse(Call<shareBean> call, Response<shareBean> response) {

                        try {
                            Toast.makeText(SingleVideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<shareBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        t.printStackTrace();
                    }
                });


            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mess = comment.getText().toString();

                if (mess.length() > 0) {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<commentBean> call = cr.comment(b.userId, videoId, mess);

                    call.enqueue(new Callback<commentBean>() {
                        @Override
                        public void onResponse(Call<commentBean> call, Response<commentBean> response) {

                            try {
                                if (Objects.equals(response.body().getMessage(), "Video Comment Success")) {
                                    comment.setText("");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<commentBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }


            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleVideoActivity.this, TimelineProfile.class);

                intent.putExtra("userId", timelineId);
                startActivity(intent);

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleVideoActivity.this, TimelineProfile.class);

                intent.putExtra("userId", timelineId);
                startActivity(intent);

            }
        });


        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mess = comment.getText().toString();

                if (mess.length() > 0)
                {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<vlogListBean> call = cr.comment(b.userId , videoId , mess);

                    call.enqueue(new Callback<vlogListBean>() {
                        @Override
                        public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<vlogListBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });*/

        //video.setMediaController(controller);


        /*video.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d("Asdasd", "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d("Asdasd", "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d("Asdasd", "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d("asdasd", "onBufferingEnd UniversalVideoView callback");
            }

        });*/


/*
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //video.setVideoPath(url);
                //video.requestFocus();

                player.setVideoSource(url);

                if (mSeekPosition > 0) {
                    //video.seekTo(mSeekPosition);
                }
                //video.start();
                //holder.controller.setTitle("Big Buck Bunny");
            }
        });
*/


        //Uri uri = Uri.parse(url);
        /*video.setVideoURI(uri);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done

                mp.setLooping(true);
            }
        });*/


    }


    @Override
    protected void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);


        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<singleVideoBean> call = cr.getsingleVideo(b.userId, videoId);

        call.enqueue(new Callback<singleVideoBean>() {
            @Override
            public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {

                try {
                    if (response.body().getData().getComments() != null) {
                        adapter.setGridData(response.body().getData().getComments());
                        grid.smoothScrollToPosition(adapter.getItemCount() - 1);

                        count = response.body().getData().getComments().size();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    timelineId = response.body().getData().getTimelineId();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);

                try {
                    views.setText(response.body().getData().getViewsCount());
                    likes.setText(response.body().getData().getLikesCount());
                    comments.setText(response.body().getData().getCommentCount());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(response.body().getData().getTimelineProfileImage(), profile);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    name.setText(response.body().getData().getTimelineName());

                    time.setText(response.body().getData().getUploadTime());

                    schedule();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<singleVideoBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }


    public void schedule() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<singleVideoBean> call = cr.getsingleVideo(b.userId, videoId);

                call.enqueue(new Callback<singleVideoBean>() {
                    @Override
                    public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {


                        try {
                            if (response.body().getData().getComments() != null) {
                                adapter.setGridData(response.body().getData().getComments());


                                if (response.body().getData().getComments().size() > count) {
                                    grid.smoothScrollToPosition(adapter.getItemCount() - 1);
                                    count = response.body().getData().getComments().size();
                                }


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            views.setText(response.body().getData().getViewsCount());
                            likes.setText(response.body().getData().getLikesCount());
                            comments.setText(response.body().getData().getCommentCount());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<singleVideoBean> call, Throwable t) {

                    }
                });

            }
        }, 0, 1000);

    }


    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

        List<Comment> list = new ArrayList<>();
        Context context;

        public CommentsAdapter(Context context, List<Comment> list) {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Comment> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.comment_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            Comment item = list.get(position);
            holder.message.setText(item.getComment());
            holder.time.setText(item.getTime());
            holder.name.setText(item.getUserName());

            bean b = (bean) context.getApplicationContext();


            if (Objects.equals(item.getUserId(), b.userId)) {
                holder.container.setGravity(Gravity.END);
                holder.bubble.setBackgroundResource(R.drawable.bubble_me);
            } else {
                holder.container.setGravity(Gravity.START);
                holder.bubble.setBackgroundResource(R.drawable.bubble);
            }


        }

        @Override
        public int getItemCount() {
            //return list.size();
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView message, time, name;
            LinearLayout bubble, container;

            public ViewHolder(View itemView) {
                super(itemView);

                message = (TextView) itemView.findViewById(R.id.message);
                time = (TextView) itemView.findViewById(R.id.time);
                name = (TextView) itemView.findViewById(R.id.name);
                bubble = (LinearLayout) itemView.findViewById(R.id.bubble);
                container = (LinearLayout) itemView.findViewById(R.id.container);

            }
        }

    }


}
