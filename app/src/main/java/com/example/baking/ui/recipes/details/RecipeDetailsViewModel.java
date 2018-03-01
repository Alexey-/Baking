package com.example.baking.ui.recipes.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.baking.model.CookingStep;
import com.example.baking.model.RecipeWithSubobjects;
import com.example.baking.model.db.BakingDatabase;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class RecipeDetailsViewModel extends ViewModel {

    private Flowable<RecipeWithSubobjects> mRecipe;
    private BehaviorSubject<CookingStep> mCookingStep;

    public RecipeDetailsViewModel(int recipeId) {
        mRecipe = BakingDatabase.getDefault().recipeDao().getRecipeWithSubobjects(recipeId);
        mCookingStep = BehaviorSubject.create();
    }

    public Flowable<RecipeWithSubobjects> getRecipe() {
        return mRecipe;
    }

    public void setCookingStep(CookingStep cookingStep) {
        mCookingStep.onNext(cookingStep);
    }

    public Observable<CookingStep> getCookingStep() {
        return mCookingStep;
    }

    public  CookingStep getCookingStepValue() {
        return mCookingStep.getValue();
    }

    public static class RecipeDetailsViewModelFactory implements ViewModelProvider.Factory {

        private final int recipeId;

        public RecipeDetailsViewModelFactory(int recipeId) {
            this.recipeId = recipeId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RecipeDetailsViewModel(recipeId);
        }
    }

}
