package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectFromStorage extends AppCompatActivity {
    ListView ingredientListView;
    ArrayList<RecipeIngredient> recipeIngredientList;
    RecipeIngredientAdapter recipeIngredientAdapter;
    IngredientsActivityViewModel viewModel;

    ArrayList<Ingredient> dbIngredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_storage);

        ingredientListView = findViewById(R.id.storage_recipe_ingredient_list);
        recipeIngredientList = new ArrayList<>();
        recipeIngredientAdapter = new RecipeIngredientAdapter(this, recipeIngredientList);
        ingredientListView.setAdapter(recipeIngredientAdapter);

        viewModel = new ViewModelProvider(this).get(IngredientsActivityViewModel.class);
        viewModel.getIngredients().observe(this, new Observer<ArrayList<Ingredient>>() {
            @Override
            public void onChanged(ArrayList<Ingredient> ingredients) {
                ArrayList<Ingredient> ings = ingredients;
                // so now we have the ingredients list from the database
                for(int i=0; i<ingredients.size()-1; i++) {
                    Ingredient ingredient = ingredients.get(i);
                    RecipeIngredient r = new RecipeIngredient(ingredient.getDescription(),
                            Double.toString(ingredient.getAmount()), ingredient.getUnit(), ingredient.getCategory());
                    // add it to the the list
                    recipeIngredientList.add(r);
                }
                recipeIngredientAdapter.notifyDataSetChanged();
            }
        });
    }
}
