package com.example.cookingapp;


import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class ShoppingListActivity extends AppCompatActivity {


    ListView shoppingListview;
    ArrayList<ShoppingList> shoppingList;
    ShoppingListAdapter shoppinglistadapter;
    LocalDate expiryDate;
    Button FromDate;
    Button ToDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        FromDate = findViewById(R.id.shopping_list_from_date);
        ToDate = findViewById(R.id.shopping_list_to_date);



        shoppingListview = findViewById(R.id.shopping_list);
        shoppingList = new ArrayList<>();
        shoppinglistadapter = new ShoppingListAdapter(this, shoppingList);
        shoppingListview.setAdapter(shoppinglistadapter);


        shoppingList.add(new ShoppingList("potato",null, "pantry", 5.0, "kg", "vegetable", false));
        shoppingList.add(new ShoppingList("Tomato",null, "pantry", 2.0, "kg", "vegetable", false));
        shoppingList.add(new ShoppingList("Cucumber",null, "Fridge", 5.77, "kg", "vegetable", false));





    }//oncreate

    public void setFirstDate(View view){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ShoppingListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // account for android's indexing
                LocalDate setDate = LocalDate.of(year, month+1, dayOfMonth);
                LocalDate currentDate = LocalDate.now();
     {
                    expiryDate = LocalDate.of(year, month+1, dayOfMonth);
                    FromDate.setText(expiryDate.toString());
                }
            }
        }, year, month, day);
        datePickerDialog.show();

    }//setDate



    public void setSecondDate(View view){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ShoppingListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // account for android's indexing
                LocalDate setDate = LocalDate.of(year, month+1, dayOfMonth);
                LocalDate currentDate = LocalDate.now();
                {
                    expiryDate = LocalDate.of(year, month+1, dayOfMonth);
                    ToDate.setText(expiryDate.toString());
                }
            }
        }, year, month, day);
        datePickerDialog.show();

    }//setDate

}