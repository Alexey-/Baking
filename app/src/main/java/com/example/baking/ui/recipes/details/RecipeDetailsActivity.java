package com.example.baking.ui.recipes.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.example.baking.BakingSchedulers;
import com.example.baking.R;
import com.example.baking.databinding.RecipeDetailsActivityBinding;
import com.example.baking.model.CookingStep;
import com.example.baking.model.RecipeWithSubobjects;
import com.example.baking.ui.BaseActivity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class RecipeDetailsActivity extends BaseActivity {

    public static final String INTENT_PARAM_RECIPE_ID = "INTENT_PARAM_RECIPE_ID";

    private final LifecycleProvider<Lifecycle.Event> lifecycle = AndroidLifecycle.createLifecycleProvider(this);
    private RecipeDetailsActivityBinding mBinding;
    private RecipeDetailsViewModel mViewModel;
    private int mRecipeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.recipe_details_activity);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecipeId = getIntent().getIntExtra(INTENT_PARAM_RECIPE_ID, 0);
        mViewModel = ViewModelProviders.of(this, new RecipeDetailsViewModel.RecipeDetailsViewModelFactory(mRecipeId)).get(RecipeDetailsViewModel.class);

        if (savedInstanceState == null) {
            if (isTablet()) {
                RecipeOverviewFragment recipeOverview = RecipeOverviewFragment.createFragment(mRecipeId);
                CookingStepFragment cookingStep = CookingStepFragment.createFragment(mRecipeId);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.left_pane_container, recipeOverview);
                transaction.replace(R.id.right_pane_container, cookingStep);
                transaction.commit();
            } else {
                RecipeOverviewFragment recipeOverview = RecipeOverviewFragment.createFragment(mRecipeId);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fullscreen_container, recipeOverview);
                transaction.commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getRecipe()
                .compose(lifecycle.bindToLifecycle())
                .subscribeOn(BakingSchedulers.getDatabaseScheduler())
                .observeOn(BakingSchedulers.getUiScheduler())
                .subscribe(this::onRecipeUpdated);
        mViewModel.getCookingStep()
                .compose(lifecycle.bindToLifecycle())
                .subscribe(this::onCookingStepSelected);
    }

    public void onRecipeUpdated(RecipeWithSubobjects recipe) {
        setTitle(recipe.getRecipe().getName());

        if (isTablet() && mViewModel.getCookingStepValue() == null && recipe.getSteps().size() > 0) {
            mViewModel.setCookingStep(recipe.getSteps().get(0));
        }
    }

    public void onCookingStepSelected(CookingStep step) {
        if (!isTablet()) {
            CookingStepFragment fragment = CookingStepFragment.createFragment(mRecipeId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.replace(R.id.fullscreen_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
