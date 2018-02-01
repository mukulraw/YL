package com.youthlive.youthlive;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

public class WowzaPlayer extends AppCompatActivity {

    VideoView video;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wowza_player);

        url = getIntent().getStringExtra("uri");
        Log.d("url" , url);

        video = (VideoView)findViewById(R.id.video);


        video.setVideoURI(Uri.parse(url));
        video.requestFocus();
        video.start();



    }
}
