package com.example.cookingapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.HashMap;

/**
    Purpose: saves state of ingredients list when app orientation changes and when returning to Ingredient Activity

    design rationale:
        Attributes:
            ingredients: MutableLiveData<ArrayList<Ingredient>> list of ingredients
        Methods:
            loadIngredients - Retrieves ingredients from firebase database
            getIngredients - returns ingredients list
            addIngredient - add given ingredient to firebase db
            editIngredient - edit ingredient at given location in list to firebase db
            deleteIngredient - delete given ingredient and update firebase db

    outstanding issues: none
*/
public class IngredientsActivityViewModel extends ViewModel {

    final String TAG = "IngredientsActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<Ingredient>> ingredients;

    /**
     * Returns LiveData list of ingredients
     * @return ingredients LiveData list of ingredients
     */
    public LiveData<ArrayList<Ingredient>> getIngredients() {
        if(ingredients == null) {
            ingredients = new MutableLiveData<>();
            loadIngredients();
        }
        return ingredients;
    }
    /**
     * Retrieves ingredients from firebase database and assigns ingredients list to
     * MutableLiveData<ArrayList<Ingredient>> ingredients.
     */
    private void loadIngredients() {
        // fetch from db
        db = FirebaseFirestore.getInstance();
        db.collection("Ingredients").get()
                .addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Ingredient> query = new ArrayList<>(queryDocumentSnapshots.toObjects(Ingredient.class));
                        ingredients.setValue(query);
                        Log.d(TAG, "Data retrieved successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be retrieved");
                    }
                });
    }

    /**
     * adds given ingredient to firebase database
     *
     * @param ingredient ingredient to add to db
     *
     */
    public void addIngredient(@NonNull Ingredient ingredient) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("description", ingredient.getDescription());
        data.put("bestBeforeDate", new Timestamp(Date.from(ingredient.getBestBeforeDate().atStartOfDay(ZoneOffset.UTC).toInstant())));
        data.put("location", ingredient.getLocation());
        data.put("amount", ingredient.getAmount());
        data.put("unit", ingredient.getUnit());
        data.put("category", ingredient.getCategory());
        db = FirebaseFirestore.getInstance();
        db.collection("Ingredients").add(data)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                ingredient.setDocumentId(documentReference.getId());
                ArrayList<Ingredient> updated = ingredients.getValue();
                updated.add(ingredient);
                ingredients.setValue(updated);
                Log.d(TAG, "Data added successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Data failed to be added");
            }
        });
    }

    /**
     * edits ingredient to given ingredient attributes at given position in the list
     * and update value in firebase database
     *
     * @param ingredient ingredient to take edit properties from
     * @param position position of ingredient to edit in the list
     *
     */
    public void editIngredient(@NonNull Ingredient ingredient, int position) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("description", ingredient.getDescription());
        data.put("bestBeforeDate", new Timestamp(Date.from(ingredient.getBestBeforeDate().atStartOfDay(ZoneOffset.UTC).toInstant())));
        data.put("location", ingredient.getLocation());
        data.put("amount", ingredient.getAmount());
        data.put("unit", ingredient.getUnit());
        data.put("category", ingredient.getCategory());

        ArrayList<Ingredient> updated = ingredients.getValue();
        Ingredient oldIngredient = updated.get(position);
        String id = oldIngredient.getDocumentId();

        db = FirebaseFirestore.getInstance();
        db.collection("Ingredients").document(id).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Remember to setup the document id for the new ingredient
                        // or you will get a null document id the next time
                        ingredient.setDocumentId(id); //
                        ArrayList<Ingredient> updated = ingredients.getValue();
                        updated.set(position, ingredient);
                        ingredients.setValue(updated);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    /**
     * delete ingredient in firebase database
     *
     * @param ingredient ingredient to delete
     *
     */
    public void deleteIngredient(@NonNull Ingredient ingredient) {
        db = FirebaseFirestore.getInstance();
        db.collection("Ingredients").document(ingredient.getDocumentId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ArrayList<Ingredient> updated = ingredients.getValue();
                        updated.remove(ingredient);
                        ingredients.setValue(updated);
                        Log.d(TAG, "Data deleted successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be deleted");
                    }
                });
    }

}
