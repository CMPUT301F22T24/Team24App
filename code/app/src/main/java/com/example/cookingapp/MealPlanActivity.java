package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This is the MealPlan class, it holds all of the information for a specific daily meal plan
 */
public class MealPlanActivity extends AppCompatActivity {
    ListView mealPlanListView;
    TextView currentWeek;
    ArrayList<MealPlan> mealPlanList;
    ArrayList<LocalDate> week;
    MealPlanAdapter mealPlanAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    MealPlanActivityViewModel viewModel;
    private static LocalDate currMonday = LocalDate.now( ZoneId.systemDefault())
            .with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // set the buttons and text view
        currentWeek = findViewById(R.id.current_week);

        // initialize the array adapter for the list of meal plans
        mealPlanListView = findViewById(R.id.meal_plan_list);
        mealPlanList = new ArrayList<>();
        mealPlanAdapter = new MealPlanAdapter(this, mealPlanList);
        mealPlanListView.setAdapter(mealPlanAdapter);

        setWeek(currMonday);
        onClickItem();
    }

    /**
     * This method is invoked when an item is clicked
     */
    public void onClickItem(){
        mealPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MealPlan meal = mealPlanList.get(i);
                viewMeal(meal);
            }//onItemClick
        });
    }

    /**
     * This method calls the ViewMealPlanActivity allowing for the user to view a meal plan
     * @param meal: a meal plan that is to be viewed
     */
    public void viewMeal(MealPlan meal){
        Intent intent = new Intent(MealPlanActivity.this, ViewMealPlanActivity.class);
        intent.putExtra("meal",meal);
        startActivity(intent);
    }

    /**
     * When the next week is clicked
     * @param view
     */
    public void onClickNextWeek(View view){
        currMonday = currMonday.plusDays(7);
        setWeek(currMonday);
    }

    /**
     * When the previous week is clicked
     * @param view
     */
    public void onClickPrevWeek(View view){
        currMonday = currMonday.minusDays(7);
        setWeek(currMonday);
    }

    public void setWeek(LocalDate currDate){
        // clear the week
        week = new ArrayList<>();
        mealPlanList.clear();
        // get the days of the week
        MealPlan meal;
        for(int i = 0; i < 7; i++) {
            week.add(currDate);
            meal = new MealPlan(currDate.toString(),null,null,null,null,null,null);
            mealPlanList.add(meal);
            currDate =  currDate.plusDays(1);
        }

        // update the current week
        LocalDate firstDay = week.get(0);
        LocalDate lastDay = week.get(6);
        getFromDB(firstDay.toString());
        String currWeek = firstDay.getMonth().toString().substring(0,3) + " " + Integer.toString(firstDay.getDayOfMonth())
                +" - " + lastDay.getMonth().toString().substring(0,3) + " " + Integer.toString(lastDay.getDayOfMonth());
        currentWeek.setText(currWeek);
    }

    public void getFromDB(String day) {
        System.out.println(day);
        viewModel = new ViewModelProvider(this).get(MealPlanActivityViewModel.class);
        viewModel.getMealPlan(day).observe(this, new Observer<ArrayList<MealPlan>>() {
            @Override
            public void onChanged(ArrayList<MealPlan> mealPlans) {
                ArrayList<MealPlan> meals = mealPlans;
                System.out.println(meals);
                if (mealPlans != null) {
                    ArrayList<MealPlan> newMeals = mealPlanList;
                    int ind;
                    for(int i = 0; i < mealPlans.size();i++){
                        ind = week.indexOf(LocalDate.parse(mealPlans.get(i).getDate()));
                        newMeals.remove(ind);
                        newMeals.add(ind,mealPlans.get(i));
                    }
                    mealPlanList = newMeals;
                    mealPlanAdapter = new MealPlanAdapter(getApplicationContext(), newMeals);
                    mealPlanListView.setAdapter(mealPlanAdapter);
                }
                mealPlanAdapter.notifyDataSetChanged();
            }
        });
    }
}