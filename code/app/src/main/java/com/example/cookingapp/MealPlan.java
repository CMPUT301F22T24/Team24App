package com.example.cookingapp;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class MealPlan implements Serializable {
    private String date;

    /* How to differentiate between recipe and ingredient ? */
    private Recipe breakfastRecipe;
    private Recipe lunchRecipe;
    private Recipe dinnerRecipe;
    private Ingredient breakfastIngredient;
    private Ingredient lunchIngredient;
    private Ingredient dinnerIngredient;

    @DocumentId
    private String documentId; // for data base

    public MealPlan() {
        this.date = null;
        this.breakfastRecipe = null;
        this.lunchRecipe = null;
        this.dinnerRecipe = null;
        this.breakfastIngredient = null;
        this.lunchIngredient = null;
        this.dinnerIngredient = null;
    }

    public MealPlan(String date, Recipe breakfastRecipe, Recipe lunchRecipe, Recipe dinnerRecipe, Ingredient breakfastIngredient, Ingredient lunchIngredient, Ingredient dinnerIngredient) {
        this.date = date;
        this.breakfastRecipe = breakfastRecipe;
        this.lunchRecipe = lunchRecipe;
        this.dinnerRecipe = dinnerRecipe;
        this.breakfastIngredient = breakfastIngredient;
        this.lunchIngredient = lunchIngredient;
        this.dinnerIngredient = dinnerIngredient;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBreakfastRecipe(Recipe breakfastRecipe) {
        this.breakfastRecipe = breakfastRecipe;
    }

    public void setLunchRecipe(Recipe lunchRecipe) {
        this.lunchRecipe = lunchRecipe;
    }

    public void setDinnerRecipe(Recipe dinnerRecipe) {
        this.dinnerRecipe = dinnerRecipe;
    }

    public void setBreakfastIngredient(Ingredient breakfastIngredient) {
        this.breakfastIngredient = breakfastIngredient;
    }

    public void setLunchIngredient(Ingredient lunchIngredient) {
        this.lunchIngredient = lunchIngredient;
    }

    public void setDinnerIngredient(Ingredient dinnerIngredient) {
        this.dinnerIngredient = dinnerIngredient;
    }

    public String getDate() {
        return date;
    }

    public Recipe getBreakfastRecipe() {
        return breakfastRecipe;
    }

    public Recipe getLunchRecipe() {
        return lunchRecipe;
    }

    public Recipe getDinnerRecipe() {
        return dinnerRecipe;
    }

    public Ingredient getBreakfastIngredient() {
        return breakfastIngredient;
    }

    public Ingredient getLunchIngredient() {
        return lunchIngredient;
    }

    public Ingredient getDinnerIngredient() {
        return dinnerIngredient;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
