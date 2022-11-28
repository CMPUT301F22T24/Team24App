package com.example.cookingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {

    private ArrayList<ShoppingListItem> shoppingL;
    private Context context;
    private CheckboxListener checkboxL;

    public interface CheckboxListener {
        void onCheckboxListener(int position);
    }

    public void setCheckboxListener( CheckboxListener checkboxL){
        this.checkboxL = checkboxL;

    }


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

        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.shopping_list_item, parent, false);

            viewHolder.check = (CheckBox) view.findViewById(R.id.checkBox);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();

        }

        ShoppingListItem shoppingList = shoppingL.get(position);

        viewHolder.check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(checkboxL!=null){
                    checkboxL.onCheckboxListener(position);
                }

            }

        });



        TextView ShoppingIngredientDescription = view.findViewById(R.id.shopping_list_ingredient_name);
        TextView ShoppingIngredientCategory = view.findViewById(R.id.shopping_list_ingredient_category);
        TextView ShoppingIngredientAmount = view.findViewById(R.id.shopping_list_ingredient_count_and_unit);


        ShoppingIngredientDescription.setText(shoppingList.getIngredient().getDescription());

        String temp;
        temp="Category: "+shoppingList.getIngredient().getCategory();
        ShoppingIngredientCategory.setText(temp);



        double tempAmount = Double.parseDouble(shoppingList.getIngredient().getAmount());
        DecimalFormat df = new DecimalFormat("###.##");

        temp="Required: "+ df.format(tempAmount) + shoppingList.getIngredient().getUnit();
        ShoppingIngredientAmount.setText(temp);




        return view;
    }


    private static class ViewHolder{
        CheckBox check;

    }



}
