package com.example.cookingapp;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {
    private String description;
    private String amount;
    private String unit;
    private String category;

    public RecipeIngredient() {
        this.description = null;
        this.amount = null;
        this.unit = null;
        this.category = null;
    }

    public RecipeIngredient(String description, String amount, String unit, String category) {
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        // we need just the description for selection from storage
        return(this.description);
    }
}
