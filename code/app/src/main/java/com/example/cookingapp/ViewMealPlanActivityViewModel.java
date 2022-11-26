package com.example.cookingapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewMealPlanActivityViewModel extends ViewModel {

    final String TAG = "ViewMealPlanActivity";
    private FirebaseFirestore db;

    public static MealPlan mealPlan;
    public String editType;

    public void updateMealPlan(@NonNull MealPlan mealPlan, String documentID) {
        db = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        if (mealPlan.getBreakfastRecipe() != null) {
            Recipe recipe = mealPlan.getBreakfastRecipe();
            data.put("breakfast", db.document("Recipe/"+recipe.getDocumentId()));
        }
        if (mealPlan.getLunchRecipe() != null) {
            Recipe recipe = mealPlan.getLunchRecipe();
            data.put("lunch", db.document("Recipe/"+recipe.getDocumentId()));
        }
        if (mealPlan.getDinnerRecipe() != null) {
            Recipe recipe = mealPlan.getDinnerRecipe();
            data.put("dinner", db.document("Recipe/"+recipe.getDocumentId()));
        }
        if (mealPlan.getBreakfastIngredient() != null) {
            Ingredient ingredient = mealPlan.getBreakfastIngredient();
            data.put("breakfast", db.document("Ingredients/"+ingredient.getDocumentId()));
        }
        if (mealPlan.getLunchIngredient() != null) {
            Ingredient ingredient = mealPlan.getLunchIngredient();
            data.put("lunch", db.document("Ingredients/"+ingredient.getDocumentId()));
        }
        if (mealPlan.getDinnerIngredient() != null) {
            Ingredient ingredient = mealPlan.getDinnerIngredient();
            data.put("dinner", db.document("Ingredients/"+ingredient.getDocumentId()));
        }

        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").document(documentID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to update");
                    }
                });
    }

}
