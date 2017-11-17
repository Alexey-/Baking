package com.example.baking.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.baking.BakingApplication;
import com.example.baking.model.CookingStep;
import com.example.baking.model.Ingredient;
import com.example.baking.model.Recipe;

@Database(entities = {Recipe.class, Ingredient.class, CookingStep.class}, version = 1)
@TypeConverters({MeasureConverter.class})
public abstract class BakingDatabase extends RoomDatabase {

    private static BakingDatabase sDefaultDatabase;

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract CookingStepDao cookingStepDao();

    public static synchronized BakingDatabase getDefault() {
        if (sDefaultDatabase == null) {
            sDefaultDatabase = getDatabase(BakingApplication.getInstance());
        }
        return sDefaultDatabase;
    }

    @VisibleForTesting
    public static synchronized void setDefault(BakingDatabase database) {
        sDefaultDatabase = database;
    }

    @VisibleForTesting
    public static BakingDatabase getDatabase(Context context) {
        return Room
                .databaseBuilder(context, BakingDatabase.class, "baking.db")
                .build();
    }

    @VisibleForTesting
    public static BakingDatabase getInMemoryDatabase(Context context) {
        return Room
                .inMemoryDatabaseBuilder(context, BakingDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

}
