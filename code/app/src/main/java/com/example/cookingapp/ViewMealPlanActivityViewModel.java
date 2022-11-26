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

    /*
    public void addMealPlan(@NonNull MealPlan mealPlan, String documentID) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("description", ingredient.getDescription());
        data.put("bestBeforeDate", new Timestamp(Date.from(ingredient.getBestBeforeDate().atStartOfDay(ZoneOffset.UTC).toInstant())));
        data.put("location", ingredient.getLocation());
        data.put("amount", ingredient.getAmount());
        data.put("unit", ingredient.getUnit());
        data.put("category", ingredient.getCategory());
        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").document(documentID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    */
}
