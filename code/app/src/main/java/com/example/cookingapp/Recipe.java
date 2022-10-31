package com.example.cookingapp;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * <p>
 * This is the Recipe class. This is the object that defines a recipe.
 * </p>
 */
public class Recipe implements Serializable {
    private String title;
    private String servings;
    private String category;
    private String comments;
    private String prepTime;
    private ArrayList<Ingredient> ingredients;
    private String image;

    @DocumentId
    private String documentId;

    public Recipe() {
        this.title = null;
        this.servings = null;
        this.category = null;
        this.comments = null;
        this.prepTime = null;
        this.ingredients = null;
        this.image = null;
    }

    public Recipe(String title, String servings, String category, String comments, String prepTime, ArrayList<Ingredient> ingredients, String image) {
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

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
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

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getDocumentId() { return documentId; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public static Comparator<Recipe> RecipeTitleComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getTitle();
            String d2 = i2.getTitle();
            // desc order
            return d1.compareTo(d2);
        }
    };

    public static Comparator<Recipe> RecipePreparationTimeComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getPrepTime();
            String d2 = i2.getPrepTime();
            // desc order
            return d1.compareTo(d2);
        }
    };


    public static Comparator<Recipe> RecipeServingsComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getServings();
            String d2 = i2.getServings();
            // desc order
            return d1.compareTo(d2);
        }
    };

    public static Comparator<Recipe> RecipeCategoryComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getCategory();
            String d2 = i2.getCategory();
            // desc order
            return d1.compareTo(d2);
        }
    };





}
