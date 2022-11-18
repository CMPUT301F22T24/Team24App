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

    public LiveData<ArrayList<MealPlan>> getMealPlan(String day) {
        if (mealPlans == null) {
            mealPlans = new MutableLiveData<>();
            loadMealPlan(day);
        }
        return mealPlans;
    }

    /**
     * <p>
     * This method loads in everything from the data base, maps each document into a meal plan class
     * and stores them in an array list
     * </p>
     */
    // https://stackoverflow.com/questions/51892766/android-firestore-convert-array-of-document-references-to-listpojo
    private void loadMealPlan(String day) {
        // fetch from db
        db = FirebaseFirestore.getInstance();
        db.collection("MealPlan").document(day).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<MealPlan> query = new ArrayList<>();
                        Map<String, Object> week = (Map<String, Object>) documentSnapshot.getData();
                        for (Map.Entry<String, Object> day : week.entrySet()) {
                            Map<String, DocumentReference> referenceMap = (Map<String, DocumentReference>) day.getValue();

                            final Recipe[] breakfastRecipe = {null};
                            final Recipe[] lunchRecipe = {null};
                            final Recipe[] dinnerRecipe = {null};

                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            for (Map.Entry<String, DocumentReference> referenceEntry : referenceMap.entrySet()) {
                                String field = referenceEntry.getKey();
                                DocumentReference reference = referenceEntry.getValue();
                                if (reference == null)
                                    continue;

                                Task<DocumentSnapshot> documentSnapshotTask = reference.get();
                                tasks.add(documentSnapshotTask);
                                documentSnapshotTask.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        // System.out.println(documentSnapshot.toObject(Recipe.class));
                                        switch (field) {
                                            case "breakfastRecipe":
                                                breakfastRecipe[0] = documentSnapshot.toObject(Recipe.class);
                                                break;
                                            case "lunchRecipe":
                                                lunchRecipe[0] = documentSnapshot.toObject(Recipe.class);
                                                break;
                                            case "dinnerRecipe":
                                                dinnerRecipe[0] = documentSnapshot.toObject(Recipe.class);
                                                break;
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> objects) {
                                    System.out.println("reee");
                                    MealPlan mealPlan = new MealPlan(day.getKey(), breakfastRecipe[0], lunchRecipe[0], dinnerRecipe[0], null, null, null);
                                    mealPlan.setDocumentId(documentSnapshot.getId());
                                    query.add(mealPlan);
                                    mealPlans.setValue(query);
                                }
                            });
                        }
                        System.out.println(query);
                        for (MealPlan m : query) {
                            System.out.println(m);
                        }

                        Log.d(TAG, "Data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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

