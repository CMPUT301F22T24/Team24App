package com.example.cookingapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * <p>
 * This is the class for the meal plan view model for the data base
 * </p>
 */
public class MealPlanActivityViewModel extends ViewModel {
    // TODO: write the query so that only the mealPlans between certain dates are shown

    final String TAG = "MealPlanActivity";
    private FirebaseFirestore db;
    private ArrayList<MealPlan> mealPlans;

    public ArrayList<MealPlan> getMealPlan(String day) {
        mealPlans = new ArrayList<>();
        loadMealPlan(day);
        return mealPlans;
    }

    /**
     * <p>
     * This method loads in everything from the data base, maps each document into a meal plan class
     * and stores them in an array list
     * </p>
     */
    private void loadMealPlan(String day) {
        // fetch from db
        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").whereEqualTo("date", day).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Gson gson = new Gson();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JsonElement jsonElement = gson.toJsonTree(document.getData());
                                MealPlan meal = gson.fromJson(jsonElement, MealPlan.class);
                                mealPlans.add(meal);
                            }
                            Log.d(TAG, "Data retrieved successfully");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be retrieved");
                    }
                });
    }

    /**
     * <p>
     * This method adds a mealPlan to the data base
     * @param mealPlan: mealPlan to added
     * </p>
     */
    public void addMealPlan(@NonNull MealPlan mealPlan) {

        HashMap<String, Object> data = new HashMap<>();
        data.put("date", mealPlan.getDate());
        data.put("breakfastRecipe", mealPlan.getBreakfastRecipe());
        data.put("lunchRecipe", mealPlan.getLunchRecipe());
        data.put("dinnerRecipe", mealPlan.getDinnerRecipe());
        data.put("breakfastIngredient",mealPlan.getBreakfastIngredient());
        data.put("lunchIngredient", mealPlan.getLunchIngredient());
        data.put("dinnerIngredient", mealPlan.getDinnerIngredient());

        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mealPlan.setDocumentId(documentReference.getId());
                        Log.d(TAG, "Data added successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added");
                    }
                });
    }

    public void editMealPlan(@NonNull MealPlan mealPlan, int position) {
        /*
        HashMap<String, Object> data = new HashMap<>();
        data.put("date", mealPlan.getDate());
        data.put("breakfastRecipe", mealPlan.getBreakfastRecipe());
        data.put("lunchRecipe", mealPlan.getLunchRecipe());
        data.put("dinnerRecipe", mealPlan.getDinnerRecipe());
        data.put("breakfastIngredient",mealPlan.getBreakfastIngredient());
        data.put("lunchIngredient", mealPlan.getLunchIngredient());
        data.put("dinnerIngredient", mealPlan.getDinnerIngredient());

        ArrayList<MealPlan> updated = mealPlans.getValue();
        MealPlan oldMealPlan = updated.get(position);
        String id = oldMealPlan.getDocumentId();

        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").document(id).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Remember to setup the document id for the new ingredient
                        // or you will get a null document id the next time
                        mealPlan.setDocumentId(id); //
                        ArrayList<MealPlan> updated = mealPlans.getValue();
                        updated.set(position, mealPlan);
                        mealPlans.setValue(updated);
                        Log.d(TAG, "Meal Plan Edited");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

         */

    }

    /**
     * <p>
     * This method deletes a meal plan from the data base
     * @param mealPlan: meal plan to be deleted
     * </p>
     */
    public void deleteMealPlan(@NonNull MealPlan mealPlan) {
        /*
        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").document(mealPlan.getDocumentId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ArrayList<MealPlan> updated = mealPlans.getValue();
                        updated.remove(mealPlan);
                        mealPlans.setValue(updated);
                        Log.d(TAG, "Data deleted successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be deleted");
                    }
                });

         */
    }

}

