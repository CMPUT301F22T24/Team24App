package com.example.cookingapp;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.Comparator;

public class ShoppingListItem implements Serializable {

    private Boolean isChecked;
    private RecipeIngredient ingredient;

    @DocumentId
    private String documentId;

    public ShoppingListItem() {

        this.isChecked = null;
        this.ingredient = null;
    }


    public ShoppingListItem(RecipeIngredient ingredient, boolean isChecked){
        this.ingredient = ingredient;
        this.isChecked=isChecked;
    }


    public RecipeIngredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(RecipeIngredient ingredient) {
        this.ingredient = ingredient;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }



    public static Comparator<ShoppingListItem> ShoppingListItemTitleComparator = new Comparator<ShoppingListItem>() {
        @Override
        public int compare(ShoppingListItem i1, ShoppingListItem i2) {
            String d1 = i1.getIngredient().getDescription();
            String d2 = i2.getIngredient().getDescription();
            d1=d1.toLowerCase();
            d2=d2.toLowerCase();

            // desc order
            return d1.compareTo(d2);
        }
    };


    public static Comparator<ShoppingListItem> ShoppingListItemCategoryComparator = new Comparator<ShoppingListItem>() {
        @Override
        public int compare(ShoppingListItem i1, ShoppingListItem i2) {
            String d1 = i1.getIngredient().getCategory();
            String d2 = i2.getIngredient().getCategory();
            d1=d1.toLowerCase();
            d2=d2.toLowerCase();
            // desc order
            return d1.compareTo(d2);
        }
    };


}//ShoppingList
