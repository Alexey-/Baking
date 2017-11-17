package com.example.baking.model.api;

import com.example.baking.model.CookingStep;
import com.example.baking.model.Ingredient;
import com.example.baking.model.Recipe;
import com.example.baking.model.RecipeWithSubobjects;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RecipeWithSubobjectsDeserializer implements JsonDeserializer<RecipeWithSubobjects> {
    @Override
    public RecipeWithSubobjects deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Recipe recipe = context.deserialize(json, Recipe.class);
        List<Ingredient> ingredients = context.deserialize(json.getAsJsonObject().getAsJsonArray("ingredients"), new TypeToken<List<Ingredient>>(){}.getType());
        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipeId(recipe.getId());
        }
        List<CookingStep> steps = context.deserialize(json.getAsJsonObject().getAsJsonArray("steps"), new TypeToken<List<CookingStep>>(){}.getType());
        for (CookingStep step : steps) {
            step.setRecipeId(recipe.getId());
        }
        return new RecipeWithSubobjects(recipe, ingredients, steps);
    }
}
