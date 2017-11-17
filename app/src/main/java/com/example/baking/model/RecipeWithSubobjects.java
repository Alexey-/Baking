package com.example.baking.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeWithSubobjects {

    @Embedded
    private Recipe mRecipe;

    @Relation(parentColumn = "mId", entityColumn = "mRecipeId", entity = Ingredient.class)
    private List<Ingredient> mIngredients;

    @Relation(parentColumn = "mId", entityColumn = "mRecipeId", entity = CookingStep.class)
    private List<CookingStep> mSteps;

    public RecipeWithSubobjects() {

    }

    public RecipeWithSubobjects(Recipe recipe, List<Ingredient> ingredients, List<CookingStep> steps) {
        mRecipe = recipe;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public List<CookingStep> getSteps() {
        return mSteps;
    }

    public void setSteps(List<CookingStep> steps) {
        mSteps = steps;
    }
}
