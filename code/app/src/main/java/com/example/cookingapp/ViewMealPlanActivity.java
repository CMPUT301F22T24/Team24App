package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Objects;

public class ViewMealPlanActivity extends AppCompatActivity {
    TextView prepTimeBreakfast;
    TextView prepTimeLunch;
    TextView prepTimeDinner;
    TextView servingsBreakfast;
    TextView servingsLunch;
    TextView servingsDinner;
    TextView breakfastName;
    TextView lunchName;
    TextView dinnerName;
    ImageView breakfastImage;
    ImageView lunchImage;
    ImageView dinnerImage;
    TextView date;
    MealPlan mealPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal_plan);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        date = findViewById(R.id.date_chosen);
        prepTimeBreakfast = findViewById(R.id.breakfast_prep_time);
        prepTimeLunch = findViewById(R.id.lunch_prep_time);
        prepTimeDinner = findViewById(R.id.dinner_prep_time);
        servingsBreakfast = findViewById(R.id.breakfast_servings);
        servingsLunch = findViewById(R.id.lunch_servings);
        servingsDinner = findViewById(R.id.dinner_servings);
        breakfastName = findViewById(R.id.breakfast);
        lunchName = findViewById(R.id.lunch);
        dinnerName = findViewById(R.id.dinner);
        breakfastImage = findViewById(R.id.breakfast_image);
        lunchImage = findViewById(R.id.lunch_image);
        dinnerImage = findViewById(R.id.dinner_image);

        getData();
        setDate();
        setBreakfast();
        setLunch();
        setDinner();
    }

    public void getData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mealPlan = (MealPlan) extras.getSerializable("meal");
        }
    }

    public void setDate(){
        LocalDate currentDate = LocalDate.parse(mealPlan.getDate());
        String month = currentDate.getMonth().toString();
        String day = Integer.toString(currentDate.getDayOfMonth());
        String year = Integer.toString(currentDate.getYear());
        String fullDate = month + " " + day + "," + year;
        SpannableString content = new SpannableString(fullDate);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        date.setText(content);
    }

    public void setBreakfast(){
        breakfastImage.setImageResource(R.mipmap.camera);
        if ( mealPlan.getBreakfastRecipe() != null){
            Recipe breakfast = mealPlan.getBreakfastRecipe();
            breakfastName.setText(breakfast.getTitle());
            prepTimeBreakfast.setText(breakfast.getPrepTime());
            servingsBreakfast.setText(breakfast.getServings());
            if(breakfast.getImage() != null){
                breakfastImage.setImageBitmap(StringToBitMap(breakfast.getImage()));
            }

        } else if (mealPlan.getBreakfastIngredient() != null) {
            Ingredient breakfast = mealPlan.getBreakfastIngredient();
            breakfastName.setText(breakfast.getDescription());
            String prepTime = "0hrs 0min";
            prepTimeBreakfast.setText(prepTime);
            servingsBreakfast.setText(breakfast.getUnit());

        } else {
            breakfastName.setText(null);
            prepTimeBreakfast.setText(null);
            servingsBreakfast.setText(null);
            breakfastImage.setImageBitmap(null);
        }

    }

    public void setLunch(){
        lunchImage.setImageResource(R.mipmap.camera);
        if ( mealPlan.getLunchRecipe() != null){
            Recipe lunch = mealPlan.getLunchRecipe();
            lunchName.setText(lunch.getTitle());
            prepTimeLunch.setText(lunch.getPrepTime());
            servingsLunch.setText(lunch.getServings());
            if(lunch.getImage() != null){
                lunchImage.setImageBitmap(StringToBitMap(lunch.getImage()));
            }
        } else if (mealPlan.getLunchIngredient() != null) {
            Ingredient lunch = mealPlan.getLunchIngredient();
            lunchName.setText(lunch.getDescription());
            String prepTime = "0hrs 0min";
            prepTimeLunch.setText(prepTime);
            servingsLunch.setText(lunch.getUnit());

        } else {
            lunchName.setText(null);
            prepTimeLunch.setText(null);
            servingsLunch.setText(null);
            lunchImage.setImageBitmap(null);
        }

    }

    public void setDinner(){
        dinnerImage.setImageResource(R.mipmap.camera);
        if ( mealPlan.getDinnerRecipe() != null){
            Recipe dinner = mealPlan.getDinnerRecipe();
            dinnerName.setText(dinner.getTitle());
            prepTimeDinner.setText(dinner.getPrepTime());
            servingsDinner.setText(dinner.getServings());
            if(dinner.getImage() != null){
                dinnerImage.setImageBitmap(StringToBitMap(dinner.getImage()));
            }

        } else if (mealPlan.getDinnerIngredient() != null) {
            Ingredient dinner = mealPlan.getDinnerIngredient();
            dinnerName.setText(dinner.getDescription());
            String prepTime = "0hrs 0min";
            prepTimeDinner.setText(prepTime);
            servingsDinner.setText(dinner.getUnit());

        } else {
            dinnerName.setText(null);
            prepTimeDinner.setText(null);
            servingsDinner.setText(null);
            dinnerImage.setImageBitmap(null);
        }

    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}