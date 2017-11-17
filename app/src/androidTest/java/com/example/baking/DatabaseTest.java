package com.example.baking;

import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;
import com.example.baking.model.CookingStep;
import com.example.baking.model.Ingredient;
import com.example.baking.model.Recipe;
import com.example.baking.model.RecipeWithSubobjects;
import com.example.baking.model.db.BakingDatabase;
import com.example.baking.utils.FakeData;
import com.example.baking.utils.InMemoryDatabaseRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    @Rule
    public InMemoryDatabaseRule mInMemoryDatabaseRule = new InMemoryDatabaseRule();

    private void assertContent(List<RecipeWithSubobjects> expectedContent) {
        BakingDatabase db = BakingDatabase.getDefault();

        List<Recipe> expectedRecipes = Stream.of(expectedContent).map(RecipeWithSubobjects::getRecipe).toList();
        List<Recipe> recipesFromDb = db.recipeDao().getRecipes().blockingFirst();
        assertEquals(expectedRecipes, recipesFromDb);

        for (RecipeWithSubobjects recipeWithSubobjects : expectedContent) {
            assertEquals(recipeWithSubobjects.getIngredients(), db.ingredientDao().getIngridients(recipeWithSubobjects.getRecipe().getId()).blockingFirst());
            assertEquals(recipeWithSubobjects.getSteps(), db.cookingStepDao().getCookingSteps(recipeWithSubobjects.getRecipe().getId()).blockingFirst());
        }
    }

    @Test
    public void testSimpleInsert() throws Exception {
        BakingDatabase db = BakingDatabase.getDefault();

        List<RecipeWithSubobjects> fakeRecipes = FakeData.readRecipesFromAssets("fake_response.json");
        RecipeWithSubobjects nutellaPieWithSubobjects = fakeRecipes.get(0);
        Recipe nutellaPieRecipe = nutellaPieWithSubobjects.getRecipe();

        try {
            db.beginTransaction();
            db.recipeDao().insertRecipes(Arrays.asList(nutellaPieRecipe));
            db.ingredientDao().insertIngredients(nutellaPieWithSubobjects.getIngredients());
            db.cookingStepDao().insertCookingSteps(nutellaPieWithSubobjects.getSteps());
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        List<Recipe> fakeRecipesFromDb = db.recipeDao().getRecipes().blockingFirst();
        assertEquals(1, fakeRecipesFromDb.size());
        assertEquals(nutellaPieRecipe, fakeRecipesFromDb.get(0));

        List<Ingredient> fakeIngredientsFromDb = db.ingredientDao().getIngridients(nutellaPieRecipe.getId()).blockingFirst();
        assertEquals(nutellaPieWithSubobjects.getIngredients(), fakeIngredientsFromDb);

        List<CookingStep> fakeCookingStepsFromDb = db.cookingStepDao().getCookingSteps(nutellaPieRecipe.getId()).blockingFirst();
        assertEquals(nutellaPieWithSubobjects.getSteps(), fakeCookingStepsFromDb);
    }

    @Test
    public void testUpdateEvent() throws Exception {
        BakingDatabase db = BakingDatabase.getDefault();

        CountDownLatch completeLatch = new CountDownLatch(1);

        List<RecipeWithSubobjects> fakeRecipes = FakeData.readRecipesFromAssets("fake_response.json");
        RecipeWithSubobjects nutellaPieWithSubobjects = fakeRecipes.get(0);
        Recipe nutellaPieRecipe = nutellaPieWithSubobjects.getRecipe();

        AtomicInteger pullIndex = new AtomicInteger(0);
        db.recipeDao().getRecipes()
                .subscribeOn(Schedulers.newThread())
                .subscribe(recipes -> {
                    switch (pullIndex.getAndIncrement()) {
                        case 0:
                            assertEquals(0, recipes.size());
                            break;
                        case 1:
                            assertEquals(1, recipes.size());
                            assertEquals(nutellaPieRecipe, recipes.get(0));
                            break;
                        case 2:
                            assertEquals(0, recipes.size());
                            completeLatch.countDown();
                            break;
                    }
                });

        Thread.sleep(100);
        db.recipeDao().insertRecipes(Arrays.asList(nutellaPieRecipe));
        Thread.sleep(100);
        db.recipeDao().deleteRecipes();

        assertTrue(completeLatch.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void testRewrite() {
        BakingDatabase db = BakingDatabase.getDefault();

        List<RecipeWithSubobjects> fakeRecipes1 = FakeData.readRecipesFromAssets("fake_response.json");
        List<RecipeWithSubobjects> fakeRecipes2 = FakeData.readRecipesFromAssets("fake_response_2.json");

        db.recipeDao().rewriteRecipies(fakeRecipes1);
        assertContent(fakeRecipes1);
        db.recipeDao().rewriteRecipies(fakeRecipes2);
        assertContent(fakeRecipes2);
    }

}
