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

public class ShoppingListAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredientList;
    private Context context;
    /**
     * Constructor of the ShoppingListAdapter
     * @param context
     * @param ingredientList
     */
    public ShoppingListAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> ingredientList) {
        super(context,0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.shopping_list_item, parent, false);
        }

        Ingredient ingredient = ingredientList.get(position);


        TextView ingredientDescription = view.findViewById(R.id.ingredient_list_item_description);
        TextView ingredientCategory = view.findViewById(R.id.ingredient_list_item_category);
        TextView ingredientUnit = view.findViewById(R.id.ingredient_list_item_location);
        TextView ingredientAmount = view.findViewById(R.id.ingredient_list_item_amount);


        ingredientDescription.setText(ingredient.getDescription());

        ingredientCategory.setText(ingredient.getCategory());

        ingredientUnit.setText(ingredient.getUnit());

        ingredientAmount.setText(ingredient.getAmount().toString());
        return view;
    }
}
