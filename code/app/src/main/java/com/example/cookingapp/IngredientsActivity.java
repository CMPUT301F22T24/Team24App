package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity implements ViewIngredientDialogFragment.OnFragmentInteractionListener{
    private final int ADD_REQUEST_CODE = 1;

    ListView ingredientListView;
    ArrayList<Ingredient> ingredientList;
    IngredientAdapter ingredientAdapter;
    IngredientsActivityViewModel viewModel;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ingredientListView = findViewById(R.id.ingredient_list);
        ingredientList = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientList);
        ingredientListView.setAdapter(ingredientAdapter);

        // TODO: handle case when no connection to db (loading state / display error)
        viewModel = new ViewModelProvider(this).get(IngredientsActivityViewModel.class);
        viewModel.getIngredients().observe(this, new Observer<ArrayList<Ingredient>>() {
                    @Override
                    public void onChanged(ArrayList<Ingredient> ingredients) {
                        if (ingredients != null) {
                            ingredientList = ingredients;
                            ingredientAdapter = new IngredientAdapter(getApplicationContext(), ingredients);
                            ingredientListView.setAdapter(ingredientAdapter);
                        }
                        ingredientAdapter.notifyDataSetChanged();
                    }
                });

                activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            Ingredient ingredient = (Ingredient) intent.getSerializableExtra("ingredient");
                            if (ingredient != null) {
                               viewModel.addIngredient(ingredient);
                            }
                        }
                    }
                });

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewIngredientDialogFragment.newInstance(ingredientList.get(i)).show(getSupportFragmentManager(), "VIEW_INGREDIENT");
            }//onItemClick
        });
    }

    // TODO: change add button color
    public void onAddClick(View view) {
        Intent intent = new Intent(this, AddIngredientActivity.class);
        activityResultLauncher.launch(intent);
    }//onAddClick

    @Override
    public void onEdit(Ingredient ingredient) {
        // TODO: edit activity
    }

    @Override
    public void onDelete(Ingredient ingredient) {
        // Confirm user wants to delete ingredient
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
        .setTitle("Delete " + ingredient.getDescription()+"?")
        .setMessage("You will not be able to undo this action!")
        .setNegativeButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  delete and update list
                        viewModel.deleteIngredient(ingredient);
                    }
                })
        .setPositiveButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        // to change color
        // dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }
}