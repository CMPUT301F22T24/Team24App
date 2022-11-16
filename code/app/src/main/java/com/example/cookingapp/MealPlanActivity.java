package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MealPlanActivity extends AppCompatActivity {
    ListView mealPlanListView;
    ArrayList<MealPlan> mealPlanList;
    MealPlanAdapter mealPlanAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    MealPlanActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize the array adapter for the list of meal plans
        mealPlanListView= findViewById(R.id.meal_plan_list);
        mealPlanList = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setTitle("testing");
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("hola");
        MealPlan meal1 = new MealPlan(new Date(), recipe,recipe,recipe,null,null,null);
        MealPlan meal2 = new MealPlan(new Date(), null,recipe,null,null,null,ingredient);
        mealPlanList.add(meal1);
        mealPlanList.add(meal2);
        mealPlanAdapter = new MealPlanAdapter(this, mealPlanList);
        mealPlanListView.setAdapter(mealPlanAdapter);
    }
}