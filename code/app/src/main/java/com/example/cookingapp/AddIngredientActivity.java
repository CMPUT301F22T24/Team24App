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
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddIngredientActivity extends AppCompatActivity {
    int receivedCode; // code this activity receives
    private final int EDIT_OK = 1;
    int resultCode = RESULT_OK; // default is add
    // receive 1 -> do edit instead -> set resultCode = 1 so onResult knows to do edit
    // receive nothing -> do add -> set resultCode as RESULT_OK

    private String Location_Text = "";
    private String Category_Text = "";
    private String Unit_Text = "";


    LocalDate expiryDate;
    TextView titleTextView;
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

    Button SelectLocation;
    Button SelectCategory;
    Button SelectUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        titleTextView = findViewById(R.id.add_ingredient_title_textView);
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

        SelectLocation=findViewById(R.id.new_location_button);
        SelectCategory=findViewById(R.id.new_category_button);
        SelectUnit=findViewById(R.id.new_unit_button);



        initLocationSpinner();
        initCategorySpinner();
        initUnitSpinner();

        Bundle info = getIntent().getExtras();
        receivedCode = info.getInt("EDIT_CODE"); // try to look for an edit request
        if (receivedCode == EDIT_OK) {
            // edit request received so change descriptors
            resultCode = EDIT_OK; // tell onResult that it came from edit
            Ingredient ingredient = (Ingredient) info.getSerializable("ingredient");
            titleTextView.setText("Edit Ingredient");
            descriptionEditText.setText(ingredient.getDescription());
            bestBeforeDateTextView.setText(ingredient.getBestBeforeDate().toString());
            amountEditText.setText(ingredient.getAmount().toString());


            Log.d("Yep1", String.valueOf(locationSpinnerAdapter.getPosition(ingredient.getLocation())));
            Log.d("Yep2", String.valueOf(categorySpinnerAdapter.getPosition(ingredient.getCategory())));
            Log.d("Yep3", String.valueOf(unitSpinnerAdapter.getPosition(ingredient.getLocation())));


            if(locationSpinnerAdapter.getPosition(ingredient.getLocation())!=-1){
                locationSpinner.setSelection(locationSpinnerAdapter.getPosition(ingredient.getLocation()));
            }else{
                SelectLocation.setText(ingredient.getLocation());
                Location_Text= ingredient.getLocation();

            }

            if(categorySpinnerAdapter.getPosition(ingredient.getCategory())!=-1){
                categorySpinner.setSelection(categorySpinnerAdapter.getPosition(ingredient.getCategory()));
            }else{
                SelectCategory.setText(ingredient.getCategory());
                Category_Text= ingredient.getCategory();
            }

            if(unitSpinnerAdapter.getPosition(ingredient.getUnit())!=-1){
                unitSpinner.setSelection(unitSpinnerAdapter.getPosition(ingredient.getUnit()));
            }else{
                SelectUnit.setText(ingredient.getUnit());
                Unit_Text= ingredient.getUnit();
            }

        }

    }//onCreate




    public void onSelectLocation(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your desired location");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Location_Text = input.getText().toString();
                if(Location_Text.replace(" ","").isEmpty()){
                    Toast.makeText(getApplicationContext(),"No input was given",Toast.LENGTH_SHORT).show();
                }else if(Location_Text.length() > 30){
                    Toast.makeText(getApplicationContext(),"Length must not exceed 30 characters",Toast.LENGTH_SHORT).show();
                }else{
                    SelectLocation.setText(Location_Text);
                    locationSpinner.setSelection(3);
                    isConfirmButtonEnabled();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }//onSelectLocation


    public void onSelectCategory(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your desired category");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category_Text = input.getText().toString();
                if(Category_Text.replace(" ","").isEmpty()){
                    Toast.makeText(getApplicationContext(),"No input was given",Toast.LENGTH_SHORT).show();
                }else if(Category_Text.length() > 30){
                    Toast.makeText(getApplicationContext(),"Length must not exceed 30 characters",Toast.LENGTH_SHORT).show();
                }else{
                    SelectCategory.setText(Category_Text);
                    categorySpinner.setSelection(3);
                    isConfirmButtonEnabled();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }//onSelectCategory


    public void onSelectUnit(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your desired unit");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Unit_Text = input.getText().toString();
                if(Unit_Text.replace(" ","").isEmpty()){
                    Toast.makeText(getApplicationContext(),"No input was given",Toast.LENGTH_SHORT).show();
                }else if(Unit_Text.length() > 30){
                    Toast.makeText(getApplicationContext(),"Length must not exceed 30 characters",Toast.LENGTH_SHORT).show();
                }else{
                    SelectUnit.setText(Unit_Text);
                    unitSpinner.setSelection(3);
                    isConfirmButtonEnabled();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }//onSelectUnit



    public void onConfirm(View view){

        String description = descriptionEditText.getText().toString();// String = description input
        String location = locationSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String amount = amountEditText.getText().toString();
        LocalDate bestBeforeDate = LocalDate.parse(bestBeforeDateTextView.getText().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String unit = unitSpinner.getSelectedItem().toString();


        if(location.isEmpty()){
            location=Location_Text;
        }
        if(category.isEmpty()){
            category=Category_Text;
        }
        if(unit.isEmpty()){
            unit=Unit_Text;
        }



        // return ingredient to IngredientsActivity
        Ingredient ingredient = new Ingredient(description, bestBeforeDate, location, Double.parseDouble(amount), unit, category);
        Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra("ingredient", ingredient);
        setResult(resultCode, intent);
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

                            }
                        })
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
            if(!Objects.equals((String) locationSpinner.getSelectedItem(), "") ){
                Location_Text = "";
                SelectLocation.setText("New Location");

            }
            if(!Objects.equals((String) categorySpinner.getSelectedItem(), "") ){
                Category_Text = "";
                SelectCategory.setText("New Category");

            }if(!Objects.equals((String) unitSpinner.getSelectedItem(), "") ){
                Unit_Text = "";
                SelectUnit.setText("New Unit");
            }


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
        if(location.isEmpty()){
            location=Location_Text;
        }
        String amount = amountEditText.getText().toString();
        String unit = unitSpinner.getSelectedItem().toString();
        if(unit.isEmpty()) {
            unit = Unit_Text;
        }
        String category = categorySpinner.getSelectedItem().toString();
        if(category.isEmpty()) {
            category = Category_Text;
        }


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
