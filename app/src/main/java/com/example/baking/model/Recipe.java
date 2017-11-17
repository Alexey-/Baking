package com.example.baking.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Recipe {

    @PrimaryKey
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("servings")
    private int mServings;
    @SerializedName("image")
    private String mImageUrl;

    public Recipe() {

    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getServings() {
        return mServings;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setServings(int servings) {
        mServings = servings;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (mId != recipe.mId) return false;
        if (mServings != recipe.mServings) return false;
        if (mName != null ? !mName.equals(recipe.mName) : recipe.mName != null) return false;
        return mImageUrl != null ? mImageUrl.equals(recipe.mImageUrl) : recipe.mImageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + mServings;
        result = 31 * result + (mImageUrl != null ? mImageUrl.hashCode() : 0);
        return result;
    }
}
