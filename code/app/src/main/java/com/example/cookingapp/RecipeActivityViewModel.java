package com.example.cookingapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeActivityViewModel extends ViewModel {
    final String TAG = "RecipesActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<Recipe>> recipes;
    public LiveData<ArrayList<Recipe>> getRecipe() {
        if(recipes == null) {
            recipes = new MutableLiveData<>();
            loadRecipies();
        }
        return recipes;
    }

    private void loadRecipies() {
        // fetch from db
        db = FirebaseFirestore.getInstance();
        db.collection("Recipe").get()
                .addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Recipe> query = new ArrayList<>(queryDocumentSnapshots.toObjects(Recipe.class));
                        recipes.setValue(query);
                        Log.d(TAG, "Data retrieved successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be retrieved");
                    }
                });
    }

    public void addRecipe(@NonNull Recipe recipe) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", recipe.getTitle());
        data.put("servings", recipe.getServings());
        data.put("category", recipe.getCategory());
        data.put("comments", recipe.getComments());
        data.put("prepTime",recipe.getPrepTime());
        data.put("ingredients", recipe.getIngredients());
        data.put("image", recipe.getImage());
        db = FirebaseFirestore.getInstance();
        db.collection("Recipe").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        recipe.setDocumentId(documentReference.getId());
                        ArrayList<Recipe> updated = recipes.getValue();
                        updated.add(recipe);
                        recipes.setValue(updated);
                        Log.d(TAG, "Data added successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added");
                    }
                });
    }

    public void deleteIngredient(@NonNull Recipe recipe) {
        db = FirebaseFirestore.getInstance();
        db.collection("Ingredients").document(recipe.getDocumentId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ArrayList<Recipe> updated = recipes.getValue();
                        updated.remove(recipe);
                        recipes.setValue(updated);
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
