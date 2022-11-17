package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class MealPlanActivity extends AppCompatActivity {
    ListView mealPlanListView;
    TextView currentWeek;
    ArrayList<MealPlan> mealPlanList;
    ArrayList<MealPlan> dbMeal;
    ArrayList<LocalDate> week;
    MealPlanAdapter mealPlanAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    MealPlanActivityViewModel viewModel;
    LocalDate currSunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set the buttons and text view
        currentWeek = findViewById(R.id.current_week);

        // initialize the array adapter for the list of meal plans
        mealPlanListView = findViewById(R.id.meal_plan_list);
        mealPlanList = new ArrayList<>();
        mealPlanAdapter = new MealPlanAdapter(this, mealPlanList);
        mealPlanListView.setAdapter(mealPlanAdapter);

        currSunday =
                LocalDate.now( ZoneId.systemDefault())
                        .with( TemporalAdjusters.previous( DayOfWeek.SUNDAY ) );
        //Recipe recipe = new Recipe();
        //recipe.setTitle("testing");
        //MealPlan testMeal = new MealPlan(currSunday.toString(),recipe,null,null,null,null,null);

        //viewModel = new ViewModelProvider(this).get(MealPlanActivityViewModel.class);
        //viewModel.addMealPlan(testMeal);
        setWeek(currSunday);
    }

    public void onClickNextWeek(View view){
        currSunday = currSunday.plusDays(7);
        setWeek(currSunday);
    }

    public void onClickPrevWeek(View view){
        currSunday = currSunday.minusDays(7);
        setWeek(currSunday);
    }

    public void setWeek(LocalDate currDate){
        // clear the week
        week = new ArrayList<>();

        // get the days of the week
        MealPlan meal;
        for(int i = 0; i < 7; i++){
            week.add(currDate);
            meal = new MealPlan(currDate.toString(),null,null,null,null,null,null);
            mealPlanList.add(meal);
            getFromDB(currDate.toString(),i);
            currDate =  currDate.plusDays(1);
        }

        // update the current week
        LocalDate firstDay = week.get(0);
        LocalDate lastDay = week.get(6);
        String currWeek = firstDay.getMonth().toString().substring(0,3) + " " + Integer.toString(firstDay.getDayOfMonth())
                +" - " + lastDay.getMonth().toString().substring(0,3) + " " + Integer.toString(lastDay.getDayOfMonth());
        currentWeek.setText(currWeek);
    }

    public void getFromDB(String day, int position) {
        viewModel = new ViewModelProvider(this).get(MealPlanActivityViewModel.class);
        ArrayList<MealPlan> arrayMeal = viewModel.getMealPlan(day);
        if (arrayMeal.size() >= 1) {
            MealPlan meal = arrayMeal.get(0);
            mealPlanList.add(position,meal);
        }
    }
}