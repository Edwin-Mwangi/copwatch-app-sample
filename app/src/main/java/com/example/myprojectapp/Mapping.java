package com.example.myprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.myprojectapp.Camera_Activity.getPictureName;

public class Mapping extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    String inverse1,inverse2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Upload upload = new Upload();
        DatabaseReference mapRef= FirebaseDatabase.getInstance().getReference();
        mapRef.child("Coordinates").child(getPictureName()).child("Latitude");
        mapRef.child("Coordinates").child(getPictureName()).child("Longitude");

        mapRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Retrieve_Coordinates info = snapshot.getValue(Retrieve_Coordinates.class);
                    //replacing dashes with dots
                    String lats = info.getLatitude();
                    inverse1 = lats.replace("_",".");
                    String longs = info.getLongitude();
                    inverse2 = longs.replace("_",".");//replace coordinates in code with the get method
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng mediaLoc = new LatLng( -0.3956909 /*Double.parseDouble(inverse1)*/, 36.9643497 /*Double.parseDouble(inverse2)*/);//fill here with cooordinate from string
        map.addMarker(new MarkerOptions().position(mediaLoc).title("Media Location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(mediaLoc));

    }
}