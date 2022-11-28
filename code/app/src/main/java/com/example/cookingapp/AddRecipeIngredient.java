package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * AddRecipeIngredient allows users to add ingredient to recipe.
 */
public class AddRecipeIngredient extends AppCompatActivity {

    EditText descriptionEditText;
    EditText categoryEditText;
    EditText unitEditText;
    EditText amountEditText;
    Button confirm;


    /**
     * onCreate allows users to add an ingredient to a recipe, with a brief description, amount, unit, and ingredient category.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_ingredient);
        descriptionEditText = findViewById(R.id.add_recipeIngredient_description_editText);
        categoryEditText = findViewById(R.id.add_recipeIngredient_category_editText);
        unitEditText = findViewById(R.id.add_recipeIngredient_unit_editText);
        amountEditText = findViewById(R.id.add_recipeIngredient_amount_editText);
        confirm = findViewById(R.id.add_recipeIngredient_confirm_button);

    }

    /**
     * onConfirm allows users to confirm addition.
     * @param view
     */
    public void onConfirm(View view) {
        String description = descriptionEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String unit = unitEditText.getText().toString();
        String amount = amountEditText.getText().toString();

        RecipeIngredient recipeIngredient = new RecipeIngredient(description, amount, unit, category);
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("recipeIngredient", recipeIngredient);
        setResult(RESULT_OK, intent);
        finish();
    }
}