package com.example.cookingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class IngredientsActivity extends AppCompatActivity {
    private final int ADD_REQUEST_CODE = 1;

    int position = -1; // for item selection and deletion
    ListView ingredientListView;
    ArrayList<Ingredient> ingredientList;
    IngredientAdapter ingredientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
        ingredientListView = findViewById(R.id.ingredient_list);
        ingredientList = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientList);
        ingredientListView.setAdapter(ingredientAdapter);

        // adding an egg ingredient to make sure everything works
        Date date = new Date(); // current date is default
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
        intent.putExtra("requestCode", ADD_REQUEST_CODE);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }//onAddClick

    public void onDeleteClick(View view) {
        // if position is not -1 that means something was selected
        if (position != -1) {
            ingredientList.remove(position);
            ingredientAdapter.notifyDataSetChanged();
            position = -1;
        }
    }//onDeleteClick

    public void onReturnClick(View view){
        finish();

    }//onReturnClick

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REQUEST_CODE) {
            if(data!=null) {
                String returnDescription = data.getStringExtra("Description");
                String returnLocation = data.getStringExtra("Location");
                String returnCategory = data.getStringExtra("Category");
                String returnDate = data.getStringExtra("Date");
                String returnUnit = data.getStringExtra("Unit");
                String returnCount = data.getStringExtra("Count");

                Date date = new Date();

                ingredientList.add(new Ingredient(returnDescription, date, returnLocation, Integer.parseInt(returnCount), returnUnit, returnCategory));
                ingredientAdapter.notifyDataSetChanged();


            }//if
        }//if
    }//onActivityResult




}