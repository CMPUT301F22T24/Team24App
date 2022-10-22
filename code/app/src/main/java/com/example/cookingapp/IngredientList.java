package com.example.cookingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class IngredientList extends AppCompatActivity {
    private final int ADD_REQUEST_CODE = 1;




    int position = -1; // for item selection and deletion
    ListView ingredientListView;
    ArrayList<Ingredient> ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
        ingredientListView = findViewById(R.id.ingredient_list);
        ingredientList = new ArrayList<>();
        ingredientAdapter = new ArrayAdapter<>(this, R.layout.content, R.id.textView, ingredientList);
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
                Toast.makeText(IngredientList.this, selected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAddClick(View view) {
        Intent intent = new Intent(this, AddIngredientActivity.class);
        intent.putExtra("requestCode", ADD_REQUEST_CODE);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    public void onDeleteClick(View view) {
        // if position is not -1 that means something was selected
        if (position != -1) {
            ingredientList.remove(position);
            ingredientAdapter.notifyDataSetChanged();
            position = -1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REQUEST_CODE) {

        }
    }
}