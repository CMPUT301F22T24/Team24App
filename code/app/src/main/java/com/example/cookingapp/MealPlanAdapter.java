package com.example.cookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * <p>
 * This is the class for the meal plan adapter. It connects the meal plan array list to the array list item.
 * </p>
 */
public class MealPlanAdapter extends ArrayAdapter<MealPlan> {
    private final ArrayList<MealPlan> mealPlanList;
    private final Context context;

    public MealPlanAdapter(@NonNull Context context, ArrayList<MealPlan> mealPlanList) {
        super(context, 0, mealPlanList);
        this.context = context;
        this.mealPlanList = mealPlanList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.mealplan_list_item, parent, false);
        }

        MealPlan mealPlan = mealPlanList.get(position);

        TextView dayOfWeek = view.findViewById(R.id.day_of_the_week);
        TextView breakfast = view.findViewById(R.id.breakfast);
        TextView lunch = view.findViewById(R.id.lunch);
        TextView dinner = view.findViewById(R.id.dinner);


        // set all of the required fields for each item in the list
        dayOfWeek.setText(mealPlan.getDate().getDayOfWeek().toString());

        String breakfast_title = mealPlan.getBreakfastRecipe() != null ?
                mealPlan.getBreakfastRecipe().getTitle(): mealPlan.getBreakfastIngredient() != null ?
                mealPlan.getBreakfastIngredient().getDescription() : null;
        breakfast.setText(breakfast_title);

        String lunch_title = mealPlan.getLunchRecipe() != null ?
                mealPlan.getLunchRecipe().getTitle():mealPlan.getLunchIngredient() != null ?
                mealPlan.getLunchIngredient().getDescription() : null;
        lunch.setText(lunch_title);

        String dinner_title = mealPlan.getDinnerRecipe() != null ?
                mealPlan.getDinnerRecipe().getTitle():mealPlan.getDinnerIngredient() != null ?
                mealPlan.getDinnerIngredient().getDescription():null;
        dinner.setText(dinner_title);


        return view;
    }

}

