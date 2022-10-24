package com.example.cookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Date;

public class AddIngredientActivity extends AppCompatActivity {
    LocalDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

    }//onCreate


    public void ConfirmAdd(View view){
        EditText textfield = (EditText) findViewById(R.id.Description_EditText);//set editText to the desired textfield
        String description = textfield.getText().toString();// String = description input

        textfield = (EditText) findViewById(R.id.Location_EditText);
        String location = textfield.getText().toString();

        textfield = (EditText) findViewById(R.id.Category_EditText);
        String category = textfield.getText().toString();


        textfield = (EditText) findViewById(R.id.Amount_EditText);
        String amount = textfield.getText().toString();


        if((description.replace(" ", "")).isEmpty() || (location.replace(" ", "")).isEmpty() || (category.replace(" ", "")).isEmpty()  || (amount.replace(" ", "")).isEmpty()) {//if user input is empty..
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;


        }else if( Double.parseDouble(amount) < 0 ){//checks if user input count is zero
            Toast.makeText(this, "Amount cannot be less than zero", Toast.LENGTH_SHORT).show();
            //Log.d("Checking","2");
            return;
        }else if(description.length() > 30){
            Toast.makeText(this, "Number of characters in description should not exceed 30", Toast.LENGTH_SHORT).show();
            return;
        }else if(category.length() > 30){
            Toast.makeText(this, "Number of characters in category should not exceed 30", Toast.LENGTH_SHORT).show();
            return;
        }else if(location.length() > 30){
            Toast.makeText(this, "Number of characters in location should not exceed 30", Toast.LENGTH_SHORT).show();
            return;
        }


        double roundToHundredth  = Math.round(Double.parseDouble(amount) * 100.0) / 100.0;//Converts an input of say "12.1234" to 12.12. Rounds up.
        amount = String.valueOf(roundToHundredth);//turn amount back into string



        // return ingredient to IngredientsActivity
        Ingredient ingredient = new Ingredient(description, LocalDate.now(), location, Double.parseDouble(amount), "kg", category);
        Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra("ingredient", ingredient);
        setResult(RESULT_OK, intent);
        finish();

    }//ConfirmAdd

    public void setDate(View view){
        currentDate = LocalDate.now();

        Button SelectDateButton;
        SelectDateButton = findViewById(R.id.Select_date_button);
        SelectDateButton.setText(currentDate.toString());

    }//setDate

    public void CancelAdd(View view){
        finish();
    }//CancelAdd

}