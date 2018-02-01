package com.youthlive.youthlive;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 * Created by TBX on 11/8/2017.
 */

public class bean extends Application{

    public String BASE_URL = "http://nationproducts.in/";

    public String userId = "";
    String userName = "";
    String userImage = "";

    @Override
    public void onCreate() {
        super.onCreate();



        Twitter.initialize(this);

    }
}
