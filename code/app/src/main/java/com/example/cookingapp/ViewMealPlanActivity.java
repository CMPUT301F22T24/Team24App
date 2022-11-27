package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Objects;
/**
 * This is the ViewMealPlanActivity class, is invoked when a meal plan is clicked to view
 */
public class ViewMealPlanActivity extends AppCompatActivity implements ViewRecipeDialogFragment.OnFragmentInteractionListener,ViewIngredientDialogFragment.OnFragmentInteractionListener,AddMealPlanDialogFragment.OnFragmentInteractionListener {
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
    LinearLayout breakfastLayout;
    LinearLayout lunchLayout;
    LinearLayout dinnerLayout;
    TextView date;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ViewMealPlanActivityViewModel viewModel;

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
        breakfastLayout = findViewById(R.id.breakfast_layout);
        lunchLayout = findViewById(R.id.lunch_layout);
        dinnerLayout = findViewById(R.id.dinner_layout);
        viewModel = new ViewModelProvider(this).get(ViewMealPlanActivityViewModel.class);

        getData();
        setDate();
        setBreakfast();
        setLunch();
        setDinner();
        onBreakfastClick();
        onLunchClick();
        onDinnerClick();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    MealPlanChoice mealPlanChoice = (MealPlanChoice) intent.getSerializableExtra("mealPlanChoice");
                    int servings = (int) intent.getSerializableExtra("numServingsSelected");
                    Log.d("vmpa", String.valueOf(servings));
                    if (mealPlanChoice != null) {

                        System.out.println(viewModel.mealPlan.getDate());

                        if (mealPlanChoice instanceof Recipe) {
                            Recipe recipe = (Recipe) mealPlanChoice;
                            switch (viewModel.editType) {

                                case ("breakfast"):
                                    viewModel.mealPlan.setBreakfastRecipe(recipe);
                                    viewModel.mealPlan.setBreakfastIngredient(null);
                                    viewModel.mealPlan.setBreakfastServings(servings);
                                    //  viewModel.mealPlan.setBreakfast.setBreakfastServings() to what youpull from servings
                                    break;

                                case ("lunch"):
                                    viewModel.mealPlan.setLunchRecipe(recipe);
                                    viewModel.mealPlan.setLunchIngredient(null);
                                    viewModel.mealPlan.setLunchServings(servings);
                                    break;

                                case ("dinner"):
                                    viewModel.mealPlan.setDinnerRecipe(recipe);
                                    viewModel.mealPlan.setDinnerIngredient(null);
                                    viewModel.mealPlan.setDinnerServings(servings);
                                    break;
                            }

                        }
                        else if (mealPlanChoice instanceof  Ingredient) {

                            Ingredient ingredient = (Ingredient) mealPlanChoice;

                            switch (viewModel.editType) {

                                case ("breakfast"):
                                    viewModel.mealPlan.setBreakfastRecipe(null);
                                    viewModel.mealPlan.setBreakfastIngredient(ingredient);
                                    viewModel.mealPlan.setBreakfastServings(servings);
                                    break;

                                case ("lunch"):
                                    viewModel.mealPlan.setLunchRecipe(null);
                                    viewModel.mealPlan.setLunchIngredient(ingredient);
                                    viewModel.mealPlan.setLunchServings(servings);
                                    break;

                                case ("dinner"):
                                    viewModel.mealPlan.setDinnerRecipe(null);
                                    viewModel.mealPlan.setDinnerIngredient(ingredient);
                                    viewModel.mealPlan.setDinnerServings(servings);
                                    break;
                            }

                        }

                        viewModel.updateMealPlan(viewModel.mealPlan, viewModel.mealPlan.getDate());
                        setBreakfast();
                        setLunch();
                        setDinner();
                    }
                }
            }
        });
    }

    /**
     * This method gets the meal plan when this intent is called
     */
    public void getData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewModel.mealPlan = (MealPlan) extras.getSerializable("meal");
        }
    }

    /**
     * uses the meal plan date to set the date
     */
    public void setDate(){
        LocalDate currentDate = LocalDate.parse(viewModel.mealPlan.getDate());
        String month = currentDate.getMonth().toString();
        String day = Integer.toString(currentDate.getDayOfMonth());
        String year = Integer.toString(currentDate.getYear());
        String fullDate = month + " " + day + "," + year;
        SpannableString content = new SpannableString(fullDate);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        date.setText(content);
    }

    /**
     * Set the breakfast view
     */
    public void setBreakfast(){
        breakfastImage.setImageResource(R.mipmap.camera);
        if ( viewModel.mealPlan.getBreakfastRecipe() != null){
            Recipe breakfast = viewModel.mealPlan.getBreakfastRecipe();
            breakfastName.setText(breakfast.getTitle());
            prepTimeBreakfast.setText(breakfast.getPrepTime());
            servingsBreakfast.setText(breakfast.getServings());
            if(breakfast.getImage() != null){
                breakfastImage.setImageBitmap(StringToBitMap(breakfast.getImage()));
            }

        } else if (viewModel.mealPlan.getBreakfastIngredient() != null) {
            Ingredient breakfast = viewModel.mealPlan.getBreakfastIngredient();
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

    /**
     * Set the lunch view
     */
    public void setLunch(){
        lunchImage.setImageResource(R.mipmap.camera);
        if ( viewModel.mealPlan.getLunchRecipe() != null){
            Recipe lunch = viewModel.mealPlan.getLunchRecipe();
            lunchName.setText(lunch.getTitle());
            prepTimeLunch.setText(lunch.getPrepTime());
            servingsLunch.setText(lunch.getServings());
            if(lunch.getImage() != null){
                lunchImage.setImageBitmap(StringToBitMap(lunch.getImage()));
            }
        } else if (viewModel.mealPlan.getLunchIngredient() != null) {
            Ingredient lunch = viewModel.mealPlan.getLunchIngredient();
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
    /**
     * Set the dinner view
     */
    public void setDinner(){
        dinnerImage.setImageResource(R.mipmap.camera);
        if ( viewModel.mealPlan.getDinnerRecipe() != null){
            Recipe dinner = viewModel.mealPlan.getDinnerRecipe();
            dinnerName.setText(dinner.getTitle());
            prepTimeDinner.setText(dinner.getPrepTime());
            servingsDinner.setText(dinner.getServings());
            if(dinner.getImage() != null){
                dinnerImage.setImageBitmap(StringToBitMap(dinner.getImage()));
            }

        } else if (viewModel.mealPlan.getDinnerIngredient() != null) {
            Ingredient dinner = viewModel.mealPlan.getDinnerIngredient();
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

    public void onBreakfastClick(){
        breakfastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.editType = "breakfast";

                if ( viewModel.mealPlan.getBreakfastRecipe() != null){
                    Recipe breakfast = viewModel.mealPlan.getBreakfastRecipe();
                    // Scale the recipe here or similar occurances

                    ViewRecipeDialogFragment.newInstance(breakfast).show(getSupportFragmentManager(), "VIEW_RECIPE");

                } else if (viewModel.mealPlan.getBreakfastIngredient() != null) {
                    Ingredient breakfast = viewModel.mealPlan.getBreakfastIngredient();
                    ViewIngredientDialogFragment.newInstance(breakfast).show(getSupportFragmentManager(), "VIEW_INGREDIENT");

                } else {
                    AddMealPlanDialogFragment.newInstance(viewModel.mealPlan,"breakfast").show(getSupportFragmentManager(), "ADD_MEAL");
                }

            }
        });
    }

    public void onLunchClick(){
        lunchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.editType = "lunch";

                if ( viewModel.mealPlan.getLunchRecipe() != null){
                    Recipe lunch = viewModel.mealPlan.getLunchRecipe();
                    ViewRecipeDialogFragment.newInstance(lunch).show(getSupportFragmentManager(), "VIEW_RECIPE");

                } else if (viewModel.mealPlan.getLunchIngredient() != null) {
                    Ingredient lunch = viewModel.mealPlan.getLunchIngredient();
                    ViewIngredientDialogFragment.newInstance(lunch).show(getSupportFragmentManager(), "VIEW_INGREDIENT");

                } else {
                    AddMealPlanDialogFragment.newInstance(viewModel.mealPlan,"lunch").show(getSupportFragmentManager(), "ADD_MEAL");
                }
            }
        });
    }

    public void onDinnerClick(){
        dinnerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                viewModel.editType = "dinner";

                if ( viewModel.mealPlan.getDinnerRecipe() != null){
                    Recipe dinner = viewModel.mealPlan.getDinnerRecipe();
                    ViewRecipeDialogFragment.newInstance(dinner).show(getSupportFragmentManager(), "VIEW_RECIPE");

                } else if (viewModel.mealPlan.getDinnerIngredient() != null) {
                    Ingredient dinner = viewModel.mealPlan.getDinnerIngredient();
                    ViewIngredientDialogFragment.newInstance(dinner).show(getSupportFragmentManager(), "VIEW_INGREDIENT");

                } else {
                    AddMealPlanDialogFragment.newInstance(viewModel.mealPlan,"dinner").show(getSupportFragmentManager(), "ADD_MEAL");
                }
            }
        });
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

    @Override
    public void onEdit(Recipe recipe) {
        Intent intent = new Intent(this, UpdateMealPlanActivity.class);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onDelete(Recipe recipe) {
        // TODO: remove delete button
    }

    @Override
    public void onEdit(Ingredient ingredient) {
        Intent intent = new Intent(this, UpdateMealPlanActivity.class);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onDelete(Ingredient ingredient) {
        // TODO: remove delete button
    }

    @Override
    public void onAddMeal(MealPlan mealplan, String mealType) {
        // mealType is either "breakfast", "lunch", "dinner"
        Intent intent = new Intent(this, UpdateMealPlanActivity.class);
        // intent.putExtra("EDIT_CODE", 1);
        intent.putExtra("mealPlan", viewModel.mealPlan);
        activityResultLauncher.launch(intent);
    }
}