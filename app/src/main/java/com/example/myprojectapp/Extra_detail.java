package com.example.myprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Extra_detail extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong;
    private ProgressDialog progressDialog;
    //firebase
    private  static final String TAG = "Upload Coordinates";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;



    EditText county,countyArea;
    Button countyDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_detail);

        county = findViewById(R.id.countyName);
        countyArea = findViewById(R.id.countyareaName);
        textLatLong = findViewById(R.id.textLatlong);

        mauth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        progressDialog = new ProgressDialog(Extra_detail.this);

        countyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String countyName = county.getText().toString().trim();
                String constituency = countyArea.getText().toString().trim();

                Log.d(TAG, "OnClick: Uploading Location.");
                progressDialog.setMessage("Uploading data...");
                progressDialog.show();

                //uploading result to firebase db...tutorial shows only display in textview in android 9:44
                FirebaseUser user = mauth.getCurrentUser();
                String userId = user.getUid();
                myRef.child(userId).child("Unspecified_location").child(String.valueOf(countyName)).setValue("true");
                myRef.child(userId).child("Unspecified_location").child(String.valueOf(constituency)).setValue("true");

                progressDialog.dismiss();


            }
        });
    }

}