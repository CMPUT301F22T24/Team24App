package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This is the add recipe activity class. It is the activity responsible for adding new recipes, editing recipe.
 * </p>
 */
public class AddRecipeActivity extends AppCompatActivity {
    int receivedCode; // code this activity receives
    private final int EDIT_OK = 1;
    int resultCode = RESULT_OK; // default is add
    private final int SELECTION_OK = 2;

    TextView titleTextView;
    ImageView image;
    EditText title, servings, comments;
    NumberPicker hourPicker, minPicker;
    Spinner categorySpinner;
    ArrayAdapter categorySpinnerAdapter;
    ActivityResultLauncher<String> getImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Button add;
    Button confirm;
    Button deleteIngredientButton;
    ImageButton openGallery;
    ImageButton openCamera;
    Integer selectedIngredientPosition = null;
    Bitmap imageBitMap = null;
    //Uri imageUri = null;
    View oldSelection = null;

    ListView ingredientListView;
    ArrayList<RecipeIngredient> ingredientList;
    RecipeIngredientAdapter recipeIngredientAdapter;

    /**
     * The main activity of Recipe, allowing users to add, edit and delete recipe entries
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // initialize variables
        titleTextView = findViewById(R.id.recipe_title_textView);
        title = findViewById(R.id.add_recipe_title_editText);
        servings = findViewById(R.id.add_recipe_servings_editText);
        comments = findViewById(R.id.add_recipe_comments_editText);
        image = findViewById(R.id.recipe_Image);
        openGallery = findViewById(R.id.open_gallery);
        openCamera = findViewById(R.id.open_camera);
        minPicker = findViewById(R.id.min_numberPicker);
        hourPicker = findViewById(R.id.hour_numberPicker);
        categorySpinner = findViewById(R.id.add_recipe_category_spinner);
        add = findViewById(R.id.add_recipe_add_ingredient_button);
        deleteIngredientButton = findViewById(R.id.add_recipe_delete_ingredient_button);
        confirm = findViewById(R.id.add_recipe_confirm_button);
        confirm.setEnabled(false);

        title.addTextChangedListener(addRecipeTextWatcher);
        servings.addTextChangedListener(addRecipeTextWatcher);

        // initialize add ingredient to recipe list items
        ingredientListView = findViewById(R.id.recipe_ingredient_list);
        ingredientList = new ArrayList<>();
        recipeIngredientAdapter = new RecipeIngredientAdapter(this, ingredientList);
        ingredientListView.setAdapter(recipeIngredientAdapter);

        // The listView is inside a ScrollView. This causes errors such as only 1 item to show
        // NestedScrollView did not work so we'll use this fix instead
        //https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
        ScrollView mScrollView = findViewById(R.id.recipe_scroll_view);
        ingredientListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });


        initCategorySpinner();
        initNumberPickers();
        onImageClick();
        Bundle info = getIntent().getExtras();
        receivedCode = info.getInt("EDIT_CODE"); // try to look for an edit request

        //delete ingredients in add recipe
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if((selectedIngredientPosition == null)){
                    selectedIngredientPosition = i;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }else if((selectedIngredientPosition != i)){
                    clearSelection();
                    selectedIngredientPosition = i;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }else if((selectedIngredientPosition == i)){
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }
            }
            private void clearSelection() {
                if(oldSelection != null) {
                    oldSelection.setBackgroundColor(Color.parseColor("#ED524E"));
                }
            }
        });

        deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ingredientList.isEmpty() && selectedIngredientPosition!= null) {
                    ingredientList.remove(Integer.parseInt(selectedIngredientPosition.toString()));
                    recipeIngredientAdapter.notifyDataSetChanged();
                }
            }
        });


        if (receivedCode == EDIT_OK) {
            // edit request received so change descriptors
            resultCode = EDIT_OK; // tell onResult that it came from edit
            Recipe recipe = (Recipe) info.getSerializable("recipe");
            titleTextView.setText("Edit Recipe");
            title.setText(recipe.getTitle());
            servings.setText(recipe.getServings());

            minPicker.setValue(Integer.parseInt(recipe.getPrepTime().split("hrs")[0].strip()));
            hourPicker.setValue(Integer.parseInt(recipe.getPrepTime().split("hrs")[1].split("min")[0].strip()));
            try {
                categorySpinner.setSelection(categorySpinnerAdapter.getPosition(recipe.getCategory()));
            } catch (Error e) {

                Log.d("Add Recipe Activity", "Logging in case snack not being shown causes problems");
                Log.d("Error", String.valueOf(e));
            }
            servings.setText(recipe.getServings());
            comments.setText(recipe.getServings());

            Log.d("REACHED", "102");
            if (recipe.getImage() == null) {
                image.setImageResource(R.mipmap.camera);
            } else {
                image.setImageBitmap(StringToBitMap(recipe.getImage()));
            }
            // Handle ingredients
            if (!recipe.getIngredients().isEmpty()) {
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    ingredientList.add(recipe.getIngredients().get(i));
                }
                recipeIngredientAdapter.notifyDataSetChanged();
            }


            //delete ingredients in edit recipe
            ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if((selectedIngredientPosition == null)){
                        selectedIngredientPosition = i;
                        oldSelection = view;
                        view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                    }else if((selectedIngredientPosition != i)){
                        clearSelection();
                        selectedIngredientPosition = i;
                        oldSelection = view;
                        view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                    }else if((selectedIngredientPosition == i)){
                        oldSelection = view;
                        view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                    }
                }
                private void clearSelection() {
                    if(oldSelection != null) {
                        oldSelection.setBackgroundColor(Color.parseColor("#ED524E"));
                    }
                }
            });

            deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ingredientList.isEmpty() && selectedIngredientPosition!= null) {
                        ingredientList.remove(Integer.parseInt(selectedIngredientPosition.toString()));
                        recipeIngredientAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    if(bitmap != null){
                        image.setImageBitmap(bitmap);
                        imageBitMap = bitmap;
                    }

                    Intent intent = result.getData();
                    RecipeIngredient recipeIngredient = (RecipeIngredient) intent.getSerializableExtra("recipeIngredient");
                    if (recipeIngredient != null) {
                        // retrieved recipeIngredient successfully from addRecipeIngredient activity
                        ingredientList.add(recipeIngredient);
                        recipeIngredientAdapter.notifyDataSetChanged();
                    }
                }
                if (result.getResultCode() == 2) {
                    Intent intent = result.getData();
                    StorageSelection selection = (StorageSelection) intent.getSerializableExtra("list");;
                    // selection has the list of selected ingredients
                    for (RecipeIngredient r : selection.getIngredients()) {
                        ingredientList.add(r);
                        recipeIngredientAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void onAddOrEditRecipeConfirmHandler(View view) {
        if (resultCode == RESULT_OK) {
            onAddRecipeConfirm(view);
        } else if (resultCode == EDIT_OK) {
            onEditRecipeConfirm(view);
        }
    }

    /**
     * <p>
     * Once the user is done filling out the form and hits confirm the information is used
     * to create a new Recipe and sends the info back to the recipe activity
     * </p>
     *
     * @param view
     */
    public void onAddRecipeConfirm(View view) {
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
            int nh = (int) (imageBitMap.getHeight() * (512.0 / imageBitMap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitMap, 512, nh, true);
            recipeImageBitMap = BitMapToString(scaled);
        } catch (Exception e) {
            System.out.println("something went wrong");
        }

        Recipe recipe = new Recipe(recipeTitle, recipeServings, recipeCategory, recipeComments, recipePrepTime, ingredientList, recipeImageBitMap);
        Intent intent = new Intent(this, RecipesActivity.class);
        intent.putExtra("recipe", recipe);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onEditRecipeConfirm(View view) {
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
            int nh = (int) (imageBitMap.getHeight() * (512.0 / imageBitMap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitMap, 512, nh, true);
            recipeImageBitMap = BitMapToString(scaled);
        } catch (Exception e) {
            System.out.println("something went wrong");
        }

        Recipe recipe = new Recipe(recipeTitle, recipeServings, recipeCategory, recipeComments, recipePrepTime, ingredientList, recipeImageBitMap);
        Intent intent = new Intent(this, RecipesActivity.class);
        intent.putExtra("recipe", recipe);
        setResult(EDIT_OK, intent);
        finish();
    }

    /**
     * <p>
     * Turns a bitmap into a string
     * </p>
     *
     * @param bitmap: the bit map of the chosen image
     * @return string
     * @author https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     */
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * <p>
     * This method initializes the category spinner
     * </p>
     */
    private void initCategorySpinner() {
        ArrayList<String> locations = new ArrayList<String>() {{
            add("Breakfast");
            add("Lunch");
            add("Dinner");
            add("Snack");
            add("");
        }};
        categorySpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, locations);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setSelection(categorySpinnerAdapter.getCount());
        categorySpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    public void onSelectFromStorage(View view) {
        Intent intent = new Intent(this, SelectFromStorage.class);
        intent.putExtra("acode", 10);
        activityResultLauncher.launch(intent);
    }

    /**
     * <p>
     * Used to disable/enable confirm button based on spinner selections
     * </p>
     */
    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            isConfirmButtonEnabled();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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

    private Bitmap UriToBitMap(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        return bitmap;
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
                try {
                    imageBitMap = UriToBitMap(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage.launch("image/*");
            }
        });

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null){
                    //activityResultLauncher.launch(intent);
                    try {
                        activityResultLauncher.launch(intent);
                    } catch (Exception e) {
                        Toast.makeText(AddRecipeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Does not support this action",
                            Toast.LENGTH_SHORT).show();
                }
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
            isConfirmButtonEnabled();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void addIngredientToRecipe(View view) {
        Intent intent = new Intent(this, AddRecipeIngredient.class);
        activityResultLauncher.launch(intent);
    }

    /**
     * <p>
     * Enables the confirm button once certain fields are entered
     * </p>
     */
    private void isConfirmButtonEnabled() {
        String usernameInput = title.getText().toString().trim();
        String passwordInput = servings.getText().toString().trim();
        String categoryInput = categorySpinner.getSelectedItem().toString();
        confirm.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty() && !categoryInput.equals(""));
    }
}