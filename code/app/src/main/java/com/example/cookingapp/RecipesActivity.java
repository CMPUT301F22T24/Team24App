package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class RecipesActivity extends AppCompatActivity {
    ListView recipeListView;
    private static final ArrayList<Recipe> recipeList = new ArrayList<>();;
    RecipeAdapter recipeAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // initialize the array adapter for the list of recipes
        recipeListView = findViewById(R.id.recipe_list);
        recipeList.add(new Recipe());
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeListView.setAdapter(recipeAdapter);
        getNewRecipe();

    }

    public void getNewRecipe() {
        // TODO: update the list this is  not working
        // add the newly submitted food to the array
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
        if (recipe != null) {
            recipeList.add(recipe);
        }
    }

    public void onAddRecipeClick(View view){
        // when the add button is clicked the user is redirected to the add recipe screen
        //Intent intent = new Intent(this, AddRecipeActivity.class);
        //activityResultLauncher.launch(intent);
        startActivity(new Intent(RecipesActivity.this, AddRecipeActivity.class));
    }
}