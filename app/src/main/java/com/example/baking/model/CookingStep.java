package com.example.baking.model;

import com.google.gson.annotations.SerializedName;

public class CookingStep {

    @SerializedName("id")
    private int mId;
    @SerializedName("shortDescription")
    private String mShortDescription;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("videoUrl")
    private String mVideoUrl;
    @SerializedName("thumbnailUrl")
    private String mThumbnailUrl;

    public CookingStep() {

    }

    public int getId() {
        return mId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }
}
