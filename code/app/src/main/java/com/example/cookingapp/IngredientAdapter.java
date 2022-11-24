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

/**
 * The IngredientAdapter class
 * Creating an AdapterView for the dataset
 */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredientList;
    private Context context;

    /**
     * Constructor of the IngredientAdapter
     * @param context
     * @param ingredientList
     */
    public IngredientAdapter(@NonNull Context context, ArrayList<Ingredient> ingredientList) {
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
                    .inflate(R.layout.ingredient_list_item, parent, false);
        }

        Ingredient ingredient = ingredientList.get(position);


        TextView ingredientDescription = view.findViewById(R.id.ingredient_list_item_description);
        TextView ingredientCategory = view.findViewById(R.id.ingredient_list_item_category);
        TextView ingredientLocation = view.findViewById(R.id.ingredient_list_item_location);
        TextView ingredientAmount = view.findViewById(R.id.ingredient_list_item_amount);

        String DescUnitCombo = ingredient.getAmount().toString() + ingredient.getUnit();
        ingredientDescription.setText(ingredient.getDescription());

        ingredientCategory.setText(ingredient.getCategory());

        ingredientLocation.setText(ingredient.getLocation());

        ingredientAmount.setText(DescUnitCombo);
        return view;
    }
}
