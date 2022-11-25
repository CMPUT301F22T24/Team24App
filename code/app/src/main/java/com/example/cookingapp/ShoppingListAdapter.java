package com.example.cookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {

    private ArrayList<ShoppingListItem> shoppingL;
    private Context context;
    /**
     *
     * Constructor of the ShoppingListAdapter
     * @param context
     * @param shoppingL
     */
    public ShoppingListAdapter(@NonNull Context context, @NonNull ArrayList<ShoppingListItem> shoppingL) {
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

        ShoppingListItem shoppingList = shoppingL.get(position);
        CheckBox isCheck =  view.findViewById(R.id.checkBox);


        TextView ShoppingIngredientDescription = view.findViewById(R.id.shopping_list_ingredient_name);
        TextView ShoppingIngredientCategory = view.findViewById(R.id.shopping_list_ingredient_category);
        TextView ShoppingIngredientAmount = view.findViewById(R.id.shopping_list_ingredient_count_and_unit);


        ShoppingIngredientDescription.setText(shoppingList.getIngredient().getDescription());

        String temp;
        temp="Category: "+shoppingList.getIngredient().getCategory();
        ShoppingIngredientCategory.setText(temp);



        temp="Required: "+shoppingList.getIngredient().getAmount() + shoppingList.getIngredient().getUnit();
        ShoppingIngredientAmount.setText(temp);

        if(shoppingList.getChecked()){
            isCheck.setChecked(true);
        }


        return view;
    }
}
