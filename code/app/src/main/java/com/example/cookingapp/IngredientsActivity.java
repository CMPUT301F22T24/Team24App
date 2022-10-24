package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class IngredientsActivity extends AppCompatActivity {
    private final int ADD_REQUEST_CODE = 1;

    int position = -1; // for item selection and deletion
    ListView ingredientListView;
    ArrayList<Ingredient> ingredientList;
    IngredientAdapter ingredientAdapter;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Ingredient ingredient = (Ingredient) intent.getSerializableExtra("ingredient");
                    if (ingredient != null) {
                        ingredientList.add(ingredient);
                        ingredientAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        ingredientListView = findViewById(R.id.ingredient_list);
        ingredientList = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientList);
        ingredientListView.setAdapter(ingredientAdapter);

        // adding an egg ingredient to make sure everything works
        LocalDate date = LocalDate.now(); // current date is default
        Ingredient ingredient = new Ingredient("egg", date, "fridge", 1, "dozen", "idk");
        ingredientList.add(ingredient);
        ingredientAdapter.notifyDataSetChanged();


        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;

                String selected = ingredientList.get(position).getDescription() + " selected";
                Toast.makeText(IngredientsActivity.this, selected, Toast.LENGTH_SHORT).show();
            }//onItemClick
        });
    }

    public void onAddClick(View view) {
        Intent intent = new Intent(this, AddIngredientActivity.class);
        activityResultLauncher.launch(intent);
    }//onAddClick

    public void onDeleteClick(View view) {
        // if position is not -1 that means something was selected
        if (position != -1) {
            ingredientList.remove(position);
            ingredientAdapter.notifyDataSetChanged();
            position = -1;
        }
    }//onDeleteClick
}