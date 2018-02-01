package com.youthlive.youthlive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class GoLive extends AppCompatActivity {

    Button goLive;
    ImageButton close;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live);

        goLive = (Button)findViewById(R.id.golive);
        close = (ImageButton)findViewById(R.id.close);

        title = (EditText)findViewById(R.id.title);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        goLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoLive.this , WowzaStream.class);
                intent.putExtra("title" , title.getText().toString());
                startActivity(intent);

            }
        });

    }
}
