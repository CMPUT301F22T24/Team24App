package com.example.cookingapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String title;
    private Double servings;
    private String category;
    private String comments;
    private String prepTime;
    private ArrayList<Ingredient> ingredients;
    private Uri image;

    public Recipe() {
        this.title = null;
        this.servings = null;
        this.category = null;
        this.comments = null;
        this.prepTime = null;
        this.ingredients = null;
        this.image = null;
    }

    public Recipe(String title, Double servings, String category, String comments, String prepTime, ArrayList<Ingredient> ingredients, Uri image) {
        this.title = title;
        this.servings = servings;
        this.category = category;
        this.comments = comments;
        this.prepTime = prepTime;
        this.ingredients = ingredients;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getServings() {
        return servings;
    }

    public void setServings(Double servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPrepTime() { return prepTime; }

    public void setPrepTime(String prepTime) { this.prepTime = prepTime; }

    public ArrayList<Ingredient> getIngredients() { return ingredients; }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Uri getImage() { return image; }

    public void setImage(Uri image) { this.image = image; }
}
