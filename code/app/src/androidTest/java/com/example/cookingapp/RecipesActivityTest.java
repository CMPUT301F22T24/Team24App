package com.example.cookingapp;
import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

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
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity", RecipesActivity.class);
        FloatingActionButton msButton = (FloatingActionButton) solo.getView(
                R.id.recipe_add_button);
        solo.clickOnView(msButton);
        solo.assertCurrentActivity("Wrong Activity", AddRecipeActivity.class);
    }
}
