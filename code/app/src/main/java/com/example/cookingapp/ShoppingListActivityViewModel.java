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

public class ShoppingListActivityViewModel extends ViewModel {
    final String TAG = "ShoppingListActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<ShoppingListItem>> shoppingList;

    public LiveData<ArrayList<ShoppingListItem>> getShoppingIngredients() {
        if (shoppingList == null) {
            shoppingList = new MutableLiveData<>();
            loadShoppingList();
        }
        return shoppingList;
    }

    private void loadShoppingList() {
        // fetch from db
        db = FirebaseFirestore.getInstance();
        db.collection("ShoppingIngredients").get()
                .addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<ShoppingListItem> query = new ArrayList<>(queryDocumentSnapshots.toObjects(ShoppingListItem.class));
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

    /**
     * This method adds a ingredient to the data base
     *
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
                        Log.d(TAG, "Data added successfully with ID" + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added");
                    }
                });
    }

}
