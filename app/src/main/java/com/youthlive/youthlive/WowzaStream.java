package com.youthlive.youthlive;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastAPI;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.data.WZDataMap;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZError;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.youthlive.youthlive.startStreamPOJO.startStreamBean;
import com.youthlive.youthlive.streamPOJO.LiveStream;
import com.youthlive.youthlive.streamPOJO.streamBean;
import com.youthlive.youthlive.streamResponsePOJO.streamResponseBean;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WowzaStream extends AppCompatActivity implements WZStatusCallback, View.OnClickListener {

    private WowzaGoCoder goCoder;

    // The GoCoder SDK camera view
    private WZCameraView goCoderCameraView;

    WZBroadcast goCoderBroadcaster;

    // The GoCoder SDK audio device
    private WZAudioDevice goCoderAudioDevice;

    // The broadcast configuration settings
    private WZBroadcastConfig goCoderBroadcastConfig;

    // Properties needed for Android 6+ permissions handling
    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    private boolean mPermissionsGranted = true;

    private String[] mRequiredPermissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };

    Button broadcastButton;


    String title;

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wowza_stream);

        goCoder = WowzaGoCoder.init(this, "GOSK-A544-0103-01C9-96AE-1E94");

        progress = (ProgressBar) findViewById(R.id.progress);

        title = getIntent().getStringExtra("title");

//        goCoder = WowzaGoCoder.init(getApplicationContext(), "GOSK-9544-0103-2C2C-0EB0-4299");

        /*if (goCoder == null) {
            // If initialization failed, retrieve the last error and display it
            WZError goCoderInitError = WowzaGoCoder.getLastError();
            Toast.makeText(this,
                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
                    Toast.LENGTH_LONG).show();
            return;
        }*/


        goCoderCameraView = (WZCameraView) findViewById(R.id.camera_preview);

// Create an audio device instance for capturing and broadcasting audio
        goCoderAudioDevice = new WZAudioDevice();


        goCoderBroadcaster = new WZBroadcast();

// Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_1920x1080);

// Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account

        //goCoderBroadcastConfig.setConnectionParameters(new WZDataMap());

// Designate the camera preview as the video source
        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);

// Designate the audio device as the audio broadcaster
        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);


        broadcastButton = (Button) findViewById(R.id.broadcast_button);
        broadcastButton.setOnClickListener(this);


        /*goCoderBroadcastConfig.setHostAddress("192.168.100.32");
        goCoderBroadcastConfig.setPortNumber(1935);
        goCoderBroadcastConfig.setApplicationName("live");
        goCoderBroadcastConfig.setStreamName("myStream");
        goCoderBroadcastConfig.setUsername("mukul");
        goCoderBroadcastConfig.setPassword("12345");
*/

        broadcastButton.setVisibility(View.VISIBLE);



        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cloud.wowza.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        streamBean bosy = new streamBean();
        LiveStream stream = new LiveStream();
        stream.setName(title);
        bosy.setLiveStream(stream);

        Call<streamResponseBean> call = cr.createStream(bosy);

        call.enqueue(new Callback<streamResponseBean>() {
            @Override
            public void onResponse(Call<streamResponseBean> call, Response<streamResponseBean> response) {

                final String host = response.body().getLiveStream().getSourceConnectionInformation().getPrimaryServer();
                final String port = String.valueOf(response.body().getLiveStream().getSourceConnectionInformation().getHostPort());
                final String appName = response.body().getLiveStream().getSourceConnectionInformation().getApplication();
                final String streamName = response.body().getLiveStream().getSourceConnectionInformation().getStreamName();

                final String id = response.body().getLiveStream().getId();

                Call<startStreamBean> call2 = cr.startStream(id);

                Log.d("startId", id);

                call2.enqueue(new Callback<startStreamBean>() {
                    @Override
                    public void onResponse(Call<startStreamBean> call, Response<startStreamBean> response) {

                        Log.d("status", response.body().getLiveStream().getState());
                        Toast.makeText(WowzaStream.this, response.body().getLiveStream().getState(), Toast.LENGTH_SHORT).show();


                        checkStatus(id, host, port, appName, streamName);


                    }

                    @Override
                    public void onFailure(Call<startStreamBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);

                    }
                });


            }

            @Override
            public void onFailure(Call<streamResponseBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                t.printStackTrace();
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
                    public void onResponse(Call<startStreamBean> call, Response<startStreamBean> response) {

                        Log.d("status", response.body().getLiveStream().getState());

                        String stat = response.body().getLiveStream().getState();

                        if (Objects.equals(stat, "started"))
                        {

                            progress.setVisibility(View.GONE);

                            goCoderBroadcastConfig.setHostAddress(host);
                            goCoderBroadcastConfig.setPortNumber(Integer.parseInt(port));
                            goCoderBroadcastConfig.setApplicationName(appName);
                            goCoderBroadcastConfig.setStreamName(streamName);

                            broadcastButton.setVisibility(View.VISIBLE);

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


    @Override
    protected void onResume() {
        super.onResume();

        // If running on Android 6 (Marshmallow) or above, check to see if the necessary permissions
        // have been granted
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null)
            rootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    public void onWZStatus(WZStatus wzStatus) {
        // A successful status transition has been reported by the GoCoder SDK
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
                Toast.makeText(WowzaStream.this, statusMessage, Toast.LENGTH_LONG).show();
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
                Toast.makeText(WowzaStream.this,
                        "Streaming error: " + wzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // return if the user hasn't granted the app the necessary permissions
        if (!mPermissionsGranted) return;

        // Ensure the minimum set of configuration settings have been specified necessary to
        // initiate a broadcast streaming session
        WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

        if (configValidationError != null) {
            Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
        } else if (goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast(this);
        } else {
            // Start streaming

            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
        }
    }
}
