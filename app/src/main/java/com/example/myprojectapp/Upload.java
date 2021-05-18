package com.example.myprojectapp;



public class Upload {
    private   String mImageUrl;
    private   String mName;
    private String thumbnail;

    public Upload() {
        //empty constructor
    }

    public Upload(String name, String imageUrl){
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName(){

        return mName;
    }
    public void setName(String name) {

        mName = name;
    }

    public String getImageUrl() {

        return mImageUrl;
    }

    public  void setImageUrl(String imageUrl){

        mImageUrl = imageUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
