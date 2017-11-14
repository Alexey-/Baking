package com.example.baking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("servings")
    private int mServings;
    @SerializedName("image")
    private String mImageUrl;
    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;
    @SerializedName("steps")
    private List<CookingStep> mSteps;

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

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<CookingStep> getSteps() {
        return mSteps;
    }
}
