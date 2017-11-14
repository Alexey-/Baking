package com.example.baking.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    private double mQuantity;
    @SerializedName("measure")
    private Measure mMeasure;
    @SerializedName("ingredient")
    private String mIngredient;

    public Ingredient() {

    }

    public double getQuantity() {
        return mQuantity;
    }

    public Measure getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }
}
