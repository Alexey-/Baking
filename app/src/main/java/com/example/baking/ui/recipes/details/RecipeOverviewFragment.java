package com.example.baking.ui.recipes.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking.BakingSchedulers;
import com.example.baking.databinding.RecipeOverviewFragmentBinding;
import com.example.baking.ui.BaseFragment;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class RecipeOverviewFragment extends BaseFragment {

    private static final String ARGUMENT_RECIPE_ID = "ARGUMENT_RECIPE_ID";

    public static RecipeOverviewFragment createFragment(int recipeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_RECIPE_ID, recipeId);

        RecipeOverviewFragment fragment = new RecipeOverviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private final LifecycleProvider<Lifecycle.Event> lifecycle = AndroidLifecycle.createLifecycleProvider(this);
    private RecipeOverviewFragmentBinding mBinding;
    private RecipeDetailsViewModel mViewModel;
    private RecipeOverviewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int recipeId = getArguments().getInt(ARGUMENT_RECIPE_ID, 0);
        mViewModel = ViewModelProviders.of(getActivity(), new RecipeDetailsViewModel.RecipeDetailsViewModelFactory(recipeId)).get(RecipeDetailsViewModel.class);
        mAdapter = new RecipeOverviewAdapter(null, mViewModel::setCookingStep);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = RecipeOverviewFragmentBinding.inflate(getLayoutInflater(), container, false);

        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getRecipe()
                .compose(lifecycle.bindToLifecycle())
                .subscribeOn(BakingSchedulers.getDatabaseScheduler())
                .observeOn(BakingSchedulers.getUiScheduler())
                .subscribe(mAdapter::setRecipe);
    }

}
