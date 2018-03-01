package com.example.baking.ui.recipes.list;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.example.baking.BakingSchedulers;
import com.example.baking.R;
import com.example.baking.databinding.RecipesListActivityBinding;
import com.example.baking.model.api.Api;
import com.example.baking.model.db.BakingDatabase;
import com.example.baking.ui.BaseActivity;
import com.example.baking.utils.Log;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Completable;

public class RecipiesListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final LifecycleProvider<Lifecycle.Event> lifecycle = AndroidLifecycle.createLifecycleProvider(this);

    private RecipesListActivityBinding mBinding;

    private RecipiesListViewModel mViewModel;

    private RecipiesListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.recipes_list_activity);

        setSupportActionBar(mBinding.toolbar);

        mBinding.refreshLayout.setOnRefreshListener(this);

        mAdapter = new RecipiesListAdapter(null);
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));

        mViewModel = ViewModelProviders.of(this).get(RecipiesListViewModel.class);

        onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getRecipes()
                .compose(lifecycle.bindToLifecycle())
                .subscribeOn(BakingSchedulers.getDatabaseScheduler())
                .observeOn(BakingSchedulers.getUiScheduler())
                .subscribe(mAdapter::setRecipes);
    }

    @Override
    public void onRefresh() {
        mBinding.refreshLayout.setRefreshing(true);
        Api.getDefault().getRecipes()
                .subscribeOn(BakingSchedulers.getNetworkScheduler())
                .observeOn(BakingSchedulers.getUiScheduler())
                .doOnSuccess(recipes -> {
                    Completable
                            .fromAction(() -> {
                                BakingDatabase.getDefault().recipeDao().rewriteRecipies(recipes);
                            })
                            .subscribeOn(BakingSchedulers.getDatabaseScheduler())
                            .observeOn(BakingSchedulers.getUiScheduler())
                            .subscribe(() -> {
                                mBinding.refreshLayout.setRefreshing(false);
                            });
                })
                .doOnError(error -> {
                    Log.e(Log.DEFAULT_TAG, "Error receiving recipes list", error);
                    mBinding.refreshLayout.setRefreshing(false);
                })
                .subscribe();
    }
}
