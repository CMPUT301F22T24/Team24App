package com.example.cookingapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpdateMealPlanActivityViewModel extends ViewModel {

    final String TAG = "UpdateMealPlanActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<MealPlanChoice>> mealPlanChoices;

    /**
     * Queries database to fetch for meal plans
     * @return mealPlanChoices
     */
    public LiveData<ArrayList<MealPlanChoice>> getMealPlanChoices() {

        if(mealPlanChoices == null) {
            mealPlanChoices = new MutableLiveData<>();
            loadMealPlanChoices();
        }

        return mealPlanChoices;
    }

    /**
     * Queries database to fetch for meal plans given a set of dates
     */
    private void loadMealPlanChoices() {
        ArrayList<MealPlanChoice> query = new ArrayList<>();

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        // fetch from db
        db = FirebaseFirestore.getInstance();
        // Get all recipes
        Task<QuerySnapshot> recipeTask = db.collection("Recipe").get()
            .addOnSuccessListener(new OnSuccessListener<>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    query.addAll((queryDocumentSnapshots.toObjects(Recipe.class)));
                    mealPlanChoices.setValue(query);
                    Log.d(TAG, "Recipe data retrieved successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Recipe data failed to be retrieved");
                }
            });
        tasks.add(recipeTask);

        // Get all ingredients
        Task<QuerySnapshot> ingredientsTask = db.collection("Ingredients").get()
            .addOnSuccessListener(new OnSuccessListener<>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    query.addAll(queryDocumentSnapshots.toObjects(Ingredient.class));
                    Log.d(TAG, "Ingredients data retrieved successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Ingredients data failed to be retrieved");
                }
            });
        tasks.add(ingredientsTask);

        // set when all recipes and ingredients retrieved
        Tasks.whenAllComplete(tasks).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
            @Override
            public void onSuccess(List<Task<?>> tasks) {
                mealPlanChoices.setValue(query);
                Log.d(TAG, "Data retrieved successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Data failed to be retrieved");
            }
        });
    }

}
