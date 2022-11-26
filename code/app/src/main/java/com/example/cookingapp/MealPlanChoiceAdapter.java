package com.example.cookingapp;

import android.app.Notification;
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

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * <p>
 * This is the class for the meal plan adapter. It connects the meal plan array list to the array list item.
 * </p>
 */
public class MealPlanChoiceAdapter extends ArrayAdapter<MealPlanChoice> {
    private final ArrayList<MealPlanChoice> mealPlanChoices;
    private final Context context;

    public MealPlanChoiceAdapter(@NonNull Context context, ArrayList<MealPlanChoice> mealPlanChoices) {
        super(context, 0, mealPlanChoices);
        this.context = context;
        this.mealPlanChoices = mealPlanChoices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.mealplanchoice_list_item, parent, false);
        }

        MealPlanChoice mealPlanChoice = mealPlanChoices.get(position);

        TextView descriptionTextView = view.findViewById(R.id.mealPlanChoice_list_item_description);
        TextView titleTextView0 = view.findViewById(R.id.mealPlanChoice_title_textView0);
        TextView titleTextView1 = view.findViewById(R.id.mealPlanChoice_title_textView1);
        TextView titleTextView2 = view.findViewById(R.id.mealPlanChoice_title_textView2);
        TextView textView0 = view.findViewById(R.id.mealPlanChoice_textView0);
        TextView textView1 = view.findViewById(R.id.mealPlanChoice_textView1);
        TextView textView2 = view.findViewById(R.id.mealPlanChoice_textView2);
        ImageView imageView = view.findViewById(R.id.mealPlanChoice_image);

        if (mealPlanChoice instanceof Recipe) {
            Recipe recipe = (Recipe) mealPlanChoice;
            descriptionTextView.setText(recipe.getTitle());
            titleTextView0.setText("Prep Time: ");
            textView0.setText(recipe.getPrepTime());
            titleTextView1.setText("Category: ");
            textView1.setText(recipe.getCategory());
            titleTextView2.setText("Servings: ");
            textView2.setText(recipe.getServings());
            imageView.setImageAlpha(255);
            imageView.setVisibility(View.VISIBLE);
            if (recipe.getImage() == null) {
                imageView.setImageResource(R.mipmap.camera);
            } else {
                imageView.setImageBitmap(StringToBitMap(recipe.getImage()));
            }
        }
        else if (mealPlanChoice instanceof Ingredient) {
            Ingredient ingredient = (Ingredient) mealPlanChoice;
            descriptionTextView.setText(ingredient.getDescription());
            titleTextView0.setText("Location: ");
            textView0.setText(ingredient.getLocation());
            titleTextView1.setText("Category: ");
            textView1.setText(ingredient.getCategory());
            titleTextView2.setText("Amount: ");
            textView2.setText(ingredient.getAmount() + " " + ingredient.getUnit());
            imageView.setImageAlpha(0);
            imageView.setVisibility(View.GONE);
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

