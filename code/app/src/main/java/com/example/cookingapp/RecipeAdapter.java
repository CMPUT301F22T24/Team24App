package com.example.cookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipeList;
    private Context context;

    public RecipeAdapter(@NonNull Context context, ArrayList<Recipe> recipeList) {
        super(context, 0, recipeList);
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.recipe_list_item, parent, false);
        }

        Recipe recipe = recipeList.get(position);


        TextView recipeDescription = view.findViewById(R.id.recipe_description);
        TextView recipeCategory = view.findViewById(R.id.recipe_category);
        //TODO: add picture and prep time

        recipeDescription.setText(recipe.getTitle());
        recipeCategory.setText(recipe.getCategory());

        return view;
    }
}
