package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        recipeList.add(new Recipe());
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeListView.setAdapter(recipeAdapter);
    }

    public void onAddRecipeClick(View view){
        // when the add button is clicked the user is redirected to the add recipe screen
        //Intent intent = new Intent(this, AddRecipeActivity.class);
        //activityResultLauncher.launch(intent);
        startActivity(new Intent(RecipesActivity.this, AddRecipeActivity.class));
    }
}