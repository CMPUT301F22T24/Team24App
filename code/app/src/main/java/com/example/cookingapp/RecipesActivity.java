package com.example.cookingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <p>
 * This is the recipe activity class. Where the user can view all their recipes, choose to delete
 * add or edit a recipe.
 * </p>
 */
public class RecipesActivity extends AppCompatActivity implements ViewRecipeDialogFragment.OnFragmentInteractionListener {
    /**
     * Recipes Activity variables
     */
    private final int EDIT_OK = 1;

    ListView recipeListView;
    ArrayList<Recipe> recipeList;
    Spinner sortBySpinner;
    CustomSpinnerAdapter sortBySpinnerAdapter;
    RecipeAdapter recipeAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    RecipeActivityViewModel viewModel;

    int position = -1;

    /**
     * onCreate allows users to see a list of recipes.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // initialize the array adapter for the list of recipes
        recipeListView = findViewById(R.id.recipe_list);
        recipeList = new ArrayList<>();;
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeListView.setAdapter(recipeAdapter);
        initSortBySpinner();


        // TODO: handle case when no connection to db (loading state / display error)
        viewModel = new ViewModelProvider(this).get(RecipeActivityViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                if (recipes != null) {
                    recipeList = recipes;
                    recipeAdapter = new RecipeAdapter(getApplicationContext(),recipes);
                    recipeListView.setAdapter(recipeAdapter);
                }
                recipeAdapter.notifyDataSetChanged();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
                    if (recipe != null) {
                        viewModel.addRecipe(recipe);
                    }
                }
                if (result.getResultCode() == EDIT_OK) {
                    Intent intent = result.getData();
                    Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
                    if (recipe != null) {
                        viewModel.editRecipe(recipe, position);
                    }
                }

            }
        });
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i; // for use in editing
                ViewRecipeDialogFragment.newInstance(recipeList.get(i)).show(getSupportFragmentManager(), "VIEW_RECIPE");
            }//onItemClick
        });
    }

    /**
     * <p>
     * When the 'add recipe' button is clicked this method will redirect the customer to the
     * add recipe activity
     * </p>
     * @param view
     */
    public void onAddRecipeClick(View view) {
        // when the add button is clicked the user is redirected to the add recipe screen
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("ADD_CODE", 0);
        activityResultLauncher.launch(intent);
    }

    /**
     * onEdit allows users to edit recipe.
     * @param recipe
     */
    public void onEdit(Recipe recipe) {
        // TODO: edit activity
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("EDIT_CODE", 1);
        intent.putExtra("recipe", recipe);
        activityResultLauncher.launch(intent);
    }

    /**
     * onDelete allows users to delete recipe.
     * @param recipe
     */
    public void onDelete(Recipe recipe) {
        // Confirm user wants to delete ingredient
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle("Delete " + recipe.getTitle()+ "?")
                .setMessage("You will not be able to undo this action!")
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  delete and update list
                                viewModel.deleteRecipe(recipe);
                            }
                        })
                .setPositiveButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        // to change color
        // dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }
    /**
     * <p>
     * Initializes the sort spinner
     * </p>
     */
    private void initSortBySpinner() {
        sortBySpinner = findViewById(R.id.recipes_sortBy_spinner);
        ArrayList<String> sortBy = new ArrayList<String>() {{
            add("Title");
            add("Preparation Time");
            add("Servings");
            add("Category");
            add("");
        }};


        sortBySpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, sortBy);
        sortBySpinner.setAdapter(sortBySpinnerAdapter);
        sortBySpinner.setSelection(sortBySpinnerAdapter.getCount());

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                switch (selection) {
                    case "Preparation Time":
                        Collections.sort(recipeList, Recipe.RecipePreparationTimeComparator);
                        break;
                    case "Servings":
                        Collections.sort(recipeList, Recipe.RecipeServingsComparator);
                        break;
                    case "Category":
                        Collections.sort(recipeList, Recipe.RecipeCategoryComparator);
                        break;
                    default:
                        Collections.sort(recipeList, Recipe.RecipeTitleComparator);
                        break;
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });


    }//initSortBySpinner

}