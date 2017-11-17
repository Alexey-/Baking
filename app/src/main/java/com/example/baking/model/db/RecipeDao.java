package com.example.baking.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.model.CookingStep;
import com.example.baking.model.Ingredient;
import com.example.baking.model.Recipe;
import com.example.baking.model.RecipeWithSubobjects;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    Flowable<List<Recipe>> getRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipes);

    @Query("DELETE FROM Recipe")
    void deleteRecipes();

    default void rewriteRecipies(List<RecipeWithSubobjects> newRecipiesWithSubobjects) {
        BakingDatabase db = BakingDatabase.getDefault();

        List<Recipe> newRecipes = new ArrayList<>();
        List<Ingredient> newIngredients = new ArrayList<>();
        List<CookingStep> newCookingSteps = new ArrayList<>();

        for (RecipeWithSubobjects recipeWithSubobjects : newRecipiesWithSubobjects) {
            newRecipes.add(recipeWithSubobjects.getRecipe());
            newIngredients.addAll(recipeWithSubobjects.getIngredients());
            newCookingSteps.addAll(recipeWithSubobjects.getSteps());
        }

        try {
            db.beginTransaction();

            deleteRecipes();
            insertRecipes(newRecipes);
            db.ingredientDao().insertIngredients(newIngredients);
            db.cookingStepDao().insertCookingSteps(newCookingSteps);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
