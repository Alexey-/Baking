package com.example.baking.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.example.baking.model.Measure;
import com.example.baking.model.RecipeWithSubobjects;
import com.example.baking.model.api.MeasureDeserializer;
import com.example.baking.model.api.RecipeWithSubobjectsDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class FakeData {

    private static Context getContext() {
        return InstrumentationRegistry.getContext();
    }

    public static List<RecipeWithSubobjects> readRecipesFromAssets(String filesName) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Measure.class, new MeasureDeserializer());
        gsonBuilder.registerTypeAdapter(RecipeWithSubobjects.class, new RecipeWithSubobjectsDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(readJsonStringFromAssets(filesName), new TypeToken<List<RecipeWithSubobjects>>(){}.getType());
    }

    public static String readJsonStringFromAssets(String fileName) {
        try {
            return InputStreamUtils.toString(getContext().getAssets().open(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
