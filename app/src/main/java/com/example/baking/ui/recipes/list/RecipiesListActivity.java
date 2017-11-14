package com.example.baking.ui.recipes.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.example.baking.R;
import com.example.baking.databinding.RecipesListActivityBinding;
import com.example.baking.model.api.Api;
import com.example.baking.ui.BaseActivity;
import com.example.baking.utils.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecipiesListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecipesListActivityBinding mBinding;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mBinding.refreshLayout.setRefreshing(true);
        Api.getDefault().getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess((recipes) -> {
                    mAdapter.setRecipes(recipes);
                })
                .doOnError((error) -> {
                    Log.e(Log.DEFAULT_TAG, "Error receiving recipes list", error);
                })
                .doFinally(() -> {
                    mBinding.refreshLayout.setRefreshing(false);
                })
                .subscribe();
    }
}
