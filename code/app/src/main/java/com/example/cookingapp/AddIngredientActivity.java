package com.example.cookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddIngredientActivity extends AppCompatActivity {
    LocalDate expiryDate;

    // description
    EditText descriptionEditText;
    // location
    Spinner locationSpinner;
    ArrayAdapter locationSpinnerAdapter;
    // category
    Spinner categorySpinner;
    ArrayAdapter categorySpinnerAdapter;
    // unit
    Spinner unitSpinner;
    ArrayAdapter unitSpinnerAdapter;
    // expiry date
    TextView bestBeforeDateTextView;
    Button selectExpiryDateButton;
    // amount
    EditText amountEditText;

    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        descriptionEditText = findViewById(R.id.add_ingredient_description_editText);
        descriptionEditText.addTextChangedListener(new EditTextWatcher());
        locationSpinner = findViewById(R.id.add_ingredient_location_spinner);
        categorySpinner = findViewById(R.id.add_ingredient_category_spinner);
        unitSpinner = findViewById(R.id.add_ingredient_unit_spinner);
        bestBeforeDateTextView = findViewById(R.id.add_ingredient_bestBeforeDate_textView);
        selectExpiryDateButton = findViewById(R.id.add_ingredient_select_bestBeforeDate_button);
        amountEditText = findViewById(R.id.add_ingredient_amount_editText);
        amountEditText.addTextChangedListener(new EditTextWatcher());
        amountEditText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3, 2)});
        confirmButton = findViewById(R.id.add_ingredient_confirm_button);

        initLocationSpinner();
        initCategorySpinner();
        initUnitSpinner();

    }//onCreate


    public void onConfirm(View view){

        String description = descriptionEditText.getText().toString();// String = description input
        String location = locationSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String amount = amountEditText.getText().toString();
        LocalDate bestBeforeDate = LocalDate.parse(bestBeforeDateTextView.getText().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String unit = unitSpinner.getSelectedItem().toString();

        // TODO: have warnings messaged displayed at the bottom telling user what is missing?
        /*
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
        */

        // return ingredient to IngredientsActivity
        Ingredient ingredient = new Ingredient(description, bestBeforeDate, location, Double.parseDouble(amount), unit, category);
        Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra("ingredient", ingredient);
        setResult(RESULT_OK, intent);
        finish();

    }//ConfirmAdd


    public void setDate(View view){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddIngredientActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // account for android's indexing
                LocalDate setDate = LocalDate.of(year, month+1, dayOfMonth);
                LocalDate currentDate = LocalDate.now();
                if (setDate.isBefore(currentDate) || setDate.equals(currentDate)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddIngredientActivity.this);
                    builder.setCancelable(true)
                        .setTitle("You entered an expired date")
                        .setMessage("Is this okay?")
                        .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                expiryDate = LocalDate.of(year, month+1, dayOfMonth);
                                bestBeforeDateTextView.setText(expiryDate.toString());
                            }
                        })
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectExpiryDateButton.callOnClick();
                            }
                        });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    expiryDate = LocalDate.of(year, month+1, dayOfMonth);
                    bestBeforeDateTextView.setText(expiryDate.toString());
                }
                isConfirmButtonEnabled();
            }
        }, year, month, day);
        datePickerDialog.show();

    }//setDate

    // Used to disable/enable confirm button based on edit text inputs
    private class EditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isConfirmButtonEnabled();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    // Used to disable/enable confirm button based on spinner selections
    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            isConfirmButtonEnabled();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void initLocationSpinner() {
        // TODO: add ability for user to add locations to list/adapter
        ArrayList<String> locations = new ArrayList<String>() {{
            add("Fridge");
            add("Freezer");
            add("Pantry");
            add("");
        }};
        locationSpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, locations);
        locationSpinner.setAdapter(locationSpinnerAdapter);
        locationSpinner.setSelection(locationSpinnerAdapter.getCount());
        locationSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    private void initCategorySpinner() {
        // TODO: add ability for user to add categories to list/adapter
        ArrayList<String> categories = new ArrayList<String>() {{
            add("Vegetable");
            add("Fruit");
            add("Pastry");
            add("");
        }};
        categorySpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, categories);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setSelection(categorySpinnerAdapter.getCount());
        categorySpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    private void initUnitSpinner() {
        // TODO: add ability for user to add units to list/adapter
        ArrayList<String> units = new ArrayList<String>() {{
            add("kg");
            add("lb");
            add("oz");
            add("");
        }};
        unitSpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, units);
        unitSpinner.setAdapter(unitSpinnerAdapter);
        unitSpinner.setSelection(unitSpinnerAdapter.getCount());
        unitSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    // https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
    // TODO: change to preference
    private class DecimalDigitsInputFilter implements InputFilter {
        Pattern mPattern;
        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }
    }

    // for enabling confirm button
    private void isConfirmButtonEnabled() {
        String description = descriptionEditText.getText().toString().trim();
        String date = bestBeforeDateTextView.getText().toString();
        String location = locationSpinner.getSelectedItem().toString();
        String amount = amountEditText.getText().toString();
        String unit = unitSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();

        // TODO: can amount have decimals? if so, restrict for a min amount of precision
        confirmButton.setEnabled(
            !description.isEmpty() && description.length() <= 30
            && !date.equals("yyyy-mm-dd")
            && !location.isEmpty()
            && !amount.isEmpty()
            && !unit.isEmpty()
            && !category.isEmpty()
        );
    }
}