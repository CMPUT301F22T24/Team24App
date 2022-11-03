package com.example.cookingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecipeDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecipeDialogFragment extends DialogFragment {

    TextView titleTextView;
    TextView prepTimeTextView;
    TextView numServingsTextView;
    TextView categoryTextView;
    TextView commentsTextView;
    ImageView recipePictureImageView;

    // TODO: ADD Ingredients
    private ViewIngredientDialogFragment.OnFragmentInteractionListener listener;

    public interface OnRecipeFragmentInteractionListener {
        // void onEdit(Ingredient ingredient);
        //void onDelete(Ingredient ingredient);
    }

    static ViewRecipeDialogFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putSerializable("recipe", recipe);

        ViewRecipeDialogFragment fragment = new ViewRecipeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        if (context instanceof OnRecipeFragmentInteractionListener) {
//            listener = (OnRecipeFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
//        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_recipe_dialog_fragment, null);

        titleTextView = view.findViewById(R.id.view_recipe_fragment_title_textView);
        prepTimeTextView = view.findViewById(R.id.view_recipe_fragment_prep_time_textView);
        numServingsTextView = view.findViewById(R.id.view_recipe_fragment_num_servings_textView);
        categoryTextView = view.findViewById(R.id.view_recipe_fragment_category_textView);
        commentsTextView = view.findViewById(R.id.view_recipe_fragment_comments_textView);
        recipePictureImageView = view.findViewById(R.id.view_recipe_fragment_image_imageView);

        // Display ingredient passed as argument
        Recipe recipe = (Recipe) getArguments().getSerializable("recipe");

        titleTextView.setText(recipe.getTitle());
        prepTimeTextView.setText(recipe.getPrepTime());
        numServingsTextView.setText(recipe.getServings());
        categoryTextView.setText(recipe.getCategory());
        commentsTextView.setText(recipe.getComments());
        if (recipe.getImage() == null) {
            recipePictureImageView.setImageResource(R.mipmap.camera);
        } else {
            recipePictureImageView.setImageBitmap(StringToBitMap(recipe.getImage()));
        }
        //TODO Ingredients

        // Get action from user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setCancelable(false)
                .setNeutralButton("Cancel", null)
//                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        listener.onDelete(recipe);
//                    }
//                })
//                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        listener.onEdit(recipe);
//                    }
//                })
                .create();

    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}