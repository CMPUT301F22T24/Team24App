package com.example.cookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectFromStorage extends AppCompatActivity {
    ListView ingredientListView;
    ArrayList<RecipeIngredient> recipeIngredientList;
//    RecipeIngredientAdapter recipeIngredientAdapter;
    IngredientsActivityViewModel viewModel;

    ArrayAdapter<RecipeIngredient> adapter;
    ArrayList<RecipeIngredient> selectedIngredientList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_storage);
        selectedIngredientList = new ArrayList<>();

        ingredientListView = findViewById(R.id.storage_recipe_ingredient_list);
        recipeIngredientList = new ArrayList<>();
//        recipeIngredientAdapter = new RecipeIngredientAdapter(this, recipeIngredientList);
        adapter = new ArrayAdapter<>(this, R.layout.select_from_storage_ingredient_item ,recipeIngredientList);
//        ingredientListView.setAdapter(recipeIngredientAdapter);
        ingredientListView.setChoiceMode(ingredientListView.CHOICE_MODE_MULTIPLE);
        ingredientListView.setAdapter(adapter);


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
//                recipeIngredientAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });

        // position -> boolean hashmap
        HashMap<Integer, Boolean> selections = new HashMap<Integer, Boolean>();

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selections.get(position) == Boolean.TRUE) {
                    Log.e("yikes", "deleting");
                    selectedIngredientList.remove(recipeIngredientList.get(position));
                    selections.put(position, Boolean.FALSE);
                }
                else {
                    // value is false or null
                    Log.e("yikes", "adding");
                    selectedIngredientList.add(recipeIngredientList.get(position));
                    selections.put(position, Boolean.TRUE);
                }

            }
        });

    }

    // after on create
    public void OnConfirmSelections(View view) {
        for (RecipeIngredient r : selectedIngredientList) {
            String s = r.getDescription();
            Log.e("pls", s);
        }
        Intent intent = new Intent(this, AddRecipeActivity.class);
        StorageSelection selection = new StorageSelection(selectedIngredientList);

        String s = selection.getIngredients().get(0).getDescription();
        Log.e("test", s);

        intent.putExtra("list", selection);
        setResult(2, intent);
        finish();



    }
}