package com.example.cookingapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StorageSelection implements Serializable {
    private ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

    public StorageSelection(ArrayList<RecipeIngredient> selection) {
        this.ingredients = selection;
    }
    public ArrayList<RecipeIngredient> getIngredients() { return ingredients; }
}
