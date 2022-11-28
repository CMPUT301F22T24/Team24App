package com.example.cookingapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
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

/**
 * Test class for Shopping List Activity. All the UI tests are written here. Robotium test framework is
 * used
 */
@RunWith(AndroidJUnit4.class)
public class ShoppingListActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ShoppingListActivity> rule =
            new ActivityTestRule<>(ShoppingListActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Tests done shopping pops up the shopping list dialog fragment
     * There must be at least one ingredient in the shopping list to run this test
     * The dialog fragment should not allow the confirm button to be clicked
     * untill all the fields are filled out
     */
    @Test
    public void doneShoppingTest() {
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
        solo.sleep(5000);
        //click on a list item
        ShoppingListActivity activity = (ShoppingListActivity) solo.getCurrentActivity();
        final ListView shoppingList = activity.shoppingListview;
        if (shoppingList.getCount() != 0) {
            solo.clickOnView(shoppingList.getChildAt(0).findViewById(R.id.checkBox));
            solo.clickOnView(solo.getView(R.id.add_recipeIngredient_confirm_button));
            solo.sleep(2000);
            solo.clickOnView(solo.getView(android.R.id.button3));
            solo.sleep(2000);

            EditText locationEditText = (EditText) solo.getView(R.id.edit_shopping_item_fragment_location_editText);
            EditText amountEditText = (EditText) solo.getView(R.id.edit_shopping_item_fragment_amount_editText);
            solo.enterText(locationEditText, "edit shopping test");
            solo.enterText(amountEditText, "1.0");
            solo.clickOnView(solo.getView(android.R.id.button3));
        }
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
    }
}
