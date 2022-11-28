package com.example.cookingapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Test class for Meal Plan
 */
public class MealPlanTest {
    private MealPlan mockMealPlan(){return new MealPlan();}
    private Recipe mockRecipe() {
        return new Recipe();
    }
    private Ingredient mockIngredient() {
        return new Ingredient();
    }

    /**
     * Test get and set date
     */
    @Test
    public void DateTest() {
        LocalDate date = LocalDate.now();
        MealPlan mealPlan = mockMealPlan();
        mealPlan.setDate(date.toString());
        assertEquals(date.toString(), mealPlan.getDate());
    }

    /**
     * Test get and set recipes
     */
    @Test
    public void recipeTests() {
        MealPlan mealPlan = mockMealPlan();
        Recipe recipe = mockRecipe();
        mealPlan.setBreakfastRecipe(recipe);
        mealPlan.setLunchRecipe(recipe);
        mealPlan.setDinnerRecipe(recipe);
        assertEquals(recipe, mealPlan.getBreakfastRecipe());
        assertEquals(recipe, mealPlan.getLunchRecipe());
        assertEquals(recipe, mealPlan.getDinnerRecipe());
    }

    /**
     * Test get and set ingredients
     */
    @Test
    public void ingredientTests() {
        MealPlan mealPlan = mockMealPlan();
        Ingredient ingredient = mockIngredient();
        mealPlan.setBreakfastIngredient(ingredient);
        mealPlan.setLunchIngredient(ingredient);
        mealPlan.setDinnerIngredient(ingredient);
        assertEquals(ingredient, mealPlan.getBreakfastIngredient());
        assertEquals(ingredient, mealPlan.getLunchIngredient());
        assertEquals(ingredient, mealPlan.getDinnerIngredient());
    }

    /***
     * Test get and set Servings
     */
    @Test
    public void servingsTests() {
        Double x, y, z;
        x = 1.0;
        y = 2.5;
        z = 3.0;
        MealPlan mealPlan = mockMealPlan();
        mealPlan.setBreakfastServings(x);
        mealPlan.setLunchServings(y);
        mealPlan.setDinnerServings(z);
        assertEquals(x, mealPlan.getBreakfastServings());
        assertEquals(y, mealPlan.getLunchServings());
        assertEquals(z, mealPlan.getDinnerServings());
    }
}
