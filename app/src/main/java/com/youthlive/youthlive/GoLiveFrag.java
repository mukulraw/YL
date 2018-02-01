package com.youthlive.youthlive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.games.Player;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TBX on 11/22/2017.
 */

public class GoLiveFrag extends Fragment {

    Button goLive;
    EditText title;
    CircleImageView profile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_go_live , container , false);


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));


        profile = (CircleImageView)view.findViewById(R.id.profile);
        goLive = (Button)view.findViewById(R.id.golive);


        title = (EditText)view.findViewById(R.id.title);

        bean b = (bean)getContext().getApplicationContext();

        ImageLoader loader = ImageLoader.getInstance();

        title.setText(b.userName + " is Live");

        loader.displayImage(b.userImage , profile);

        goLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , LiveScreen.class);
                //Intent intent = new Intent(getContext() , IrisLive.class);
                intent.putExtra("title" , title.getText().toString());
                startActivity(intent);
            }
        });


        return  view;
    }
}
