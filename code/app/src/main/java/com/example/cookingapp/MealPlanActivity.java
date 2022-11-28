package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This is the MealPlanActivity class, is called when meal plan is clicked from the main page
 */
public class MealPlanActivity extends AppCompatActivity {
    ListView mealPlanListView;
    TextView currentWeek;
    ArrayList<MealPlan> mealPlanList;
    ArrayList<LocalDate> week;
    MealPlanAdapter mealPlanAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    MealPlanActivityViewModel viewModel;
    ImageButton previousWeekButton;
    ImageButton nextWeekButton;
    private static LocalDate currMonday = LocalDate.now( ZoneId.systemDefault())
            .with( TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY ) );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        previousWeekButton = findViewById(R.id.previous_week_button);
        nextWeekButton = findViewById(R.id.next_week_button);

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

    /**
     * Sets the current week dates and current weeks meal plans
     * @param currDate
     */
    public void setWeek(LocalDate currDate){

        // Disable until data fetched
        previousWeekButton.setImageAlpha(75);
        previousWeekButton.setEnabled(false);
        nextWeekButton.setImageAlpha(75);
        nextWeekButton.setEnabled(false);

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
        ArrayList<String> docIds = new ArrayList<>();
        for (int i = 0; i <= 6; i ++)
            docIds.add(week.get(i).toString());
        System.out.println(docIds);
        getFromDB(docIds);
        String currWeek = firstDay.getMonth().toString().substring(0,3) + " " + Integer.toString(firstDay.getDayOfMonth())
                +" - " + lastDay.getMonth().toString().substring(0,3) + " " + Integer.toString(lastDay.getDayOfMonth());
        currentWeek.setText(currWeek);
    }

    /**
     * Get the meal plans that are in the database
     * @param docIds: list of dates to fetch
     */
    public void getFromDB(ArrayList<String> docIds) {
        viewModel = new ViewModelProvider(this).get(MealPlanActivityViewModel.class);
        viewModel.getMealPlans(docIds).observe(this, new Observer<ArrayList<MealPlan>>() {
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

                    // re-enable buttons
                    previousWeekButton.setImageAlpha(255);
                    previousWeekButton.setEnabled(true);
                    nextWeekButton.setImageAlpha(255);
                    nextWeekButton.setEnabled(true);
                }
                mealPlanAdapter.notifyDataSetChanged();

            }
        });
    }
}