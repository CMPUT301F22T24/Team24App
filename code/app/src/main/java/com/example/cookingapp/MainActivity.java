package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button ingredients;
    Button recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ingredients = findViewById(R.id.main_ingredient_button);
        recipes = findViewById(R.id.main_recipe_button);
    }

    /**
     *
     * @param view
     * commences the activity_ingredient_list.xml
     */
    public void startIngredientList(View view) {
        startActivity(new Intent(MainActivity.this, IngredientsActivity.class));

    }//startIngredientList

    /**
     *
     * @param view
     * commences the activity_recipe_list.xml
     */
    public void startRecipeList(View view) {
        startActivity(new Intent(MainActivity.this, RecipesActivity.class));

    }//startRecipeList
}