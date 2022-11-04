package com.example.cookingapp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for Recipes Activity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<RecipesActivity> rule =
            new ActivityTestRule<>(RecipesActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Add a recipe to the listview and check the recipe title using assertTrue
     * then deletes the recipe
     */
    @Test
    public void addAndDeleteRecipeTest(){
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", RecipesActivity.class);
        solo.sleep(2000);
        // click add recipe button
        FloatingActionButton msButton = (FloatingActionButton) solo.getView(
                R.id.recipe_add_button);
        solo.clickOnView(msButton);
        solo.assertCurrentActivity("Wrong Activity", AddRecipeActivity.class);

        // fill out the title and servings text views and click submit button
        EditText titleTextView = (EditText) solo.getView(R.id.add_recipe_title_editText);
        EditText servingsTextView = (EditText) solo.getView(R.id.add_recipe_servings_editText);
        Button submitButton = (Button) solo.getView(R.id.add_recipe_confirm_button);
        solo.enterText(titleTextView, "addRecipeTest");
        solo.enterText(servingsTextView, "2");
        solo.pressSpinnerItem(0, 0);
        solo.clickOnView(submitButton);
        solo.clearEditText(titleTextView);
        solo.clearEditText(servingsTextView);

        // check that the recipe is seen on the screen
        solo.assertCurrentActivity("Wrong Activity", RecipesActivity.class);
        assertTrue(solo.waitForText("addRecipeTest", 1, 2000));

        // check that the recipe has been added to the list
        RecipesActivity activity = (RecipesActivity) solo.getCurrentActivity();
        final ListView recipeList = activity.recipeListView;
        Recipe recipe = (Recipe) recipeList.getItemAtPosition(recipeList.getCount() - 1);
        assertEquals("addRecipeTest", recipe.getTitle());
        int countBefore = recipeList.getCount();
        // delete recipe
        int childCount = recipeList.getChildCount();
        solo.clickOnView(recipeList.getChildAt(childCount - 1));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.sleep(2000);
        // check delete recipe
        assertEquals(countBefore - 1, recipeList.getCount());
    }

    /**
     * Add a recipe, edit the title, and assert the new title is in the list.
     * Delete the recipie afterwards
     */
    @Test
    public void addAndEditRecipeTest() {
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", RecipesActivity.class);
        solo.sleep(2000);
        // click add recipe button
        FloatingActionButton msButton = (FloatingActionButton) solo.getView(
                R.id.recipe_add_button);
        solo.clickOnView(msButton);
        solo.assertCurrentActivity("Wrong Activity", AddRecipeActivity.class);

        // fill out the title,servings, and category text views and click submit button
        EditText titleTextView = (EditText) solo.getView(R.id.add_recipe_title_editText);
        EditText servingsTextView = (EditText) solo.getView(R.id.add_recipe_servings_editText);
        Spinner categorySpinner = (Spinner) solo.getView(R.id.add_recipe_category_spinner);
        Button submitButton = (Button) solo.getView(R.id.add_recipe_confirm_button);

        solo.enterText(titleTextView, "addRecipeTest");
        solo.enterText(servingsTextView, "2");


        solo.sleep(1000);
        //EditText spinnerTextView = (EditText) solo.getView(R.id.recipe_spinner_item);
        //solo.clickOnView(spinnerTextView);
        solo.pressSpinnerItem(0, 0);
        solo.clickOnView(submitButton);
        solo.clearEditText(titleTextView);
        solo.clearEditText(servingsTextView);
        // check that the recipe is seen on the screen
        solo.assertCurrentActivity("Wrong Activity", RecipesActivity.class);
        assertTrue(solo.waitForText("addRecipeTest", 1, 2000));

        // check that the recipe has been added to the list
        RecipesActivity activity = (RecipesActivity) solo.getCurrentActivity();
        final ListView recipeList = activity.recipeListView;
        Recipe recipe = (Recipe) recipeList.getItemAtPosition(recipeList.getCount() - 1);
        assertEquals("addRecipeTest", recipe.getTitle());
        int countBefore = recipeList.getCount();
        // edit recipe

        int childCount = recipeList.getChildCount();
        solo.clickOnView(recipeList.getChildAt(childCount - 1));
        solo.clickOnButton("Edit");
        solo.sleep(500);
        //solo.scrollToTop();
        titleTextView = (EditText) solo.getView(R.id.add_recipe_title_editText);

        solo.clearEditText(titleTextView);
        solo.enterText(titleTextView, "editRecipeTest");
        submitButton = (Button) solo.getView(R.id.add_recipe_confirm_button);
        solo.clickOnView(submitButton);
        // check that the recipe has been edited
        solo.scrollToBottom();
        solo.assertCurrentActivity("Wrong Activity", RecipesActivity.class);
        assertTrue(solo.waitForText("editRecipeTest", 1, 2000));
        // delete recipe

        solo.clickOnView(recipeList.getChildAt(childCount - 1));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.sleep(2000);
        // check delete recipe
        assertEquals(countBefore - 1, recipeList.getCount());
    }
    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}