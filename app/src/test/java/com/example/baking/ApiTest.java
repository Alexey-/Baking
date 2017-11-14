package com.example.baking;

import com.example.baking.model.Measure;
import com.example.baking.model.Recipe;
import com.example.baking.model.api.ApiFactory;
import com.example.baking.testutils.MockHttpInterceptor;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApiTest {

    @Test
    public void testRecipesList() throws Exception {
        MockHttpInterceptor interceptor = new MockHttpInterceptor();
        interceptor.addJsonResponse("/topher/2017/May/59121517_baking/baking.json", "fake_response.json");
        List<Recipe> recipes = ApiFactory.getApi(interceptor).getRecipes().blockingGet();
        assertEquals(4, recipes.size());
        assertEquals("Nutella Pie", recipes.get(0).getName());
        Recipe nutellaPieRecipe = recipes.get(0);
        assertEquals("https://example.com/image", nutellaPieRecipe.getImageUrl());
        assertEquals(9, nutellaPieRecipe.getIngredients().size());
        assertEquals(7, nutellaPieRecipe.getSteps().size());
        assertEquals(Measure.Unit.CUP, nutellaPieRecipe.getIngredients().get(0).getMeasure().getUnit());
    }

}