package com.example.baking.ui.recipes.list;

import android.arch.lifecycle.ViewModel;

import com.example.baking.model.Recipe;
import com.example.baking.model.db.BakingDatabase;

import java.util.List;

import io.reactivex.Flowable;

public class RecipiesListViewModel extends ViewModel {

    private Flowable<List<Recipe>> mRecipes;

    public RecipiesListViewModel() {
        mRecipes = BakingDatabase.getDefault().recipeDao().getRecipes();
    }

    public Flowable<List<Recipe>> getRecipes() {
        return mRecipes;
    }

}
