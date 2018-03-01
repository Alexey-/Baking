package com.example.baking.ui.recipes.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.baking.R;
import com.example.baking.databinding.CookingStepFragmentBinding;
import com.example.baking.model.CookingStep;
import com.example.baking.ui.BaseFragment;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class CookingStepFragment extends BaseFragment {

    private static final String ARGUMENT_RECIPE_ID = "ARGUMENT_RECIPE_ID";

    public static CookingStepFragment createFragment(int recipeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_RECIPE_ID, recipeId);

        CookingStepFragment fragment = new CookingStepFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private final LifecycleProvider<Lifecycle.Event> lifecycle = AndroidLifecycle.createLifecycleProvider(this);
    private CookingStepFragmentBinding mBinding;
    private RecipeDetailsViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int recipeId = getArguments().getInt(ARGUMENT_RECIPE_ID, 0);
        mViewModel = ViewModelProviders.of(getActivity(), new RecipeDetailsViewModel.RecipeDetailsViewModelFactory(recipeId)).get(RecipeDetailsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = CookingStepFragmentBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getCookingStep()
                .compose(lifecycle.bindToLifecycle())
                .subscribe(this::onCookingStepSelected);
    }

    public void onCookingStepSelected(CookingStep cookingStep) {
        if (cookingStep == null) {
            mBinding.content.setVisibility(View.GONE);
        } else {
            mBinding.content.setVisibility(View.VISIBLE);
            mBinding.shortDescription.setText(cookingStep.getShortDescription());
            mBinding.description.setText(cookingStep.getDescription());

            if (!TextUtils.isEmpty(cookingStep.getThumbnailUrl())) {
                mBinding.image.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(cookingStep.getThumbnailUrl())
                        .fitCenter()
                        .placeholder(R.drawable.placeholder)
                        .into(mBinding.image);
            } else {
                mBinding.image.setVisibility(View.GONE);
            }
        }
    }

}
