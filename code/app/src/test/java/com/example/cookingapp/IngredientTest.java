package com.example.cookingapp;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

public class IngredientTest {
    private Ingredient mockIngredient(){
        Ingredient ingredient = new Ingredient();
        return ingredient;
    }

    @Test
    public void DescriptionTest() {
        Ingredient ingredient = mockIngredient();
        ingredient.setDescription("food");
        assertEquals("food", ingredient.getDescription());
    }

    @Test
    public void LocationTest() {
        Ingredient ingredient = mockIngredient();
        ingredient.setLocation("fridge");
        assertEquals("fridge", ingredient.getLocation());
    }

    @Test
    public void AmountTest() {
        Ingredient ingredient = mockIngredient();
        ingredient.setAmount(1.0);
        String amount = String.valueOf(ingredient.getAmount());
        assertEquals("1.0", amount);
    }

    @Test
    public void DocumentIdTest() {
        Ingredient ingredient = mockIngredient();
        ingredient.setDocumentId("123");
        assertEquals("123", ingredient.getDocumentId());
    }

    @Test
    public void UnitTest() {
        Ingredient ingredient = mockIngredient();
        ingredient.setUnit("kg");
        assertEquals("kg", ingredient.getUnit());
    }

    @Test
    public void Category() {
        Ingredient ingredient = mockIngredient();
        ingredient.setCategory("Fruits");
        assertEquals("Fruits", ingredient.getCategory());
    }
}
