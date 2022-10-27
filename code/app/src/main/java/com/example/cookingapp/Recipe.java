package com.example.cookingapp;

import java.io.Serializable;

public class Recipe implements Serializable {
    private String title;
    private Double servings;
    private String category;
    private String comments;
    //TODO: photograph, preptime, ingredient list and picture??

    public Recipe() {
        this.title = null;
        this.servings = null;
        this.category = null;
        this.comments = null;
    }

    public Recipe(String title, Double servings, String category, String comments) {
        this.title = title;
        this.servings = servings;
        this.category = category;
        this.comments = comments;
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
}
