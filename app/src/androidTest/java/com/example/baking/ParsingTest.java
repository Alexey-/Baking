package com.example.baking;

import android.support.test.runner.AndroidJUnit4;

import com.example.baking.model.CookingStep;
import com.example.baking.model.Ingredient;
import com.example.baking.model.Measure;
import com.example.baking.model.Recipe;
import com.example.baking.model.RecipeWithSubobjects;
import com.example.baking.model.api.ApiFactory;
import com.example.baking.utils.TestResponseRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ParsingTest {

    @Rule
    public TestResponseRule mResponseRule = new TestResponseRule(
            "/topher/2017/May/59121517_baking/baking.json", "fake_response.json"
    );

    @Test
    public void testRecipesList() throws Exception {
        List<RecipeWithSubobjects> recipes = ApiFactory.getDefault().getRecipes().blockingGet();
        assertEquals(4, recipes.size());

        Recipe nutellaPieRecipe = recipes.get(0).getRecipe();
        assertEquals("Nutella Pie", nutellaPieRecipe.getName());
        assertEquals("https://example.com/image", nutellaPieRecipe.getImageUrl());

        List<Ingredient> nutellaPieIngredients = recipes.get(0).getIngredients();
        assertEquals(9, nutellaPieIngredients.size());
        assertEquals(Measure.Unit.CUP, nutellaPieIngredients.get(0).getMeasure().getUnit());

        List<CookingStep> nutellaPieSteps = recipes.get(0).getSteps();
        assertEquals(7, nutellaPieSteps.size());
    }

}