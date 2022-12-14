package com.example.cookingapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
/**
 * Test class for Recipe
 */
public class RecipeTest {
    private Recipe mockRecipe() {
        return new Recipe();
    }

    /**
     * Test get and set title
     */
    @Test
    public void titleTest(){
        Recipe recipe = mockRecipe();
        recipe.setTitle("Tacos");
        assertEquals("Tacos",recipe.getTitle());
    }

    /**
     * Test get and set servings
     */
    @Test
    public void servingsTest(){
        Recipe recipe = mockRecipe();
        recipe.setServings("5");
        assertEquals("5",recipe.getServings());
    }

    /**
     * Test get and set category
     */
    @Test
    public void categoryTest(){
        Recipe recipe = mockRecipe();
        recipe.setCategory("Dinner");
        assertEquals("Dinner",recipe.getCategory());
    }

    /**
     * Test get and set comment
     */
    @Test
    public void commentsTest(){
        Recipe recipe = mockRecipe();
        recipe.setComments("comment");
        assertEquals("comment", recipe.getComments());
    }

    /**
     * Test get and set prepTime
     */
    @Test
    public void prepTimeTest(){
        Recipe recipe = mockRecipe();
        recipe.setPrepTime("5 hours 10 min");
        assertEquals("5 hours 10 min", recipe.getPrepTime());
    }

    /**
     * Test get and set image
     */
    @Test
    public void imageTest(){
        Recipe recipe = mockRecipe();
        recipe.setImage("image");
        assertEquals("image", recipe.getImage());
    }
    /**
     * Test scale to servings function
     */
    @Test
    public void scaleRecipeIngredientsTest(){
        Double wantedServings = 5.0;
        Recipe recipe = mockRecipe();
        recipe.setServings(String.valueOf(2.0));
        RecipeIngredient ingredient1 = new RecipeIngredient();
        RecipeIngredient ingredient2 = new RecipeIngredient();
        ingredient1.setAmount(String.valueOf(1.0));
        ingredient2.setAmount(String.valueOf(2.5));
        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.setIngredients(ingredients);
        Recipe scaledRecipe = recipe.scale(wantedServings);
        assertEquals(2.5,Double.parseDouble(scaledRecipe.getIngredients().get(0).getAmount()),0.001);
        assertEquals(6.25,Double.parseDouble(scaledRecipe.getIngredients().get(1).getAmount()),0.001);
    }


}
