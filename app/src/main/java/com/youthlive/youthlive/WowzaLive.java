package com.youthlive.youthlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

public class WowzaLive extends AppCompatActivity{

    private WZCameraView goCoderCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wowza_live);

        goCoderCameraView = (WZCameraView) findViewById(R.id.camera_preview);



    }




}
