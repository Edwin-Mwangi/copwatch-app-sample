package com.example.myprojectapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context context;
    private List<Upload> mUploads;

    public ImageAdapter(Context context, List<Upload> uploads) {
        this.context = context;
        this.mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());

        //picasso code exiting...
        Picasso.with(context)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.ic_baseline_image)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_name);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

}
