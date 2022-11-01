package com.example.cookingapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class RecipeTest {
    private Recipe mockRecipe() {
        return new Recipe();
    }

    private Ingredient mockIngredient() {
        return new Ingredient();
    }

    @Test
    public void titleTest(){
        Recipe recipe = mockRecipe();
        recipe.setTitle("Tacos");
        assertEquals("Tacos",recipe.getTitle());
    }

    @Test
    public void servingsTest(){
        Recipe recipe = mockRecipe();
        recipe.setServings("5");
        assertEquals("5",recipe.getServings());
    }

    @Test
    public void categoryTest(){
        Recipe recipe = mockRecipe();
        recipe.setCategory("Dinner");
        assertEquals("Dinner",recipe.getCategory());
    }

    @Test
    public void commentsTest(){
        Recipe recipe = mockRecipe();
        recipe.setComments("comment");
        assertEquals("comment", recipe.getComments());
    }

    @Test
    public void prepTimeTest(){
        Recipe recipe = mockRecipe();
        recipe.setPrepTime("5 hours 10 min");
        assertEquals("5 hours 10 min", recipe.getPrepTime());
    }

    @Test
    public void imageTest(){
        Recipe recipe = mockRecipe();
        recipe.setImage("image");
        assertEquals("image", recipe.getImage());
    }

    @Test
    public void ingredientTest(){
        Recipe recipe = mockRecipe();
        Ingredient ingredient = mockIngredient();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);
        assertEquals(ingredients, recipe.getIngredients());
    }

}
