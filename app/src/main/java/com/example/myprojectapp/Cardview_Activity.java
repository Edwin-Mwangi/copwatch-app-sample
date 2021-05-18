package com.example.myprojectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class Cardview_Activity extends AppCompatActivity {

    private CardView cam,audioRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        //what cardview camera will do
        cam = (CardView) findViewById(R.id.cam);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Camera_Activity.class));
            }
        });

        //what cardview audio will do
        audioRec = (CardView) findViewById(R.id.audio_rec);
        audioRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Audio_Activity.class));
            }
        });


        /*CardView cam = (CardView) findViewById(R.id.cam);
        //request for camera runtime permission
        if (ContextCompat.checkSelfPermission(cardview.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(cardview.this, new String[]{
                    Manifest.permission.CAMERA

            },100);
        }

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caption = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(caption,100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //code to do...setting image to imageview...check video

        }*/
    }
}