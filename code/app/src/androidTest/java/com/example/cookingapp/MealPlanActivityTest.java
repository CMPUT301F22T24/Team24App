package com.example.cookingapp;

import android.app.Activity;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for Meal PLan Activity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class MealPlanActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MealPlanActivity> rule =
            new ActivityTestRule<>(MealPlanActivity.class, true, true);
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
     * Click a a meal plan test
     */
    @Test
    public void clickMealPlanTest(){
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
        solo.sleep(2000);

        //click on a list item
        MealPlanActivity activity = (MealPlanActivity) solo.getCurrentActivity();
        final ListView mealPlanList = activity.mealPlanListView;
        solo.clickOnView(mealPlanList.getChildAt(0));
        solo.assertCurrentActivity("Wrong Activity", ViewMealPlanActivity.class);
    }
    /**
     * Check scaling in mealplan works for ingredients
     */

    /**
     * Check scaling in meal plan works for recipes
     * Should check the servings of the recipes
     * and the servings of ingredients within the recipe
     */
}
