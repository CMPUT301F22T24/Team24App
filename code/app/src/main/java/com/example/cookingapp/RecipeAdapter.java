package com.example.cookingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/**
 * <p>
 * This is the class for the recipe adapter. It connects the recipe array list to the array list item.
 * </p>
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private final ArrayList<Recipe> recipeList;
    private final Context context;

    /**
     * Recipe Adapter instantiate context and recipelist.
     * @param context
     * @param recipeList
     */
    public RecipeAdapter(@NonNull Context context, ArrayList<Recipe> recipeList) {
        super(context, 0, recipeList);
        this.context = context;
        this.recipeList = recipeList;
    }

    /**
     * getView allows to see recipe list.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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
        TextView recipePrepTime = view.findViewById(R.id.recipe_prep_time);
        ImageView recipeImage = view.findViewById(R.id.recipeImage);

        // set all of the required fields for each item in the list
        recipeDescription.setText(recipe.getTitle());
        recipeCategory.setText(recipe.getCategory());
        recipePrepTime.setText(recipe.getPrepTime());
        if (recipe.getImage() == null){
            recipeImage.setImageResource(R.mipmap.camera);
        } else {
            recipeImage.setImageBitmap(StringToBitMap(recipe.getImage()));
        }

        return view;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
