package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class RecipesActivity extends AppCompatActivity {
    ListView recipeListView;
    ArrayList<Recipe> recipeList;
    RecipeAdapter recipeAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    RecipeActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // initialize the array adapter for the list of recipes
        recipeListView = findViewById(R.id.recipe_list);
        recipeList = new ArrayList<>();;
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeListView.setAdapter(recipeAdapter);

        // TODO: handle case when no connection to db (loading state / display error)
        viewModel = new ViewModelProvider(this).get(RecipeActivityViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                if (recipes != null) {
                    recipeList = recipes;
                    recipeAdapter = new RecipeAdapter(getApplicationContext(),recipes);
                    recipeListView.setAdapter(recipeAdapter);
                }
                recipeAdapter.notifyDataSetChanged();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
                    if (recipe != null) {
                        viewModel.addRecipe(recipe);
                    }
                }
            }
        });
    }

    public void onAddRecipeClick(View view){
        // when the add button is clicked the user is redirected to the add recipe screen
        Intent intent = new Intent(this, AddRecipeActivity.class);
        activityResultLauncher.launch(intent);
    }
}