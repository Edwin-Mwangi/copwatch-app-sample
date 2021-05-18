package com.example.myprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myprojectapp.Camera_Activity.getPictureName;


public class Coordinate_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong;
    private TextView textAddress;
    private Button getLocation;
    private ProgressDialog progressDialog;
    private ResultReceiver resultReceiver;
    //firebase
    private  static final String TAG = "Upload Coordinates";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_);

        resultReceiver = new AddressResultReceiver(new Handler());


        textLatLong = (TextView) findViewById(R.id.textLatlong);
        textAddress = findViewById(R.id.textAddress);
        getLocation = findViewById(R.id.getLocation);


        mauth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        progressDialog = new ProgressDialog(Coordinate_Activity.this);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(!countyName.equals("")){
                //    uploadCoordinates();
                // }
                //location ifs to check permissions
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(
                            Coordinate_Activity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );

                } else{
                    getCurrentLocation();
                }
                //uploadCoordinates();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void getCurrentLocation(){

        Log.d(TAG, "OnClick: Uploading Location.");
        progressDialog.setMessage("Getting Location...");
        progressDialog.show();


        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //
        try {
            LocationServices.getFusedLocationProviderClient(Coordinate_Activity.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback(){

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(Coordinate_Activity.this)
                                    .removeLocationUpdates(this);
                            if(locationResult != null && locationResult.getLocations().size() > 0){
                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                double latitude =
                                        locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                double longitude =
                                        locationResult.getLocations().get(latestLocationIndex).getLongitude();

                                textLatLong.setText(

                                        String.format(
                                                "Latitude: %s\nLongitude: %s",latitude,longitude
                                        )
                                );

                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                fetchAddressFromLatLong(location);
                                //Firebase cannot store dots so...for latitude
                                String lats = String.valueOf(latitude);
                                String reverse1 = lats.replace(".","_");
                                //for longitude
                                String longs = String.valueOf(longitude);
                                String reverse2 = longs.replace(".","_");

                                //uploading result to firebase db...tutorial shows only display in textviwe in android 9:44
                               FirebaseUser user = mauth.getCurrentUser();
                               String userId = user.getUid();
                               myRef.child("Coordinates").child(String.valueOf(getPictureName())).child("Latitude").child(reverse1).setValue("true");
                               myRef.child("Coordinates").child(String.valueOf(getPictureName())).child("Longitude").child(reverse2).setValue("true");

                            }else {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(Coordinate_Activity.this, "Location sent", Toast.LENGTH_SHORT).show();

                        }
                    }, Looper.getMainLooper());

        }catch(SecurityException securityException){
            Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT){
                textAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else {
                Toast.makeText(Coordinate_Activity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}