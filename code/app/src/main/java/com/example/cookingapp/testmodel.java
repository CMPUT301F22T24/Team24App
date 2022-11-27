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
    private MutableLiveData<ArrayList<RecipeIngredient>> mealIngredients;

    public LiveData<ArrayList<Ingredient>> getShopping(ArrayList<String> docIds) {

        mealPlans = new MutableLiveData<>();
        ingredients = new MutableLiveData<>();

        mealIngredients = new MutableLiveData<>();
        loadMealPlans(docIds);
        loadIngredients();

        // i have both lists

        ArrayList<Ingredient> mealIngredients = new ArrayList<>();
        ArrayList<RecipeIngredient>shoppingStuff = new ArrayList<>();

        ArrayList<MealPlan> meals = mealPlans.getValue();




        HashMap <String, Double> ingredientStorageMap = new HashMap<String , Double>();
        HashMap <String, Double> mealPlanStorageMap = new HashMap<String ,Double>();


        for (MealPlan m : meals) {
            if (m.getLunchIngredient() != null) {
                String s = m.getLunchIngredient().getDescription();
                Log.e("test", s);

            }
        }



        //Nima's Code begins here:

        for (Ingredient x : mealIngredients){//Load ingredientStorage ingredients into hashmap. Just the description, unit and Category
            String temp = x.getDescription()  + x.getUnit()  + x.getCategory();
            ingredientStorageMap.put(temp, x.getAmount());

        }


            for (MealPlan m : meals){//Load mealPlan ingredients into second hashmap. Just the description, unit and category.
                if(m.getBreakfastIngredient() != null){
                    String temp = m.getBreakfastIngredient().getDescription()  + m.getBreakfastIngredient().getUnit()  + m.getBreakfastIngredient().getCategory();
                    mealPlanStorageMap.put(temp, m.getBreakfastIngredient().getAmount());

                }//if
                if(m.getLunchIngredient() != null){
                    String temp = m.getLunchIngredient().getDescription() + m.getLunchIngredient().getUnit()  + m.getLunchIngredient().getCategory();
                    mealPlanStorageMap.put(temp, m.getLunchIngredient().getAmount());
                }//if
                 if(m.getDinnerIngredient() != null){
                    String temp = m.getDinnerIngredient().getDescription()  + m.getDinnerIngredient().getUnit()  + m.getDinnerIngredient().getCategory();
                    mealPlanStorageMap.put(temp, m.getDinnerIngredient().getAmount());
                }//if


        }//for







        for(Map.Entry<String, Double> m : mealPlanStorageMap.entrySet()){
            for(Map.Entry<String, Double> x : ingredientStorageMap.entrySet()){
                if(x.getKey().equals( m.getKey())){//if ingredient of mealPlan == ingredient of ingredientStorage
                    if( x.getValue() > 0.0) {// if ingredient amount not zero
                        double temp = x.getValue() - m.getValue();// amount storage - amount mealPlan

                        if(temp < 0.0){// if it turns out mealPlan amount was less than storage amount
                            temp = temp * -1.0;//make positive
                            x.setValue(0.0);// set storage amount to 0
                            m.setValue(temp);// set mealplant amount to what is left

                        }else if (temp >= 0.0){//if it turns out storage amount is greater than amount mealPlan
                            x.setValue(temp);// set Mealplan amount to what is left
                            m.setValue(0.0);// set storage amount to 0

                        }

                    }

                }
            }//for
        }//for


        //By now, the subtraction is done between storage and mealPlan. you should have the mealPlan hashmap set with what is left.
        //So now we must throw the results into a new list: shoppingList




        //Setup for the following: for all mealplan, iterate through our hashmap keys to find a match. if match found, shoppingStuff.add( mealPlan ingredient ) EXCEPT WITH NEW AMOUNT.
        for(MealPlan m : meals){
            String temp = "";
            if(m.getBreakfastIngredient() != null){
                temp = m.getBreakfastIngredient().getDescription()  + m.getBreakfastIngredient().getUnit()  + m.getBreakfastIngredient().getCategory();
                for(Map.Entry<String, Double> z : mealPlanStorageMap.entrySet()){
                    String temp2 = z.getKey();
                    if(temp.contentEquals(temp2)){
                        shoppingStuff.add(new RecipeIngredient(m.getBreakfastIngredient().getDescription(), Double.toString(z.getValue()), m.getBreakfastIngredient().getUnit(), m.getBreakfastIngredient().getUnit()));
                    }//if
                }//for
            }//if
            if(m.getLunchIngredient() != null){
                temp = m.getLunchIngredient().getDescription() + m.getLunchIngredient().getUnit()  + m.getLunchIngredient().getCategory();
                for(Map.Entry<String, Double> z : mealPlanStorageMap.entrySet()){
                    String temp2 = z.getKey();
                    if(temp.contentEquals(temp2)){
                        shoppingStuff.add(new RecipeIngredient(m.getLunchIngredient().getDescription(), Double.toString(z.getValue()), m.getLunchIngredient().getUnit(), m.getLunchIngredient().getUnit()));
                    }//if
                }//for
            }//if
            if(m.getDinnerIngredient() != null){
                temp = m.getDinnerIngredient().getDescription()  + m.getDinnerIngredient().getUnit()  + m.getDinnerIngredient().getCategory();
                for(Map.Entry<String, Double> z : mealPlanStorageMap.entrySet()){
                    String temp2 = z.getKey();
                    if(temp.contentEquals(temp2)){
                        shoppingStuff.add(new RecipeIngredient(m.getDinnerIngredient().getDescription(), Double.toString(z.getValue()), m.getDinnerIngredient().getUnit(), m.getDinnerIngredient().getUnit()));
                    }//if
                }//for
            }//if

        }//if



        //And finally, return shoppingStuff(Not done yet)
        //THE END



        return ingredients;
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
    private void loadMealPlans(ArrayList<String> docIds) {

        // fetch from db
        db = FirebaseFirestore.getInstance();
        ArrayList<MealPlan> query = new ArrayList<>();
        mealPlans.setValue(new ArrayList<>());
        db.collection("MealPlan").whereIn(FieldPath.documentId(), docIds).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        for (DocumentSnapshot document: querySnapshot.getDocuments()) {
                            Map<String, Object> data = document.getData();

                            final Recipe[] breakfastRecipe = {null};
                            final Recipe[] lunchRecipe = {null};
                            final Recipe[] dinnerRecipe = {null};
                            final Ingredient[] breakfastIngredient = {null};
                            final Ingredient[] lunchIngredient = {null};
                            final Ingredient[] dinnerIngredient = {null};

                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {

                                DocumentReference reference = (DocumentReference) entry.getValue();
                                Task<DocumentSnapshot> documentSnapshotTask = reference.get();
                                tasks.add(documentSnapshotTask);

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
                                            breakfastIngredient[0], lunchIngredient[0], dinnerIngredient[0]);
                                    mealPlan.setDocumentId(document.getId());
                                    query.add(mealPlan);
                                    mealPlans.setValue(query);
                                }
                            });
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

