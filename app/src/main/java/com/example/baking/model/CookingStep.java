package com.example.baking.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "mId", childColumns = "mRecipeId", onDelete = ForeignKey.CASCADE))
public class CookingStep {

    @PrimaryKey(autoGenerate = true)
    private int mLocalId;
    @SerializedName("id")
    private int mId;
    private int mRecipeId;
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

    public int getLocalId() {
        return mLocalId;
    }

    public void setLocalId(int localId) {
        mLocalId = localId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int recipeId) {
        mRecipeId = recipeId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CookingStep that = (CookingStep) o;

        if (mId != that.mId) return false;
        if (mRecipeId != that.mRecipeId) return false;
        if (mShortDescription != null ? !mShortDescription.equals(that.mShortDescription) : that.mShortDescription != null)
            return false;
        if (mDescription != null ? !mDescription.equals(that.mDescription) : that.mDescription != null)
            return false;
        if (mVideoUrl != null ? !mVideoUrl.equals(that.mVideoUrl) : that.mVideoUrl != null)
            return false;
        return mThumbnailUrl != null ? mThumbnailUrl.equals(that.mThumbnailUrl) : that.mThumbnailUrl == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mRecipeId;
        result = 31 * result + (mShortDescription != null ? mShortDescription.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mVideoUrl != null ? mVideoUrl.hashCode() : 0);
        result = 31 * result + (mThumbnailUrl != null ? mThumbnailUrl.hashCode() : 0);
        return result;
    }
}
