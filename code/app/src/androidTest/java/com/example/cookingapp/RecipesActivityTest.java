package com.example.cookingapp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
     */
    @Test
    public void addRecipeTest(){
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

        // delete recipe
        solo.clickOnView(recipeList.getChildAt(0));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.clickOnView(solo.getView(android.R.id.button2));

        // check delete recipe
        recipe = (Recipe) recipeList.getItemAtPosition(0);
        assertNotEquals("addRecipeTest", recipe.getTitle());
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
