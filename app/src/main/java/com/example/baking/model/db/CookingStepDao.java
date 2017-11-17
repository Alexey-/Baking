package com.example.baking.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.model.CookingStep;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CookingStepDao {

    @Query("SELECT * FROM CookingStep WHERE mRecipeId = :recipeId")
    Flowable<List<CookingStep>> getCookingSteps(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCookingSteps(List<CookingStep> steps);

    @Query("DELETE FROM CookingStep")
    void deleteCookingSteps();

}
