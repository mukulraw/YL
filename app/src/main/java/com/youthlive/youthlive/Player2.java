package com.youthlive.youthlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.configuration.WZStreamConfig;
import com.wowza.gocoder.sdk.api.h264.WZProfileLevel;
import com.wowza.gocoder.sdk.api.player.WZPlayerConfig;
import com.wowza.gocoder.sdk.api.player.WZPlayerView;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

public class Player2 extends AppCompatActivity implements WZStatusCallback {

    WZPlayerView player;
    String uri;
    WZStreamConfig wzPlayerConfig;
    private WowzaGoCoder goCoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);
        goCoder = WowzaGoCoder.init(this, "GOSK-C344-0103-177D-9E68-FCF9");
        uri = getIntent().getStringExtra("uri");

        player = (WZPlayerView)findViewById(R.id.player);


        wzPlayerConfig = new WZPlayerConfig();
        //wzPlayerConfig.setIsPlayback(true);


        wzPlayerConfig.setHostAddress("ec2-18-219-154-44.us-east-2.compute.amazonaws.com");
        wzPlayerConfig.setApplicationName("live");
        wzPlayerConfig.setStreamName(uri);
        wzPlayerConfig.setPortNumber(1935);



        wzPlayerConfig.setVideoEnabled(true);
        wzPlayerConfig.setVideoFrameWidth(640);
        wzPlayerConfig.setVideoFrameHeight(480);
        wzPlayerConfig.setVideoFramerate(30);
        wzPlayerConfig.setVideoKeyFrameInterval(30);
        wzPlayerConfig.setVideoBitRate(1500);
        wzPlayerConfig.setABREnabled(false);

        WZProfileLevel profileLevel = new WZProfileLevel(-1, -1);

        if (profileLevel.validate()) {
            wzPlayerConfig.setVideoProfileLevel(profileLevel);
        }


        wzPlayerConfig.setAudioEnabled(true);

        wzPlayerConfig.setAudioSampleRate(44100);
        wzPlayerConfig.setAudioChannels(1);
        wzPlayerConfig.setAudioBitRate(64000);




        //WZStreamingError configValidationError = wzPlayerConfig.validateForPlayback();

        //Log.d("Error Wowza" , configValidationError.getErrorDescription());

        //wzPlayerConfig.setHLSEnabled(true);

        if (player.isReadyToPlay())
        {
            //wzPlayerConfig.setPreRollBufferDuration(0.0);

            player.setLogLevel(2);

            player.play((WZPlayerConfig) wzPlayerConfig, this);

            Log.d("asdasdas" , "ready_to_play");

        }


    }

    @Override
    public void onWZStatus(WZStatus wzStatus) {
        Log.e("status" , String.valueOf(wzStatus.getState()));
    }

    @Override
    public void onWZError(WZStatus wzStatus) {

        Log.d("error" , wzStatus.getLastError().getErrorDescription());

    }
}
