package com.example.cookingapp;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * This is the meal plan class, it holds all of the information for a specific daily meal plan
 */
public class MealPlan implements Serializable {
    private String date;

    /* How to differentiate between recipe and ingredient ? */
    private Recipe breakfastRecipe;
    private Recipe lunchRecipe;
    private Recipe dinnerRecipe;
    private Double breakfastServings, lunchServings, dinnerServings;
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
        this.breakfastServings = null;
        this.lunchServings = null;
        this.dinnerServings = null;
        this.breakfastIngredient = null;
        this.lunchIngredient = null;
        this.dinnerIngredient = null;
    }

    public MealPlan(String date, Recipe breakfastRecipe, Recipe lunchRecipe, Recipe dinnerRecipe, Ingredient breakfastIngredient, Ingredient lunchIngredient, Ingredient dinnerIngredient) {
        this.date = date;
        this.breakfastRecipe = breakfastRecipe;
        this.lunchRecipe = lunchRecipe;
        this.dinnerRecipe = dinnerRecipe;
        this.breakfastServings = null;
        this.lunchServings = null;
        this.dinnerServings = null;
        this.breakfastIngredient = breakfastIngredient;
        this.lunchIngredient = lunchIngredient;
        this.dinnerIngredient = dinnerIngredient;
    }

    public MealPlan(String date, Recipe breakfastRecipe, Recipe lunchRecipe, Recipe dinnerRecipe, Double breakfastServings, Double lunchServings, Double dinnerServings, Ingredient breakfastIngredient, Ingredient lunchIngredient, Ingredient dinnerIngredient, String documentId) {
        this.date = date;
        this.breakfastRecipe = breakfastRecipe;
        this.lunchRecipe = lunchRecipe;
        this.dinnerRecipe = dinnerRecipe;
        this.breakfastServings = breakfastServings;
        this.lunchServings = lunchServings;
        this.dinnerServings = dinnerServings;
        this.breakfastIngredient = breakfastIngredient;
        this.lunchIngredient = lunchIngredient;
        this.dinnerIngredient = dinnerIngredient;
        this.documentId = documentId;
    }

    /**
     * sets the date of the meal plan
     *
     * @param date : the date of this daily meal plan
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * sets the breakfast recipe of the meal plan
     *
     * @param breakfastRecipe : recipe for breakfast
     */
    public void setBreakfastRecipe(Recipe breakfastRecipe) {
        this.breakfastRecipe = breakfastRecipe;
    }

    /**
     * sets the lunch recipe of the meal plan
     *
     * @param lunchRecipe : recipe for lunch
     */
    public void setLunchRecipe(Recipe lunchRecipe) {
        this.lunchRecipe = lunchRecipe;
    }

    /**
     * sets the dinner recipe of the meal plan
     *
     * @param dinnerRecipe : recipe for dinner
     */
    public void setDinnerRecipe(Recipe dinnerRecipe) {
        this.dinnerRecipe = dinnerRecipe;
    }

    /**
     * sets the breakfast ingredient of the meal plan
     *
     * @param breakfastIngredient : ingredient for breakfast
     */
    public void setBreakfastIngredient(Ingredient breakfastIngredient) {
        this.breakfastIngredient = breakfastIngredient;
    }

    /**
     * sets the lunch ingredient of the meal plan
     *
     * @param lunchIngredient : ingredient for lunch
     */
    public void setLunchIngredient(Ingredient lunchIngredient) {
        this.lunchIngredient = lunchIngredient;
    }

    /**
     * sets the dinner ingredient of the meal plan
     *
     * @param dinnerIngredient : ingredient for dinner
     */
    public void setDinnerIngredient(Ingredient dinnerIngredient) {
        this.dinnerIngredient = dinnerIngredient;
    }

    /**
     * gets the date of the meal plan
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * gets the breakfastRecipe of the meal plan
     *
     * @return breakfastRecipe
     */

    public Recipe getBreakfastRecipe() {
        return breakfastRecipe;
    }

    /**
     * gets the lunchRecipe of the meal plan
     *
     * @return lunchRecipe
     */
    public Recipe getLunchRecipe() {
        return lunchRecipe;
    }

    /**
     * gets the dinnerRecipe of the meal plan
     *
     * @return dinnerRecipe
     */
    public Recipe getDinnerRecipe() {
        return dinnerRecipe;
    }

    /**
     * Gets the breakfastServings for the meal plan
     *
     * @return breakfastServings
     */
    public Double getBreakfastServings() {
        return breakfastServings;
    }

    /**
     * Sets the breakfastServings for the meal plan
     */
    public void setBreakfastServings(Double breakfastServings) {
        this.breakfastServings = breakfastServings;
    }

    /**
     * Gets the lunchServings for the meal plan
     *
     * @return lunchServings
     */
    public Double getLunchServings() {
        return lunchServings;
    }

    /**
     * Set the lunch Servings for the meal plan
     */
    public void setLunchServings(Double lunchServings) {
        this.lunchServings = lunchServings;
    }

    /**
     * Gets the dinnerServings for the meal plan
     *
     * @return dinnerServings
     */
    public Double getDinnerServings() {
        return dinnerServings;
    }

    /**
     * Set the dinnerServings for the meal plan
     */
    public void setDinnerServings(Double dinnerServings) {
        this.dinnerServings = dinnerServings;
    }

    /**
     * gets the breakfastIngredient of the meal plan
     *
     * @return breakfastIngredient
     */

    public Ingredient getBreakfastIngredient() {
        return breakfastIngredient;
    }

    /**
     * gets the lunchIngredient of the meal plan
     *
     * @return lunchIngredient
     */
    public Ingredient getLunchIngredient() {
        return lunchIngredient;
    }

    /**
     * gets the dinnerIngredient of the meal plan
     *
     * @return dinnerIngredient
     */
    public Ingredient getDinnerIngredient() {
        return dinnerIngredient;
    }

    /**
     * gets the documentId of the meal plan
     *
     * @return documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * sets the document id for the data base
     *
     * @param documentId
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
