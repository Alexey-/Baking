package com.example.baking.model.api;

import com.example.baking.model.RecipeWithSubobjects;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface Api {

    static Api getDefault() {
        return ApiFactory.getDefault();
    }

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Single<List<RecipeWithSubobjects>> getRecipes();

}
