package com.example.cookingapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingListActivityViewModel extends ViewModel {
    final String TAG = "ShoppingListActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<Ingredient>> shoppingList;
    public LiveData<ArrayList<Ingredient>> getIngredients() {
        if(shoppingList == null) {
            shoppingList = new MutableLiveData<>();
            loadIngredients();
        }
        return shoppingList;
    }

    private void loadIngredients() {
        // fetch from db
        db = FirebaseFirestore.getInstance();
        db.collection("Ingredients").get()
                .addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Ingredient> query = new ArrayList<>(queryDocumentSnapshots.toObjects(Ingredient.class));
                        shoppingList.setValue(query);
                        Log.d(TAG, "Data retrieved successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be retrieved");
                    }
                });
    }

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
                        ArrayList<Ingredient> updated = shoppingList.getValue();
                        updated.add(ingredient);
                        shoppingList.setValue(updated);
                        Log.d(TAG, "Data added successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added");
                    }
                });
    }
}
