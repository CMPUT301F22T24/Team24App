package com.example.cookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateMealPlanActivity extends AppCompatActivity {

    UpdateMealPlanActivityViewModel viewModel;

    ListView mealPlanListView;
    ArrayList<MealPlanChoice> mealPlanChoices = new ArrayList<>();
    Spinner sortBySpinner;
    CustomSpinnerAdapter sortBySpinnerAdapter;
    MealPlanChoiceAdapter mealPlanChoiceAdapter;
    EditText servingsEditText;
    // ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal_plan);

        servingsEditText = findViewById(R.id.update_mealPlan_servings_editText);

        mealPlanListView = findViewById(R.id.update_mealPlan_list);

        mealPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (servingsEditText.getText().toString().isEmpty()) {
                    Toast.makeText(UpdateMealPlanActivity.this, "servings input field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMealPlanActivity.this)
                        .setMessage("Confirm Selection?")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getBaseContext(), ViewMealPlanActivity.class);
                                intent.putExtra("mealPlanChoice", mealPlanChoices.get(position));
                                intent.putExtra("numServingsSelected", Integer.parseInt(servingsEditText.getText().toString()));
                                // Have to return servings in here
                                // Have to intent.putExtra(servings)
                                setResult(RESULT_OK, intent);

                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    builder.create().show();
                }
            }
        });


        viewModel = new ViewModelProvider(this).get(UpdateMealPlanActivityViewModel.class);
        viewModel.getMealPlanChoices().observe(this, new Observer<ArrayList<MealPlanChoice>>() {
            @Override
            public void onChanged(ArrayList<MealPlanChoice> choices) {
                if (mealPlanChoices != null) {
                    mealPlanChoices = choices;
                    mealPlanChoiceAdapter = new MealPlanChoiceAdapter(getApplicationContext(), mealPlanChoices);
                    mealPlanListView.setAdapter(mealPlanChoiceAdapter);
                }
                mealPlanChoiceAdapter.notifyDataSetChanged();
            }
        });

    }
}