package com.example.cookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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


        if(description.isEmpty() || location.isEmpty() || category.isEmpty()  || amount.isEmpty()) {//if user input is empty..
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            return;


        }else if( Double.parseDouble(amount) <=0 ){//checks if user input count is zero
            Toast.makeText(this, "Inputs cannot be less than zero", Toast.LENGTH_LONG).show();
            //Log.d("Checking","2");
            return;
        }//if


        Intent intent = new Intent();//Sends user input back to MainActivity.java
        intent.putExtra("Description", description);
        intent.putExtra("Count", amount);
        intent.putExtra("Location", location);
        intent.putExtra("Category", category);
        intent.putExtra("Unit", "kg");
        Date tempdate = new Date();
        intent.putExtra("Date", tempdate);


        setResult(RESULT_OK, intent);
        finish();//End it

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