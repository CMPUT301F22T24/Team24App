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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.local.ReferenceSet;
import com.google.firestore.v1.MapValue;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.ref.ReferenceQueue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>
 * This is the class for the meal plan view model for the data base
 * </p>
 */
public class MealPlanActivityViewModel extends ViewModel {
    // TODO: write the query so that only the mealPlans between certain dates are shown

    final String TAG = "MealPlanActivity";
    private FirebaseFirestore db;
    private MutableLiveData<ArrayList<MealPlan>> mealPlans;

    public LiveData<ArrayList<MealPlan>> getMealPlans(ArrayList<String> docIds) {

        mealPlans = new MutableLiveData<>();
        loadMealPlans(docIds);

        return mealPlans;
    }

    /**
     * <p>
     * This method loads in everything from the data base, maps each document into a meal plan class
     * and stores them in an array list
     * </p>
     */
    private void loadMealPlans(ArrayList<String> docIds) {

        // fetch from db
        db = FirebaseFirestore.getInstance();
        ArrayList<MealPlan> query = new ArrayList<>();
        mealPlans.setValue(new ArrayList<>());
        db.collection("MealPlan").whereIn(FieldPath.documentId(), docIds).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        List<Task<DocumentSnapshot>> all_tasks = new ArrayList<>();

                        for (DocumentSnapshot document: querySnapshot.getDocuments()) {
                            Map<String, Object> data = document.getData();

                            final Recipe[] breakfastRecipe = {null};
                            final Recipe[] lunchRecipe = {null};
                            final Recipe[] dinnerRecipe = {null};
                            final Ingredient[] breakfastIngredient = {null};
                            final Ingredient[] lunchIngredient = {null};
                            final Ingredient[] dinnerIngredient = {null};
                            final Double[] breakfastServings = {null};
                            final Double[] lunchServings = {null};
                            final Double[] dinnerServings = {null};

                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {

                                if (!(entry.getValue() instanceof DocumentReference)) {

                                    switch (entry.getKey()) {

                                        case "breakfastServings":
                                            breakfastServings[0] = (Double) entry.getValue();
                                            break;
                                        case "lunchServings":
                                            lunchServings[0] = (Double) entry.getValue();
                                            break;
                                        case "dinnerServings":
                                            dinnerServings[0] = (Double) entry.getValue();
                                            break;

                                    }
                                    continue;
                                }

                                DocumentReference reference = (DocumentReference) entry.getValue();

                                Task<DocumentSnapshot> documentSnapshotTask = reference.get();
                                tasks.add(documentSnapshotTask);
                                all_tasks.add(documentSnapshotTask);

                                documentSnapshotTask
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String[] refPath = reference.getPath().split("/");
                                            switch (entry.getKey()) {

                                                case "breakfast":
                                                    if (refPath[0].equals("Recipe"))
                                                        breakfastRecipe[0] = documentSnapshot.toObject(Recipe.class);
                                                    else if (refPath[0].equals("Ingredients"))
                                                        breakfastIngredient[0] = documentSnapshot.toObject(Ingredient.class);
                                                    break;
                                                case "lunch":
                                                    if (refPath[0].equals("Recipe"))
                                                        lunchRecipe[0] = documentSnapshot.toObject(Recipe.class);
                                                    else if (refPath[0].equals("Ingredients"))
                                                        lunchIngredient[0] = documentSnapshot.toObject(Ingredient.class);
                                                    break;
                                                case "dinner":
                                                    if (refPath[0].equals("Recipe"))
                                                        dinnerRecipe[0] = documentSnapshot.toObject(Recipe.class);
                                                    else if (refPath[0].equals("Ingredients"))
                                                        dinnerIngredient[0] = documentSnapshot.toObject(Ingredient.class);
                                                    break;
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Data failed to be added");
                                        }
                                    });
                            }
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> objects) {
                                    MealPlan mealPlan = new MealPlan(document.getId(), breakfastRecipe[0], lunchRecipe[0], dinnerRecipe[0],
                                            breakfastServings[0], lunchServings[0], dinnerServings[0],
                                            breakfastIngredient[0], lunchIngredient[0], dinnerIngredient[0],
                                            document.getId());
                                    // mealPlan.setDocumentId(document.getId());
                                    query.add(mealPlan);
                                }
                            });
                        }
                        Tasks.whenAllSuccess(all_tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> objects) {
                                mealPlans.setValue(query);
                            }
                        });
                        Log.d(TAG, "Data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}

