package com.example.baking.ui.recipes.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.baking.R;
import com.example.baking.databinding.RecipeViewHolderBinding;
import com.example.baking.model.Recipe;
import com.example.baking.ui.recipes.details.RecipeDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipiesListAdapter extends RecyclerView.Adapter<RecipiesListAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    public RecipiesListAdapter(List<Recipe> recipes) {
        this.setRecipes(recipes);
    }

    public void setRecipes(List<Recipe> recipes) {
        if (recipes == null) {
            mRecipes = Collections.emptyList();
        } else {
            mRecipes = new ArrayList<>(recipes);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_holder, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.setRecipe(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private Recipe mRecipe;

        private RecipeViewHolderBinding mBinding;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mBinding = RecipeViewHolderBinding.bind(itemView);
        }

        public void setRecipe(Recipe recipe) {
            mRecipe = recipe;

            Glide.with(itemView.getContext())
                    .load(mRecipe.getImageUrl())
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(mBinding.image);
            mBinding.title.setText(mRecipe.getName());

            mBinding.card.setOnClickListener((view) -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailsActivity.class);
                intent.putExtra(RecipeDetailsActivity.INTENT_PARAM_RECIPE_ID, mRecipe.getId());
                context.startActivity(intent);
            });
        }

    }

}
