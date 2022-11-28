package com.example.cookingapp;

import android.util.Log;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * This is the Recipe class. This is the object that defines a recipe.
 * </p>
 */
public class Recipe implements Serializable, MealPlanChoice {
    /**
     * recipe attributes: title, servings, category, comments, prepTime and image.
     */
    private String title;
    private String servings;
    private String category;
    private String comments;
    private String prepTime;
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private String image;

    @DocumentId
    private String documentId;

    /**
     * instantiate recipe
     */
    public Recipe() {
        this.title = null;
        this.servings = null;
        this.category = null;
        this.comments = null;
        this.prepTime = null;
        this.ingredients = null;
        this.image = null;
    }

    /**
     * instantiate recipe
     * @param title
     * @param servings
     * @param category
     * @param comments
     * @param prepTime
     * @param ingredients
     * @param image
     */
    public Recipe(String title, String servings, String category, String comments, String prepTime, ArrayList<RecipeIngredient> ingredients, String image) {
        this.title = title;
        this.servings = servings;
        this.category = category;
        this.comments = comments;
        this.prepTime = prepTime;
        this.ingredients = ingredients;
        this.image = image;
    }

    /**
     * getter and setter
     * @return
     */
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

    public List<RecipeIngredient> getIngredients() { return ingredients; }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getDocumentId() { return documentId; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    /**
     * Recipe Title Comparator change title to lower case.
     */
    public static Comparator<Recipe> RecipeTitleComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getTitle();
            String d2 = i2.getTitle();
            d1=d1.toLowerCase();
            d2=d2.toLowerCase();

            // desc order
            return d1.compareTo(d2);
        }
    };

    /**
     * Recipe Preparation Time Comparator set the prep time
     */
    public static Comparator<Recipe> RecipePreparationTimeComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getPrepTime();
            String d2 = i2.getPrepTime();
            d1 = d1.replace("hrs","");
            d1 = d1.replace("min","");
            d1 = d1.replace(" ","");
            d2 = d2.replace("hrs","");
            d2 = d2.replace("min","");
            d2 = d2.replace(" ","");

            if(Integer.parseInt(d1)<Integer.parseInt(d2)){
                return -1;

            }else if(Integer.parseInt(d1)>Integer.parseInt(d2)){
                return 1;
            }else{//if equal
                return 0;
            }


            // desc order
        }
    };

    /**
     * Recipe Servings Comparator get servings
     */
    public static Comparator<Recipe> RecipeServingsComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getServings();
            String d2 = i2.getServings();

            if(Integer.parseInt(d1)<Integer.parseInt(d2)){
                return -1;

            }else if(Integer.parseInt(d1)>Integer.parseInt(d2)){
                return 1;
            }else{//if equal
                return 0;
            }


        }
    };

    /**
     * Recipe Category Comparator change Category to lower case
     */
    public static Comparator<Recipe> RecipeCategoryComparator = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe i1, Recipe i2) {
            String d1 = i1.getCategory();
            String d2 = i2.getCategory();
            d1=d1.toLowerCase();
            d2=d2.toLowerCase();
            // desc order
            return d1.compareTo(d2);
        }
    };

    /**
     * meal plan choice
     * @param servings
     * @return
     */
    @Override
    public MealPlanChoice scale(int servings) {

        return null;
    }
}
