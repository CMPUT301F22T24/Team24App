package com.example.cookingapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;

public class ShoppingListActivity extends AppCompatActivity {


    ListView shoppingListview;
    ArrayList<ShoppingListItem> shoppingList;
    ShoppingListAdapter shoppinglistadapter;
    ShoppingListActivityViewModel viewModel;
    ArrayList<RecipeIngredient> ingredientList;


    ArrayList<LocalDate> week;
    TextView shopping_list_date_week;
    Spinner sortBySpinner;
    CustomSpinnerAdapter sortBySpinnerAdapter;


    private static LocalDate currMonday = LocalDate.now( ZoneId.systemDefault())
            .with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        initSortBySpinner();

        shopping_list_date_week = findViewById(R.id.shopping_list_current_week);
        viewModel = new ViewModelProvider(this).get(ShoppingListActivityViewModel.class);

        shoppingListview = findViewById(R.id.shopping_list);
        shoppingList = new ArrayList<>();
        shoppinglistadapter = new ShoppingListAdapter(this, shoppingList);
        shoppingListview.setAdapter(shoppinglistadapter);

        ingredientList = new ArrayList<>();
        ingredientList.add(new RecipeIngredient("Potato", "5.0", "kg", "vegetable"));
        ingredientList.add(new RecipeIngredient("Tomato", "2.77", "kg", "vegetable"));
        ingredientList.add(new RecipeIngredient("Orange", "0.21", "lb", "fruit"));
        ingredientList.add(new RecipeIngredient("Strawberry", "2.1", "kg", "fruit"));


        Log.d("YesPlease", ingredientList.get(0).getDescription());

        shoppingList.add(new ShoppingListItem(ingredientList.get(0), true));
        shoppingList.add(new ShoppingListItem(ingredientList.get(1), true));
        shoppingList.add(new ShoppingListItem(ingredientList.get(2), true));
        shoppingList.add(new ShoppingListItem(ingredientList.get(3), true));

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

    public void setWeek(LocalDate currDate) {
        // clear the week
        week = new ArrayList<>();
        shoppingList.clear();
        // get the days of the week
        MealPlan meal;

        // update the current week
        LocalDate firstDay = week.get(0);
        LocalDate lastDay = week.get(6);
        String currWeek = firstDay.getMonth().toString().substring(0, 3) + " " + Integer.toString(firstDay.getDayOfMonth())
                + " - " + lastDay.getMonth().toString().substring(0, 3) + " " + Integer.toString(lastDay.getDayOfMonth());
        shopping_list_date_week.setText(currWeek);
    }//setWeek


    /**
     * <p>
     * Initializes the sort spinner
     * </p>
     */
    private void initSortBySpinner() {
        sortBySpinner = findViewById(R.id.shopping_list_sortBy_spinner);
        ArrayList<String> sortBy = new ArrayList<String>() {{
            add("Title");
            add("Category");
            add("");
        }};


        sortBySpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, sortBy);
        sortBySpinner.setAdapter(sortBySpinnerAdapter);
        sortBySpinner.setSelection(sortBySpinnerAdapter.getCount());

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                if ("Category".equals(selection)) {
                    Collections.sort(shoppingList, ShoppingListItem.ShoppingListItemCategoryComparator);
                } else {
                    Collections.sort(shoppingList, ShoppingListItem.ShoppingListItemTitleComparator);
                }
                shoppinglistadapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });


    }//initSortBySpinner /**









    public void onDoneShopping(View view) {
        for (ShoppingListItem shoppingListItem : shoppingList) {
            if (shoppingListItem.getChecked()) {
                Log.d("here", shoppingListItem.getIngredient().getDescription());
                EditShoppingListItemDialogFragment.newInstance(shoppingListItem).show(getSupportFragmentManager(), "EDIT_SHOPPING_ITEM");

            }
        }

    }
}