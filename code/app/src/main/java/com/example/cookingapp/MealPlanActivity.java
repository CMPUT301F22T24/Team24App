package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
    ArrayList<LocalDate> week;
    MealPlanAdapter mealPlanAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    MealPlanActivityViewModel viewModel;
    LocalDate currMonday;


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

        currMonday =
                LocalDate.now( ZoneId.systemDefault())
                        .with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );

        setWeek(currMonday);
    }

    public void onClickNextWeek(View view){
        currMonday = currMonday.plusDays(7);
        setWeek(currMonday);
    }

    public void onClickPrevWeek(View view){
        currMonday = currMonday.minusDays(7);
        setWeek(currMonday);
    }

    public void setWeek(LocalDate currDate){
        // clear the week
        week = new ArrayList<>();

        // get the days of the week
        MealPlan meal;
        for(int i = 0; i < 7; i++){
            week.add(currDate);
            meal = new MealPlan(currDate.toString(),null,null,null,null,null,null);
            // mealPlanList.add(meal);
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
                if (mealPlans != null) {
                    mealPlanList = mealPlans;
                    mealPlanAdapter = new MealPlanAdapter(getApplicationContext(), mealPlans);
                    mealPlanListView.setAdapter(mealPlanAdapter);
                }
                mealPlanAdapter.notifyDataSetChanged();
            }
        });
    }
}