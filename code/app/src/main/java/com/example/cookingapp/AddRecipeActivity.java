package com.example.cookingapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * <p>
 * This is the add recipe activity class. It is the activity responsible for adding new recipes :)
 * </p>
 */
public class AddRecipeActivity extends AppCompatActivity {
    ImageView image;
    EditText title, servings, comments;
    ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>(); //TODO: implement the ingredient list
    ListView ingredientList;
    NumberPicker hourPicker, minPicker;
    Spinner categorySpinner;
    ArrayAdapter categorySpinnerAdapter;
    ActivityResultLauncher<String> getImage;
    Button confirm;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // initialize variables
        // TODO: make it so that they cannot click submit without all values being filled
        title = findViewById(R.id.add_recipe_title_editText);
        servings = findViewById(R.id.add_recipe_servings_editText);
        comments = findViewById(R.id.add_recipe_comments_editText);
        ingredientList = findViewById(R.id.recipe_ingredient_list);
        image = findViewById(R.id.add_recipe_imageView);
        minPicker = findViewById(R.id.min_numberPicker);
        hourPicker = findViewById(R.id.hour_numberPicker);
        categorySpinner = findViewById(R.id.add_recipe_category_spinner);
        confirm = findViewById(R.id.add_recipe_confirm_button);
        image.setClickable(true);

        title.addTextChangedListener(addRecipeTextWatcher);
        servings.addTextChangedListener(addRecipeTextWatcher);

        initCategorySpinner();
        initNumberPickers();
        onImageClick();
    }

    /**
     * <p>
     * Once the user is done filling out the form and hits confirm the information is used
     * to create a new Recipe and sends the info back to the recipe activity
     * </p>
     * @param view
     */
    public void onAddRecipeConfirm(View view){
        // This method is called when the confirm button has been clicked
        String recipeTitle = title.getText().toString();
        String recipeServings = servings.getText().toString();
        String recipeComments = comments.getText().toString();
        String recipeCategory = categorySpinner.getSelectedItem().toString();
        int prepTimeHours = hourPicker.getValue();
        int prepTimeMinutes = minPicker.getValue();
        String recipePrepTime = String.valueOf(prepTimeHours) + " hrs " + String.valueOf(prepTimeMinutes) + " min";
        String recipeImageBitMap = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            recipeImageBitMap = BitMapToString(scaled);
        } catch (Exception e) {
            System.out.println("something went wrong"); // TODO: change this into an error log
        }

        Recipe recipe = new Recipe(recipeTitle,recipeServings,recipeCategory,recipeComments,recipePrepTime,ingredients,recipeImageBitMap);
        Intent intent = new Intent(this, RecipesActivity.class);
        intent.putExtra("recipe", recipe);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * <p>
     * Turns a bitmap into a string
     * </p>
     * @author https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     * @param bitmap: the bit map of the chosen image
     * @return string
     */
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * <p>
     * This method initializes the category spinner
     * </p>
     */
    private void initCategorySpinner() {
        // TODO: add ability for user to add categories to list/adapter
        ArrayList<String> locations = new ArrayList<String>() {{
            add("Breakfast");
            add("Lunch");
            add("Dinner");
            add("Snack");
        }};
        categorySpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, locations);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setSelection(categorySpinnerAdapter.getCount());
    }

    /**
     * <p>
     * This method sets the number pickers for the preparation time. One for hours and another
     * for minutes.
     * </p>
     */
    private void initNumberPickers() {
        minPicker.setMaxValue(59);
        minPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        hourPicker.setMinValue(0);
    }

    /**
     * <p>
     * This method is called when the user clicks to choose an image. It redirects them to the
     * images on their phone where they can then choose an Image. Once the Image is chosen the
     * user is redirected to the add recipe form and the image will be shown.
     * shown on the addRecipe form.
     * </p>
     */
    private void onImageClick() {

        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                    image.setImageURI(result);
                    imageUri = result;
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage.launch("image/*");
            }
        });
    }

    /**
     * <p>
     * This method is used to ensure certain fields get filled before the user can click confirm
     * https://gist.github.com/codinginflow/52db7d909c179e9e175dd8da322cb79e
     * </p>
     */
    private TextWatcher addRecipeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = title.getText().toString().trim();
            String passwordInput = servings.getText().toString().trim();

            confirm.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}