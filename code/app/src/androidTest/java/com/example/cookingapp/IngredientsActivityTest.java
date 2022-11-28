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
 * Test class for Ingredients Activity. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class IngredientsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<IngredientsActivity> rule =
            new ActivityTestRule<>(IngredientsActivity.class, true, true);

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
     * Add a ingredient to the listview and check the recipe title using assertTrue
     * then deletes the ingredient
     */
    @Test
    public void addAndDeleteIngredientTest(){
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", IngredientsActivity.class);
        solo.sleep(2000);
        // click add ingredient button
        FloatingActionButton msButton = (FloatingActionButton) solo.getView(
                R.id.ingredient_add_button);
        solo.clickOnView(msButton);
        solo.assertCurrentActivity("Wrong Activity", AddIngredientActivity.class);

        // fill out the title and servings text views and click submit button
        EditText descriptionTextView = (EditText) solo.getView(R.id.add_ingredient_description_editText);
        EditText amountTextView = (EditText) solo.getView(R.id.add_ingredient_amount_editText);
        Button dateButton = (Button) solo.getView(R.id.add_ingredient_select_bestBeforeDate_button);
        Button confirmButton = (Button) solo.getView(R.id.add_ingredient_confirm_button);
        solo.enterText(descriptionTextView, "addIngredientTest");
        solo.enterText(amountTextView, "2");
        solo.pressSpinnerItem(0, 0);
        solo.pressSpinnerItem(1, 0);
        solo.pressSpinnerItem(2, 0);
        solo.clickOnView(dateButton);
        solo.setDatePicker(0, 2023, 11,11);
        solo.clickOnText("OK");
        solo.clickOnView(confirmButton);
        solo.assertCurrentActivity("Wrong Activity", AddIngredientActivity.class);
        assertTrue(solo.waitForText("addIngredientTest", 1, 2000));
        // check that the recipe is seen on the screen
//        // check that the recipe has been added to the list
        IngredientsActivity activity = (IngredientsActivity) solo.getCurrentActivity();
        final ListView ingredientList = activity.ingredientListView;
        Ingredient ingredient = (Ingredient) ingredientList.getItemAtPosition(ingredientList.getCount() - 1);
        assertEquals("addIngredientTest", ingredient.getDescription());
        int countBefore = ingredientList.getCount();
        // delete recipe
        int childCount = ingredientList.getChildCount();
        solo.clickOnView(ingredientList.getChildAt(childCount - 1));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.sleep(2000);
        // check delete recipe
        assertEquals(countBefore - 1, ingredientList.getCount());
    }



    /**
     * Add a ingredient, edit the description, and assert the new description is in the list.
     */
    @Test
    public void addAndEditIngredientTest() {
        // check we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", IngredientsActivity.class);
        solo.sleep(2000);
        // click add ingredient button
        FloatingActionButton msButton = (FloatingActionButton) solo.getView(
                R.id.ingredient_add_button);
        solo.clickOnView(msButton);
        solo.assertCurrentActivity("Wrong Activity", AddIngredientActivity.class);

        EditText descriptionTextView = (EditText) solo.getView(R.id.add_ingredient_description_editText);
        EditText amountTextView = (EditText) solo.getView(R.id.add_ingredient_amount_editText);
        Button dateButton = (Button) solo.getView(R.id.add_ingredient_select_bestBeforeDate_button);
        Button confirmButton = (Button) solo.getView(R.id.add_ingredient_confirm_button);
        solo.enterText(descriptionTextView, "addIngredientTest");
        solo.enterText(amountTextView, "2");
        solo.pressSpinnerItem(0, 0);
        solo.pressSpinnerItem(1, 0);
        solo.pressSpinnerItem(2, 0);
        solo.clickOnView(dateButton);
        solo.setDatePicker(0, 2023, 11,11);
        solo.clickOnText("OK");
        solo.clickOnView(confirmButton);
        solo.assertCurrentActivity("Wrong Activity", AddIngredientActivity.class);
        assertTrue(solo.waitForText("addIngredientTest", 1, 2000));
        // check that the recipe is seen on the screen
        IngredientsActivity activity = (IngredientsActivity) solo.getCurrentActivity();
        final ListView ingredientList = activity.ingredientListView;
        Ingredient ingredient = (Ingredient) ingredientList.getItemAtPosition(ingredientList.getCount() - 1);
        assertEquals("addIngredientTest", ingredient.getDescription());
        // edit ingredient
        int childCount = ingredientList.getChildCount();
        solo.clickOnView(ingredientList.getChildAt(childCount - 1));
        solo.clickOnButton("Edit");
        solo.sleep(500);
        descriptionTextView = (EditText) solo.getView(R.id.add_ingredient_description_editText);
        solo.clearEditText(descriptionTextView);
        solo.enterText(descriptionTextView, "editIngredientTest");
        confirmButton = (Button) solo.getView(R.id.add_ingredient_confirm_button);
        solo.clickOnView(confirmButton);
        // check that the ingredient has been edited
        solo.scrollToBottom();
        solo.assertCurrentActivity("Wrong Activity", IngredientsActivity.class);
        assertTrue(solo.waitForText("editIngredientTest", 1, 2000));
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
