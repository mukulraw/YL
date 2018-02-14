package com.youthlive.youthlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.Circle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.video.AudioTrack;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.Participant;
import com.twilio.video.Room;
import com.twilio.video.RoomState;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;
import com.veer.hiddenshot.HiddenShot;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;
import com.yasic.bubbleview.BubbleView;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.commentPOJO.commentBean;
import com.youthlive.youthlive.followPOJO.followBean;
import com.youthlive.youthlive.getIpdatedPOJO.Comment;
import com.youthlive.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.youthlive.youthlive.goLivePOJO.goLiveBean;
import com.youthlive.youthlive.liveCommentPOJO.liveCommentBean;
import com.youthlive.youthlive.requestConnectionPOJO.requestConnectionBean;
import com.youthlive.youthlive.singleVideoPOJO.singleVideoBean;
import com.youthlive.youthlive.startStreamPOJO.startStreamBean;
import com.youthlive.youthlive.streamPOJO.LiveStream;
import com.youthlive.youthlive.streamPOJO.streamBean;
import com.youthlive.youthlive.streamResponsePOJO.streamResponseBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LiveScreen extends AppCompatActivity implements WZStatusCallback {

    RecyclerView grid;
    RecyclerView grid2;

    private Room room;

    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    LinearLayoutManager manager2;
    ImageButton heart;

    //Toast toast;

    private LocalVideoTrack localVideoTrack;

    private LocalAudioTrack localAudioTrack;

    String giftURL = "", giftName = "";

    private BubbleView bubbleView;
    ImageButton close;
    ImageButton folloview_friends;
    //Broadcaster mBroadcaster;
    //SurfaceView mPreviewSurface;
    private static final String APPLICATION_ID = "gA1JdKySejF0GfA0ChIvVA";
    ListView following_friendList;
    String userId, friendid, str;
    ArrayList<String> name;
    ArrayList<String> img;
    ImageView back;
    TextView tv;

    String key = "";

    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    private boolean mPermissionsGranted = true;

    private String[] mRequiredPermissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };


    // followingfriend_adapter fd;
    String url = "http://nationproducts.in/youthlive/api/follow_unfollow.php";
    RequestQueue requestQueue;
    SharedPreferences settings;


    private WowzaGoCoder goCoder;

    // The GoCoder SDK camera view
    private WZCameraView goCoderCameraView;

    WZBroadcast goCoderBroadcaster;

    // The GoCoder SDK audio device
    private WZAudioDevice goCoderAudioDevice;

    // The broadcast configuration settings
    private WZBroadcastConfig goCoderBroadcastConfig;


    ProgressBar progress;

    private CameraCapturerCompat cameraCapturerCompat;

    TextView likeCount;

    String title;

    List<Comment> cList;
    List<com.youthlive.youthlive.getIpdatedPOJO.View> vList;

    String liveId = "";

    int count = 0;

    TextView viewCount;

    Integer[] gfts = {
            R.drawable.gift1,
            R.drawable.gift2,
            R.drawable.gift3,
            R.drawable.gift4,
            R.drawable.gift5,
            R.drawable.gift6
    };

    ImageButton chatIcon, switchCamera, crop;
    LinearLayout chat, actions;
    EditText comment;
    FloatingActionButton send;
    CircleImageView image;
    TextView username;

    TextView beans, level;


    LinearLayout giftLayout;
    ImageView giftIcon;
    TextView giftTitle;

    ProgressDialog progressDialog;

    RelativeLayout playerLayout1;

    String access = "";
    String sid = "";


    VideoView thumb;


    Timer t;


    public static final String ACCOUNT_SID = "ACf9d0e5a1986b1e86724cb7fbb6129960";
    public static final String API_KEY_SID = "SK0494c286aa25e050354405aaa5fc8526";
    public static final String API_KEY_SECRET = "FyGiHq3n82IgHoZ3Dil6EiC4zlNu4Ybc";
    private boolean disconnectedFromOnDestroy;
    private String TAG = "ddfsdf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_screen);


        thumb = (VideoView)findViewById(R.id.thumbnail_video_view);


        goCoder = WowzaGoCoder.init(this, "GOSK-C344-0103-177D-9E68-FCF9");

        playerLayout1 = (RelativeLayout)findViewById(R.id.player_layout1);

        viewCount = (TextView) findViewById(R.id.view_count);

        //toast = Toast.makeText(LiveScreen.this , null , Toast.LENGTH_SHORT);

        goCoderCameraView = (WZCameraView) findViewById(R.id.camera_preview);

        goCoderAudioDevice = new WZAudioDevice();


        goCoderBroadcaster = new WZBroadcast();

// Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_640x480);

// Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account

        //goCoderBroadcastConfig.setConnectionParameters(new WZDataMap());

// Designate the camera preview as the video source
        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);

// Designate the audio device as the audio broadcaster
        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);

        title = getIntent().getStringExtra("title");


        giftLayout = (LinearLayout) findViewById(R.id.gift_layout);
        giftIcon = (ImageView) findViewById(R.id.gift_icon);
        giftTitle = (TextView) findViewById(R.id.gift_title);

        progress = (ProgressBar) findViewById(R.id.progress);
        chat = (LinearLayout) findViewById(R.id.chat);
        actions = (LinearLayout) findViewById(R.id.actions);
        chatIcon = (ImageButton) findViewById(R.id.chat_icon);
        switchCamera = (ImageButton) findViewById(R.id.switch_camera);
        comment = (EditText) findViewById(R.id.comment);
        send = (FloatingActionButton) findViewById(R.id.send);

        crop = (ImageButton) findViewById(R.id.crop);

        beans = (TextView) findViewById(R.id.beans);
        level = (TextView) findViewById(R.id.level);

        likeCount = (TextView) findViewById(R.id.like_count);

        image = (CircleImageView) findViewById(R.id.image);
        username = (TextView) findViewById(R.id.name);

        cList = new ArrayList<>();
        vList = new ArrayList<>();

        //mPreviewSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);
        settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userId = settings.getString("userid", "");
        folloview_friends = findViewById(R.id.folloview_friends);
        //mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        //mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        //mBroadcaster.setTitle(getIntent().getStringExtra("title"));
        final bean b = (bean) getApplicationContext();

        username.setText(b.userName);


        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(b.userImage, image);


        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chat.getVisibility() == View.VISIBLE) {
                    chat.setVisibility(View.GONE);
                } else if (chat.getVisibility() == View.GONE) {
                    chat.setVisibility(View.VISIBLE);
                }


            }
        });


        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCoderCameraView.switchCamera();
            }
        });


        //mBroadcaster.setAuthor(b.userImage);
        //mBroadcaster.setSendPosition(true);
        //mBroadcaster.setCustomData(b.userId);
        //mBroadcaster.setSaveOnServer(false);
        grid = (RecyclerView) findViewById(R.id.grid);
        grid2 = (RecyclerView) findViewById(R.id.grid2);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        heart = (ImageButton) findViewById(R.id.heart);
        folloview_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  follow();
            }
        });
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();
                //finish();


                WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();


                if (configValidationError != null) {
                    //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                } else if (goCoderBroadcaster.getStatus().isRunning()) {
                    // Stop the broadcast that is currently running
                    goCoderBroadcaster.endBroadcast(LiveScreen.this);
                } else {
                    // Start streaming
                    goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, LiveScreen.this);
                }

                t.cancel();

                finish();



            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

            }
        });

        adapter = new LiveAdapter(this, vList);
        adapter2 = new LiveAdapter2(this, cList);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        grid2.setAdapter(adapter2);
        grid2.setLayoutManager(manager2);


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


                    Call<liveCommentBean> call = cr.commentLive(b.userId, liveId, mess);

                    call.enqueue(new Callback<liveCommentBean>() {
                        @Override
                        public void onResponse(Call<liveCommentBean> call, retrofit2.Response<liveCommentBean> response) {


                            if (Objects.equals(response.body().getMessage(), "Video Comment Success")) {
                                comment.setText("");
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<liveCommentBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }

            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //               HiddenShot.getInstance().buildShotAndShare(LiveScreen.this,"Check this out");

            }
        });

        bubbleView = (BubbleView) findViewById(R.id.bubble);
        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_cyan_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_light_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_lime_a200_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_pink_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_red_900_24dp));
        bubbleView.setDrawableList(drawableList);


        /*Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {


*//*
                if (mBroadcaster.canStartBroadcasting()) {
                    mBroadcaster.startBroadcast();
                }
*//*


            }
        }, 3000);*/


        progressDialog = new ProgressDialog(LiveScreen.this);

        progressDialog.setMessage("Starting Stream...");

        progressDialog.setCancelable(false);

        progressDialog.show();




        //progress.setVisibility(View.VISIBLE);


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<goLiveBean> call3 = cr.goLive(b.userId, b.userId, "");

        call3.enqueue(new Callback<goLiveBean>() {
            @Override
            public void onResponse(Call<goLiveBean> call, retrofit2.Response<goLiveBean> response) {

                if (Objects.equals(response.body().getStatus(), "1")) {
                    //Toast.makeText(LiveScreen.this, "You are now live", Toast.LENGTH_SHORT).show();
                    liveId = response.body().getData().getLiveId();

                    actions.setVisibility(View.VISIBLE);


                    Log.d("liveId", liveId);
                    Log.d("userId", b.userId);



                    goCoderBroadcastConfig.setHostAddress("ec2-18-219-154-44.us-east-2.compute.amazonaws.com");
                    goCoderBroadcastConfig.setPortNumber(1935);
                    goCoderBroadcastConfig.setApplicationName("live");
                    goCoderBroadcastConfig.setStreamName(liveId);


                    WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

                    if (configValidationError != null) {
                        //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                    } else if (goCoderBroadcaster.getStatus().isRunning()) {
                        // Stop the broadcast that is currently running
                        goCoderBroadcaster.endBroadcast(LiveScreen.this);
                    } else {
                        // Start streaming
                        goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, LiveScreen.this);
                    }


                    progressDialog.dismiss();


                    schedule(liveId);

                } else {
                    Toast.makeText(LiveScreen.this, "Error going on live", Toast.LENGTH_SHORT).show();
                    finish();
                }

                //progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<goLiveBean> call, Throwable t) {
                //progress.setVisibility(View.GONE);
            }
        });

















    }







    private void checkStatus(final String id, final String host, final String port, final String appName, final String streamName) {

        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.cloud.wowza.com/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<startStreamBean> call2 = cr.getState(id);

                //Log.d("startId", id);

                call2.enqueue(new Callback<startStreamBean>() {
                    @Override
                    public void onResponse(Call<startStreamBean> call, retrofit2.Response<startStreamBean> response) {

                        Log.d("status", response.body().getLiveStream().getState());

                        String stat = response.body().getLiveStream().getState();

                        if (Objects.equals(stat, "started")) {

                            //progress.setVisibility(View.GONE);

                            progressDialog.dismiss();









                            t.cancel();


                        }


                    }

                    @Override
                    public void onFailure(Call<startStreamBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);

                    }
                });


            }
        }, 0, 800);

    }


    public void BlockPersson(View view) {
        PersonBlock();
    }

    @Override
    public void onWZStatus(WZStatus wzStatus) {
        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");

        switch (wzStatus.getState()) {
            case WZState.STARTING:
                statusMessage.append("Broadcast initialization");
                break;

            case WZState.READY:
                statusMessage.append("Ready to begin streaming");
                break;

            case WZState.RUNNING:
                statusMessage.append("Streaming is active");
                break;

            case WZState.STOPPING:
                statusMessage.append("Broadcast shutting down");
                break;

            case WZState.IDLE:
                statusMessage.append("The broadcast is stopped");
                break;

            default:
                return;
        }

        // Display the status message using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(LiveScreen.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWZError(final WZStatus wzStatus) {
// If an error is reported by the GoCoder SDK, display a message
        // containing the error details using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                /*Toast.makeText(LiveScreen.this,
                        "Streaming error: " + wzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

        List<com.youthlive.youthlive.getIpdatedPOJO.View> list = new ArrayList<>();
        Context context;

        public LiveAdapter(Context context, List<com.youthlive.youthlive.getIpdatedPOJO.View> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<com.youthlive.youthlive.getIpdatedPOJO.View> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewers_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final com.youthlive.youthlive.getIpdatedPOJO.View item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getUserImage(), holder.image , options);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", item.getUserId());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (CircleImageView) itemView.findViewById(R.id.image);

            }
        }
    }


    public class LiveAdapter2 extends RecyclerView.Adapter<LiveAdapter2.ViewHolder> {


        List<Comment> list = new ArrayList<>();
        Context context;

        public LiveAdapter2(Context context, List<Comment> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<Comment> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.chat_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final Comment item = list.get(position);

            if (position == 0) {

                holder.name.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.gradient_white_top, 0, 0);

            } else if (position == list.size() - 1) {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.gradient_white);
            } else {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getUserImage(), holder.index);

            final bean b = (bean) context.getApplicationContext();

            holder.user.setText(item.getUserName());

            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", item.getUserId());
                    startActivity(intent);

                }
            });

            if (Objects.equals(item.getFriendStatus().getFollow(), "true")) {
                holder.add.setBackgroundResource(R.drawable.tick);
            } else {
                holder.add.setBackgroundResource(R.drawable.plus_red);
            }

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    Dialog dialog = new Dialog(LiveScreen.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.connect_dialog);
                    dialog.setCancelable(true);
                    dialog.show();


                    CircleImageView image = (CircleImageView)dialog.findViewById(R.id.image);
                    TextView name = (TextView)dialog.findViewById(R.id.name);
                    Button follo = (Button)dialog.findViewById(R.id.follow);
                    Button connect = (Button)dialog.findViewById(R.id.connect);



                    ImageLoader loader1 = ImageLoader.getInstance();

                    loader1.displayImage(b.userImage , image);

                    name.setText(b.userName);

                    follo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            progress.setVisibility(View.VISIBLE);

                            final bean b = (bean) context.getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                            retrofit2.Call<followBean> call = cr.follow(b.userId, item.getUserId());

                            call.enqueue(new retrofit2.Callback<followBean>() {
                                @Override
                                public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

                                    progress.setVisibility(View.GONE);

                                }
                            });

                        }
                    });






                    connect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {







                            if (sid.length() > 0)
                            {
                                progress.setVisibility(View.VISIBLE);

                                final bean b = (bean) context.getApplicationContext();

                                final Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.BASE_URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                final AllAPIs cr = retrofit.create(AllAPIs.class);



                                Call<requestConnectionBean> call = cr.requestConnection(liveId , b.userId , item.getUserId());

                                call.enqueue(new Callback<requestConnectionBean>() {
                                    @Override
                                    public void onResponse(Call<requestConnectionBean> call, retrofit2.Response<requestConnectionBean> response) {



                                        playerLayout1.setVisibility(View.VISIBLE);



                                        progress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            }
                            else
                            {

                                final VideoGrant grant = new VideoGrant();
                                grant.setRoom(liveId);

                                // Create an Access Token



                                final AccessToken token = new AccessToken.Builder(ACCOUNT_SID, API_KEY_SID, API_KEY_SECRET)
                                        .identity(b.userId) // Set the Identity of this token
                                        .grant(grant) // Grant access to Video
                                        .build();

                                // Serialize the token as a JWT
                                //final String jwt = token.toJwt();
                                //System.out.println(jwt);

                                String toke = token.toString();

                                Log.d("token" , toke);


                                connectToRoom(liveId , toke);





                            }





















                        }
                    });




                }
            });


            if (Objects.equals(item.getUserId(), b.userId)) {
                holder.add.setVisibility(View.GONE);
            } else {
                holder.add.setVisibility(View.VISIBLE);
            }

            holder.name.setText(item.getComment());

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, user;
            CircleImageView index;
            ImageButton add;

            public ViewHolder(View itemView) {
                super(itemView);

                index = (CircleImageView) itemView.findViewById(R.id.index);
                name = (TextView) itemView.findViewById(R.id.name);
                user = (TextView) itemView.findViewById(R.id.user);
                add = (ImageButton) itemView.findViewById(R.id.add);

            }
        }
    }


    private void connectToRoom(String roomName, String accessToken) {
        //configureAudio(true);
        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(accessToken)
                .roomName(roomName);

        /*
         * Add local audio track to connect options to share with participants.
         */
        if (localAudioTrack != null) {
            connectOptionsBuilder
                    .audioTracks(Collections.singletonList(localAudioTrack));
        }

        cameraCapturerCompat = new CameraCapturerCompat(this, getAvailableCameraSource());

        if (localVideoTrack == null) {
            localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturerCompat.getVideoCapturer());
//            localVideoTrack.addRenderer(localVideoView);

            /*
             * If connected to a Room then share the local video track.
             */

        }

        /*
         * Add local video track to connect options to share with participants.
         */
        if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));
        }
        room = Video.connect(this, connectOptionsBuilder.build(), roomListener());
        //setDisconnectAction();
    }


    private void PersonBlock() {
        final Dialog dialog = new Dialog(LiveScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blockpersom_dialog);
        dialog.show();
    }


    private CameraCapturer.CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) ?
                (CameraCapturer.CameraSource.FRONT_CAMERA) :
                (CameraCapturer.CameraSource.BACK_CAMERA);
    }



    public void schedule(final String vid) {

        Log.d("hgfjhg", vid);

        t = new Timer();
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


                Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, vid);


                call.enqueue(new Callback<getUpdatedBean>() {
                    @Override
                    public void onResponse(Call<getUpdatedBean> call, retrofit2.Response<getUpdatedBean> response) {

                        try {

                            adapter2.setGridData(response.body().getData().getComments());
                            adapter.setGridData(response.body().getData().getViews());

                            int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                            beans.setText(response.body().getData().getBeans());
                            level.setText(response.body().getData().getLevel());


                            viewCount.setText(response.body().getData().getViewsCount());


                            if (response.body().getData().getGift().size() > 0) {
                                try {

                                    giftName = response.body().getData().getGift().get(0).getGiftId();

                                    showGift(Integer.parseInt(response.body().getData().getGift().get(0).getGiftId()), response.body().getData().getGift().get(0).getGiftName());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                            if (count1 > count) {
                                for (int i = 0; i < count1 - count; i++)

                                    bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                                likeCount.setText(response.body().getData().getLikesCount());

                                count = count1;
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<getUpdatedBean> call, Throwable t) {

                        Log.d("asdasd", t.toString());

                    }
                });

            }
        }, 0, 2000);

    }

    public void showGift(final int pos, String title) {

        giftLayout.setVisibility(View.VISIBLE);

        Glide.with(getApplicationContext()).load(gfts[pos]).into(giftIcon);
        giftTitle.setText(title);

        final Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {


                giftLayout.getHandler().post(new Runnable() {
                    public void run() {
                        giftLayout.setVisibility(View.GONE);
                    }
                });
                t.cancel();

            }
        }, 2500);


    }


    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(Room room) {
                //localParticipant = room.getLocalParticipant();
                //videoStatusTextView.setText("Connected to " + room.getName());
                //setTitle(room.getName());

                for (Participant participant : room.getParticipants()) {
                    addParticipant(participant);
                    break;
                }
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                //videoStatusTextView.setText("Failed to connect");
                //configureAudio(false);
                //intializeUI();
            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
                //localParticipant = null;
                //videoStatusTextView.setText("Disconnected from " + room.getName());
                //VideoActivity.this.room = null;
                // Only reinitialize the UI if disconnect was not called from onDestroy()
                /*if (!disconnectedFromOnDestroy) {
                    configureAudio(false);
                    intializeUI();
                    moveLocalVideoToPrimaryView();
                }*/
            }

            @Override
            public void onParticipantConnected(Room room, Participant participant) {
                addParticipant(participant);
            }

            @Override
            public void onParticipantDisconnected(Room room, Participant participant) {
                //removeParticipant(participant);
            }

            @Override
            public void onRecordingStarted(Room room) {
                /*
                 * Indicates when media shared to a Room is being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStarted");
            }

            @Override
            public void onRecordingStopped(Room room) {
                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStopped");
            }
        };
    }

    private void addParticipant(Participant participant) {
        /*
         * This app only displays video for one additional participant per Room
         */

        //participantIdentity = participant.getIdentity();
        //videoStatusTextView.setText("Participant "+ participantIdentity + " joined");

        /*
         * Add participant renderer
         */
        if (participant.getVideoTracks().size() > 0) {
            addParticipantVideo(participant.getVideoTracks().get(0));
        }

        /*
         * Start listening for participant events
         */
        participant.setListener(participantListener());
    }

    private void addParticipantVideo(VideoTrack videoTrack) {
        //moveLocalVideoToThumbnailView();
        //primaryVideoView.setMirror(false);
        videoTrack.addRenderer(thumb);
    }

    private Participant.Listener participantListener() {
        return new Participant.Listener() {
            @Override
            public void onAudioTrackAdded(Participant participant, AudioTrack audioTrack) {
                //videoStatusTextView.setText("onAudioTrackAdded");
            }

            @Override
            public void onAudioTrackRemoved(Participant participant, AudioTrack audioTrack) {
                //videoStatusTextView.setText("onAudioTrackRemoved");
            }

            @Override
            public void onVideoTrackAdded(Participant participant, VideoTrack videoTrack) {
                //videoStatusTextView.setText("onVideoTrackAdded");
                //addParticipantVideo(videoTrack);
            }

            @Override
            public void onVideoTrackRemoved(Participant participant, VideoTrack videoTrack) {
                //videoStatusTextView.setText("onVideoTrackRemoved");
                //removeParticipantVideo(videoTrack);
            }

            @Override
            public void onAudioTrackEnabled(Participant participant, AudioTrack audioTrack) {

            }

            @Override
            public void onAudioTrackDisabled(Participant participant, AudioTrack audioTrack) {

            }

            @Override
            public void onVideoTrackEnabled(Participant participant, VideoTrack videoTrack) {

            }

            @Override
            public void onVideoTrackDisabled(Participant participant, VideoTrack videoTrack) {

            }
        };
    }


    @Override
    public void onPause() {
        super.onPause();
        //mBroadcaster.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
            if (!mPermissionsGranted)
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
        } else
            mPermissionsGranted = true;

        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView.isPreviewPaused())
                goCoderCameraView.onResume();
            else
                goCoderCameraView.startPreview();
        }

        //mBroadcaster.setCameraSurface(mPreviewSurface);
        //mBroadcaster.onActivityResume();
    }

    public void follow() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals(0)) {
                        JSONObject obj2 = jObj.getJSONObject("data");
                        userId = obj2.getString("userId");
                        friendid = obj2.getString("friendId");

                    } else {
                        str = jObj.getString("message");
                        //Toast.makeText(LiveScreen.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LiveScreen.this, error.toString(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                bean b = new bean();
                params.put("userId", "170");
                params.put("friendId", "19");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // Check the result of each permission granted
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    //
// Utility method to check the status of a permissions request for an array of permission identifiers
//
    private static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();


        if (configValidationError != null) {
            //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
        } else if (goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast(LiveScreen.this);
        } else {
            // Start streaming
            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, LiveScreen.this);
        }


        t.cancel();

        finish();



    }


    @Override
    protected void onStop() {
        super.onStop();

        t.cancel();

    }

    @Override
    protected void onDestroy() {
        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room.getState() != RoomState.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }

        super.onDestroy();
    }

}
