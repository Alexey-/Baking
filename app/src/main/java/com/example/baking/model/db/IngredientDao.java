package com.example.baking.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.model.Ingredient;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM Ingredient WHERE mRecipeId = :recipeId")
    Flowable<List<Ingredient>> getIngridients(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(List<Ingredient> ingredients);

    @Query("DELETE FROM Ingredient")
    void deleteIngredients();

}
