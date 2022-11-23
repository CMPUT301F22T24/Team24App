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

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    private ArrayList<ShoppingList> shoppingL;
    private Context context;
    /**
     *
     * Constructor of the ShoppingListAdapter
     * @param context
     * @param shoppingL
     */
    public ShoppingListAdapter(@NonNull Context context, @NonNull ArrayList<ShoppingList> shoppingL) {
        super(context,0, shoppingL);
        this.context = context;
        this.shoppingL = shoppingL;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.shopping_list_item, parent, false);
        }

        ShoppingList shoppingList = shoppingL.get(position);



        TextView ShoppingIngredientDescription = view.findViewById(R.id.shopping_list_ingredient_name);
        TextView ShoppingIngredientCategory = view.findViewById(R.id.shopping_list_ingredient_category);
        TextView ShoppingIngredientUnit = view.findViewById(R.id.shopping_list_ingredient_location);
        TextView ShoppingIngredientAmount = view.findViewById(R.id.shopping_list_ingredient_count_unit);


        ShoppingIngredientDescription.setText(shoppingList.getDescription());

        ShoppingIngredientCategory.setText(shoppingList.getCategory());

        ShoppingIngredientUnit.setText(shoppingList.getUnit());

        String temp=shoppingList.getAmount().toString() + shoppingList.getUnit();
        ShoppingIngredientAmount.setText(temp);

        return view;
    }
}
