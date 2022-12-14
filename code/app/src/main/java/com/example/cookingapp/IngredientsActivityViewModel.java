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
 * <p>
 * This is the class for the ingredient view model for the data base
 * </p>
 */
public class IngredientsActivityViewModel extends ViewModel {

    final String TAG = "IngredientsActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<Ingredient>> ingredients;
    public LiveData<ArrayList<Ingredient>> getIngredients() {
        if(ingredients == null) {
            ingredients = new MutableLiveData<>();
            loadIngredients();
        }
        return ingredients;
    }
    /**
     * <p>
     * This method loads in everything from the data base, maps each document into a ingredient class
     * and stores them in an array list
     * </p>
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
     * This method adds a ingredient to the data base
     * @param ingredient
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
     * This method edits a ingredient to the data base
     * @param ingredient
     * @param position
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
     * This method deletes a ingredient to the data base.
     * @param ingredient
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
