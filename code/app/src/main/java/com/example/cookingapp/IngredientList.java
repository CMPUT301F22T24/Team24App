package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class IngredientList extends AppCompatActivity {
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
    }

    public void onDeleteClick(View view) {
    }
}