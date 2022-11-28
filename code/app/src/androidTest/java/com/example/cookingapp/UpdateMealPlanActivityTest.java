package com.example.cookingapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Test class for Update Meal PLan Activity. All the UI tests are written here.
 * Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class UpdateMealPlanActivityTest {

    private Solo solo;
    @Rule
    public ActivityTestRule<UpdateMealPlanActivity> rule =
            new ActivityTestRule<>(UpdateMealPlanActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        UpdateMealPlanActivity activity = (UpdateMealPlanActivity) solo.getCurrentActivity();
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
     * Click a a meal plan choice test with no servings selected
     */
    @Test
    public void clickMealPlanChoiceNoServingsTest(){
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", UpdateMealPlanActivity.class);
        solo.sleep(2000);

        //click on a list item
        UpdateMealPlanActivity activity = (UpdateMealPlanActivity) solo.getCurrentActivity();
        final ListView mealPlanChoiceList = activity.mealPlanListView;
        // assumes something is already in the db
        View view = mealPlanChoiceList.getChildAt(0);
        if (view != null) {
            solo.clickOnView(view);
            // should not show dialog to confirm
            assertFalse("Could find the dialog!", solo.searchText("Confirm Selection?"));
        }
    }
    /**
     * Click a a meal plan choice test with servings selected
     */
    @Test
    public void clickMealPlanChoiceTest() {
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", UpdateMealPlanActivity.class);
        solo.sleep(2000);

        //click on a list item
        UpdateMealPlanActivity activity = (UpdateMealPlanActivity) solo.getCurrentActivity();
        final ListView mealPlanChoiceList = activity.mealPlanListView;
        final EditText servingsEditText = activity.servingsEditText;
        solo.enterText(servingsEditText, "2");
        // assumes something is already in the db
        View view = mealPlanChoiceList.getChildAt(0);
        if (view != null) {
            solo.clickOnView(view);
            // should show dialog to confirm
            assertTrue("Could not find the dialog!", solo.searchText("Confirm Selection?"));
        }
    }
}
