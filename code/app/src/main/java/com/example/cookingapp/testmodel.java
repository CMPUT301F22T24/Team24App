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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This is the class for the meal plan view model for the data base
 * </p>
 */
public class testmodel extends ViewModel {
    // TODO: write the query so that only the mealPlans between certain dates are shown

    final String TAG = "MealPlanActivity";
    private FirebaseFirestore db;


    private MutableLiveData<ArrayList<MealPlan>> mealPlans;
    private MutableLiveData<ArrayList<Ingredient>> ingredients;
    private MutableLiveData<ArrayList<ShoppingListItem>> shopping;
    private MutableLiveData<ArrayList<Ingredient>> mealIngredients;

    public LiveData<ArrayList<Ingredient>> getShopping(ArrayList<String> docIds) {

        mealPlans = new MutableLiveData<>();
        ingredients = new MutableLiveData<>();

        mealIngredients = new MutableLiveData<>();

        mealIngredients = new MutableLiveData<>();
        loadMealPlans(docIds);
        loadIngredients();

        // i have both lists
        return mealIngredients;
    }

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
     * <p>
     * This method loads in everything from the data base, maps each document into a meal plan class
     * and stores them in an array list
     * </p>
     */
    // https://stackoverflow.com/questions/51892766/android-firestore-convert-array-of-document-references-to-listpojo
    private ArrayList<MealPlan> loadMealPlans(ArrayList<String> docIds) {

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
                            final Integer[] breakfastServings = {null};
                            final Integer[] lunchServings = {null};
                            final Integer[] dinnerServings = {null};

                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {

                                if (!(entry.getValue() instanceof DocumentReference)) {

                                    switch (entry.getKey()) {

                                        case "breakfastServings":
                                            breakfastServings[0] = (Integer) Math.toIntExact((Long) entry.getValue());
                                            break;
                                        case "lunchServings":
                                            lunchServings[0] = (Integer) Math.toIntExact((Long) entry.getValue());
                                            break;
                                        case "dinnerServings":
                                            dinnerServings[0] = (Integer) Math.toIntExact((Long) entry.getValue());
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
                                db = FirebaseFirestore.getInstance();
                                db.collection("Ingredients").get()
                                        .addOnSuccessListener(new OnSuccessListener<>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                ArrayList<Ingredient> iquery = new ArrayList<>(queryDocumentSnapshots.toObjects(Ingredient.class));
                                                int len = query.size();
                                                String s = Integer.toString(len);
                                                Log.e("test", s);

                                                int i = iquery.size();
                                                String t = Integer.toString(i);
                                                Log.e("test", t);





                                                ArrayList<Ingredient> updated =  new ArrayList<>();

                                                for (MealPlan m : query) {
                                                    if (m.getBreakfastIngredient() != null) {
                                                        updated.add(m.getBreakfastIngredient());
                                                        Log.e("test", "asdjkfl;");

                                                    }
                                                }

                                                int test = updated.size();
                                                String lol   = Integer.toString(test);
                                                Log.e("test", lol);
                                                mealIngredients.setValue(updated);


                                                //        loadMealPlans(docIds);

                                                // query -- meal plan query
                                                // iquery -- ingredients query


//                                                ingredients.setValue(query);

                                                Log.d(TAG, "Data retrieved successfully");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Data failed to be retrieved");
                                            }
                                        });


                                // here
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
        return query;
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

