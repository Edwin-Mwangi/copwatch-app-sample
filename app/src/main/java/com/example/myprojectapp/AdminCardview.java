package com.example.myprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminCardview extends AppCompatActivity {

    private CardView mediaView,videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincardview);

        //what cardview media will do
        mediaView = (CardView) findViewById(R.id.mediaView);
        mediaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Media_retrieval.class));
            }
        });

        //what mapView media will do
        videoView = (CardView) findViewById(R.id.videoView);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Showvideo.class));
            }
        });

    }
}