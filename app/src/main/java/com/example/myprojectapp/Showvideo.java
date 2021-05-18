   package com.example.myprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

  public class Showvideo extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showvideo);

        databaseReference = FirebaseDatabase.getInstance().getReference("files");

        recyclerView = findViewById(R.id.recyclerview_ShowVideo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();


    }

      @Override
      protected void onStart() {
          super.onStart();

          FirebaseRecyclerOptions<Upload> options =
                  new FirebaseRecyclerOptions.Builder<Upload>()
                  .setQuery(databaseReference,Upload.class)
                  .build();

          FirebaseRecyclerAdapter<Upload,ViewHolder> firebaseRecyclerAdapter =
                  new FirebaseRecyclerAdapter<Upload, ViewHolder>(options) {

                     @Override
                     protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Upload model) {
                         holder.setExoplayer(getApplication(),model.getName(),model.getImageUrl());

                     }

                     @NonNull
                     @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.media_item,parent,false);

                        return new ViewHolder(view);

                    }
                  };

          firebaseRecyclerAdapter.startListening();
          recyclerView.setAdapter(firebaseRecyclerAdapter);


      }
  }