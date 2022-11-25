package com.example.cookingapp;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {


    ListView shoppingListview;
    ArrayList<ShoppingListItem> shoppingList;
    ShoppingListAdapter shoppinglistadapter;

    ArrayList<RecipeIngredient> ingredientList;


    ArrayList<LocalDate> week;
    TextView shopping_list_date_week;

    private static LocalDate currMonday = LocalDate.now( ZoneId.systemDefault())
            .with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        shopping_list_date_week = findViewById(R.id.shopping_list_current_week);


        shoppingListview = findViewById(R.id.shopping_list);
        shoppingList = new ArrayList<>();
        shoppinglistadapter = new ShoppingListAdapter(this, shoppingList);
        shoppingListview.setAdapter(shoppinglistadapter);

        ingredientList = new ArrayList<>();
        ingredientList.add(new RecipeIngredient("Potato","5.0","kg","vegetable"));
        ingredientList.add(new RecipeIngredient("Tomato","2.77","kg","vegetable"));


        Log.d("YesPlease",ingredientList.get(0).getDescription());

        shoppingList.add(new ShoppingListItem(ingredientList.get(0),true));
        shoppingList.add(new ShoppingListItem(ingredientList.get(1),false));

        //Log.d("YesPlease",shoppingList.get(0).getIngredient().getDescription());






    }//oncreate

    public void onClickNextWeekShopping(View view){
        currMonday = currMonday.plusDays(7);
        setWeek(currMonday);
    }//onClickNextWeek

    public void onClickPrevWeekShopping(View view){
        currMonday = currMonday.minusDays(7);
        setWeek(currMonday);
    }//onClickprevWeek

    public void setWeek(LocalDate currDate){
        // clear the week
        week = new ArrayList<>();
        shoppingList.clear();
        // get the days of the week
        MealPlan meal;

        // update the current week
        LocalDate firstDay = week.get(0);
        LocalDate lastDay = week.get(6);
        String currWeek = firstDay.getMonth().toString().substring(0,3) + " " + Integer.toString(firstDay.getDayOfMonth())
                +" - " + lastDay.getMonth().toString().substring(0,3) + " " + Integer.toString(lastDay.getDayOfMonth());
        shopping_list_date_week.setText(currWeek);
    }//setWeek

}