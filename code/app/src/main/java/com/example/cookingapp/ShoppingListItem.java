package com.example.cookingapp;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

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
}//ShoppingList
