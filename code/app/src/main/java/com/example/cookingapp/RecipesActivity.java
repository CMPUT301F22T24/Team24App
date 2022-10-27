package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class RecipesActivity extends AppCompatActivity {
    ListView recipeListView;
    ArrayList<Recipe> recipeList;
    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // initialize the array adapter for the list of recipes
        recipeListView = findViewById(R.id.recipe_list);
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeListView.setAdapter(recipeAdapter);
    }
}