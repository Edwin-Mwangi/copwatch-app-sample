package com.example.myprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Camera_Activity extends AppCompatActivity {

    public static final String TAG = "Capture Picture";
    static final int REQUEST_PICTURE_CAPTURE = 1;
    static final int VIDEO_CAPTURE = 101;
    //static final int REQUEST_VIDEO_CAPTURE = 1;
    public ImageView myImage;
    //public VideoView videoView;
    public String cloudFilePath;
    public Button captureBtn, saveGallery, saveCloud, captureVideo;
    public String mediaFilePath;
    public static String mediaName;
    public FirebaseStorage firebaseStorage;
    private ProgressDialog camprogressDialog;
    //uploading images
    //public StorageReference mStorage;
    public DatabaseReference mDatabaseReference;
   // public StorageReference uploadRef;
    public FirebaseAuth auth;
    public Uri picUri;
    public String uploadId;

    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        myImage = (ImageView) findViewById(R.id.myImage);
//        videoView =(VideoView) findViewById(R.id.videoView);

        captureBtn = (Button) findViewById(R.id.captureButton);
        saveGallery = (Button) findViewById(R.id.save_local);
        saveCloud = (Button) findViewById(R.id.save_cloud);
        firebaseStorage = FirebaseStorage.getInstance();
        captureVideo = (Button) findViewById(R.id.captureVideo);

        //mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("files");
        //uploadRef = FirebaseStorage.getInstance().getReference().child("files");

        camprogressDialog = new ProgressDialog(Camera_Activity.this);
        auth = FirebaseAuth.getInstance();

        //checkFilePermissions;

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();

            }
        });

        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });

        saveGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToGallery();

            }
        });

        saveCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCloudStorage();
                startActivity(new Intent(getApplicationContext(), Coordinate_Activity.class));
            }

            //
            //StorageReference uploadRef = storageRef.child(cloudFilePath);//cloud file path is path of file in firebase
            //if successful
            //camprogressDialog.dismiss();//combine this todo
        });
    }

    ;

    //taking video\
    private void takeVideo() {
        Intent videoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoCapture, VIDEO_CAPTURE);

        videoCapture.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (videoCapture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoCapture, VIDEO_CAPTURE);

            File videoFile = null;
            try {
                videoFile = getVideoFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Video file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;

            }
            if (videoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        //to be changed
                        "com.example.myprojectapp.fileprovider",//com.example.myprojectapp
                        videoFile);
                videoCapture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(videoCapture, VIDEO_CAPTURE);
            }

        }


    }




    //unique filename for video
    private File getVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mediaName = "COPWATCH_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(mediaName, ".mp4",storageDir);
        mediaFilePath = video.getAbsolutePath();
        return video;
    }




    private void takePicture() {
        //multiple intents to be edited
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capture.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (capture.resolveActivity(getPackageManager()) != null) {
            /*startActivityForResult(capture,  REQUEST_PICTURE_CAPTURE)*/;

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;

            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        //to be changed
                        "com.example.myprojectapp.fileprovider",//com.example.myprojectapp
                        pictureFile);
                capture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(capture, REQUEST_PICTURE_CAPTURE);
            }

        }


        }

    //creating a unique filename for picture
    public File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mediaName = "COPWATCH_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(mediaName, ".jpg", storageDir) ;
        mediaFilePath = image.getAbsolutePath();
        return image;

    }
    //used by both take picture and take video
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        /*if(JButton.getModel().isArmed()){
            //your code here.
            //your code will be only executed if JButton is clicked.

        }*/
        super.onActivityResult(requestCode,  resultCode,  data);
        if ((requestCode == REQUEST_PICTURE_CAPTURE ) && resultCode == RESULT_OK){
            File imgFile = new File(mediaFilePath);
            if (imgFile.exists())
                myImage.setImageURI(Uri.fromFile(imgFile));
        }else{
            Toast.makeText(this, "Failed to capture Image",
                    Toast.LENGTH_LONG).show();
        }



       /* super.onActivityResult(requestCode, resultCode,  data);
        videoView.setBackgroundColor(Color.WHITE);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setBackgroundColor(Color.TRANSPARENT);
            }
        });*/

        if ((requestCode == VIDEO_CAPTURE) && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            File imgFile = new File(mediaFilePath);
            if (imgFile.exists()){
                myImage.setImageURI(videoUri.fromFile(imgFile));//changed from videoView
                Toast.makeText(this, "Video saved to:\n" +
                        mediaFilePath, Toast.LENGTH_LONG).show();
            }



            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            }

    }



    //save captured picture in gallery
    public void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mediaFilePath);
        picUri = Uri.fromFile(f);
        galleryIntent.setData(picUri);
        this.sendBroadcast(galleryIntent);
        Toast.makeText(Camera_Activity.this,
                "Media has been saved to gallery",
                Toast.LENGTH_SHORT).show();
    }
    //save picture to cloud changed code



    //save picture to cloud storage....this code to be changed todo(ditch identifier to use authentication...see firebase capture image pdf)
    public void addToCloudStorage() {

        Log.d(TAG, "OnClick: Uploading Media.");
        camprogressDialog.setMessage("Uploading file...");
        camprogressDialog.show();

        //get the signed in user
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        File f = new File(mediaFilePath);
        Uri picUri = Uri.fromFile(f);
        //saving name in firebase

       // StorageReference fileReference = uploadRef.child(cloudFilePath);

        String cloudFilePath = ("files/" + mediaName );

        //array due to .jpg and .mp4...this has been ditched
        //cloudFilePath = ("files/users" + mediaName + );
        //cloudFilePath[1] = ("files/users" + mediaName );//"files/users" + userId + "/" + pictureName + ".jpg"

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadRef = storageRef.child(cloudFilePath);
        //



        uploadTask = uploadRef.putFile(picUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                Toast.makeText(Camera_Activity.this,
                        "Image has been uploaded to cloud storage",
                        Toast.LENGTH_SHORT).show();
                camprogressDialog.dismiss();
                /*Upload upload = new Upload(mediaName, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                uploadId = mDatabaseReference.push().getKey();
                mDatabaseReference.child(uploadId).setValue(upload);*/
               // mDatabaseReference.setValue(upload);
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                
                Upload upload = new Upload(mediaName,downloadUrl.toString());
                String uploadId = mDatabaseReference.push().getKey();
                mDatabaseReference.child(uploadId).setValue(upload);

                //shows success
                /*Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                upload = new Upload(downloadUrl.toString());

                String uploadId = mDatabaseReference.push().getKey();
                mDatabaseReference.child(uploadId).setValue(upload);*/

            }
        })
                .addOnFailureListener(new OnFailureListener(){
            public void onFailure(@NonNull Exception exception){
                Log.e(TAG,"Failed to upload picture to cloud storage");
            }
        });
    }

    //getting file extension
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(picUri));

    }

    public static String getPictureName(){
        return mediaName;
    }
    public String getCloudFilePath(){

        return cloudFilePath;
    }

}
