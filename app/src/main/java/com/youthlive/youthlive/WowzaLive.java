package com.youthlive.youthlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

import net.ossrs.rtmp.ConnectCheckerRtmp;

public class WowzaLive extends AppCompatActivity implements ConnectCheckerRtsp{

    SurfaceView surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wowza_live);

        surface = (SurfaceView)findViewById(R.id.surface);

        //RtmpCamera2 rtmpCamera1 = new RtmpCamera2(surface , this);

        RtspCamera1 rtmpCamera1 = new RtspCamera1(surface, this);


        if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
            //rtmpCamera1.setAuthorization("client" , "12345");
            rtmpCamera1.startStream("rtsp://7f5475.entrypoint.cloud.wowza.com/app-680e");
        } else {
            /**This device cant init encoders, this could be for 2 reasons: The encoder selected doesnt support any configuration setted or your device hasnt a H264 or AAC encoder (in this case you can see log error valid encoder not found)*/
        }

    }



    @Override
    public void onConnectionSuccessRtsp() {

    }

    @Override
    public void onConnectionFailedRtsp(String s) {

        Log.d("failed" , s);

    }

    @Override
    public void onDisconnectRtsp() {

    }

    @Override
    public void onAuthErrorRtsp() {

    }

    @Override
    public void onAuthSuccessRtsp() {

        Log.d("success" , "success");

    }
}
