package com.example.cookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The IngredientAdapter class
 * Creating an AdapterView for the dataset
 */
public class RecipeIngredientAdapter extends ArrayAdapter<RecipeIngredient> {

    private ArrayList<RecipeIngredient> ingredientList;
    private Context context;

    /**
     * Constructor of the IngredientAdapter
     * @param context
     * @param ingredientList
     */
    public RecipeIngredientAdapter(@NonNull Context context, ArrayList<RecipeIngredient> ingredientList) {
        super(context, 0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    /**
     * Inflates a view from content.xml to display data
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
                    .inflate(R.layout.recipe_ingredient_list_item, parent, false);
        }

        RecipeIngredient ingredient = ingredientList.get(position);


        TextView ingredientDescription = view.findViewById(R.id.ingredient_list_item_description);
        TextView ingredientCategory = view.findViewById(R.id.ingredient_list_item_category);
        TextView ingredientUnit = view.findViewById(R.id.ingredient_list_item_location);
        TextView ingredientAmount = view.findViewById(R.id.ingredient_list_item_amount);

        double tempAmount = Double.parseDouble(ingredient.getAmount());
        DecimalFormat df = new DecimalFormat("###.##");


        ingredientDescription.setText(ingredient.getDescription());

        ingredientCategory.setText(ingredient.getCategory());

        ingredientUnit.setText(ingredient.getUnit());

        ingredientAmount.setText(df.format(tempAmount));
        return view;
    }
}
