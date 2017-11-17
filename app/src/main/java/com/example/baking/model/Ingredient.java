package com.example.baking.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "mId", childColumns = "mRecipeId", onDelete = ForeignKey.CASCADE))
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int mLocalId;
    private int mRecipeId;
    @SerializedName("quantity")
    private double mQuantity;
    @Embedded
    @SerializedName("measure")
    private Measure mMeasure;
    @SerializedName("ingredient")
    private String mIngredient;

    public Ingredient() {

    }

    public int getLocalId() {
        return mLocalId;
    }

    public void setLocalId(int localId) {
        mLocalId = localId;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int recipeId) {
        mRecipeId = recipeId;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        mQuantity = quantity;
    }

    public Measure getMeasure() {
        return mMeasure;
    }

    public void setMeasure(Measure measure) {
        mMeasure = measure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        if (mRecipeId != that.mRecipeId) return false;
        if (Double.compare(that.mQuantity, mQuantity) != 0) return false;
        if (mMeasure != null ? !mMeasure.equals(that.mMeasure) : that.mMeasure != null)
            return false;
        return mIngredient != null ? mIngredient.equals(that.mIngredient) : that.mIngredient == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mRecipeId;
        temp = Double.doubleToLongBits(mQuantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mMeasure != null ? mMeasure.hashCode() : 0);
        result = 31 * result + (mIngredient != null ? mIngredient.hashCode() : 0);
        return result;
    }
}
