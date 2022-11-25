package com.example.cookingapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;

public class MealPlanTest {
    private MealPlan mockMealPlan(){return new MealPlan();}
    private Recipe mockRecipe() {
        return new Recipe();
    }
    private Ingredient mockIngredient() {
        return new Ingredient();
    }

    @Test
    public void DateTest() {
        LocalDate date = LocalDate.now();
        MealPlan mealPlan = mockMealPlan();
        mealPlan.setDate(date.toString());
        assertEquals(date.toString(), mealPlan.getDate());
    }

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

}
