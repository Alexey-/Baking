package com.example.baking.ui.recipes.details;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baking.R;
import com.example.baking.databinding.CookingStepViewHolderBinding;
import com.example.baking.databinding.RecipeDescriptionViewHolderBinding;
import com.example.baking.model.CookingStep;
import com.example.baking.model.Ingredient;
import com.example.baking.model.RecipeWithSubobjects;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeOverviewAdapter extends RecyclerView.Adapter<RecipeOverviewAdapter.RecipeOverviewViewHolder> {

    public interface OnCookingStepSelectedListener {
        public void onCookingStepSelected(CookingStep cookingStep);
    }

    private RecipeWithSubobjects mRecipe;
    private List<Object> mItems;
    private OnCookingStepSelectedListener mOnCookingStepSelectedListener;

    public RecipeOverviewAdapter(RecipeWithSubobjects recipe, OnCookingStepSelectedListener cookingStepSelectedListener) {
        this.setRecipe(recipe);
        mOnCookingStepSelectedListener = cookingStepSelectedListener;
    }

    public void setRecipe(RecipeWithSubobjects recipe) {
        mRecipe = recipe;
        if (mRecipe == null) {
            mItems = Collections.emptyList();
        } else {
            mItems = new ArrayList<>();
            mItems.add(mRecipe);
            mItems.addAll(mRecipe.getSteps());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItems.get(position);
        if (item instanceof RecipeWithSubobjects) {
            return R.id.cell_type_recipe_description;
        } else if (item instanceof CookingStep) {
            return R.id.cell_type_cooking_step;
        }
        throw new RuntimeException("Unknown item type: " + item.getClass().getSimpleName());
    }

    @Override
    public RecipeOverviewAdapter.RecipeOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.id.cell_type_recipe_description: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_description_view_holder, parent, false);
                return new RecipeOverviewAdapter.RecipeDescriptionViewHolder(itemView);
            }
            case R.id.cell_type_cooking_step: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooking_step_view_holder, parent, false);
                return new RecipeOverviewAdapter.CookingStepViewHolder(itemView);
            }
        }
        throw new RuntimeException("Unhandled view holder type:" + viewType);
    }

    @Override
    public void onBindViewHolder(RecipeOverviewAdapter.RecipeOverviewViewHolder holder, int position) {
        holder.setItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public abstract class RecipeOverviewViewHolder extends RecyclerView.ViewHolder {

        public RecipeOverviewViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setItem(Object item);

    }

    public class RecipeDescriptionViewHolder extends RecipeOverviewViewHolder {

        private DecimalFormat mQuantityFormat = new DecimalFormat("#.##");

        private RecipeWithSubobjects mRecipe;

        private RecipeDescriptionViewHolderBinding mBinding;

        public RecipeDescriptionViewHolder(View itemView) {
            super(itemView);
            mBinding = RecipeDescriptionViewHolderBinding.bind(itemView);
        }

        @Override
        public void setItem(Object item) {
            setRecipe((RecipeWithSubobjects) item);
        }

        public void setRecipe(RecipeWithSubobjects recipe) {
            mRecipe = recipe;

            Glide.with(itemView.getContext())
                    .load(mRecipe.getRecipe().getImageUrl())
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(mBinding.image);
            mBinding.title.setText(mRecipe.getRecipe().getName());
            mBinding.servings.setText(Integer.toString(mRecipe.getRecipe().getServings()));

            mBinding.ingredientsContainer.removeAllViews();
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                Resources res = itemView.getContext().getResources();
                String ingredientDescription = res.getString(R.string.recipe_description_ingredient_format,
                        mQuantityFormat.format(ingredient.getQuantity()),
                        ingredient.getMeasure().getText(itemView.getContext(), ingredient.getQuantity()),
                        ingredient.getIngredient());

                TextView ingredientView = (TextView) LayoutInflater.from(itemView.getContext()).inflate(R.layout.ingredient_view, mBinding.ingredientsContainer, false);
                ingredientView.setText(ingredientDescription);
                mBinding.ingredientsContainer.addView(ingredientView);
            }
        }
    }

    public class CookingStepViewHolder extends RecipeOverviewViewHolder {

        private CookingStep mCookingStep;

        private CookingStepViewHolderBinding mBinding;

        public CookingStepViewHolder(View itemView) {
            super(itemView);
            mBinding = CookingStepViewHolderBinding.bind(itemView);
        }

        @Override
        public void setItem(Object item) {
            this.setCookingStep((CookingStep) item);
        }

        public void setCookingStep(CookingStep cookingStep) {
            mCookingStep = cookingStep;

            mBinding.shortDescription.setText(mCookingStep.getShortDescription());
            mBinding.description.setText(mCookingStep.getDescription());

            mBinding.card.setOnClickListener((view) -> {
                mOnCookingStepSelectedListener.onCookingStepSelected(mCookingStep);
            });
        }
    }

}
