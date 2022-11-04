package com.example.cookingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
Purpose: dialog fragment to view all ingredient properties
design rationale:
    Notes: handles edit and delete actions
    Attributes:
        layout variables:
            descriptionTextView
            locationTextView
            categoryTextView
            bestBeforeDateTextView
            amountTextView
            unitTextView

        listener - OnFragmentInteractionListener to handle edit and delete of ingredient
       OnFragmentInteractionListener - interface to define onEdit() and onDelete()
    Methods:
        newInstance - creates a new instance of ViewIngredientDialogFragment with ingredient as argument
        onAttach - check if OnFragmentInteractionListener is implemented in caller
        onCreateDialog - update layout views with ingredient properties

outstanding issues: none
*/
public class ViewIngredientDialogFragment extends DialogFragment {

    TextView descriptionTextView;
    TextView locationTextView;
    TextView categoryTextView;
    TextView bestBeforeDateTextView;
    TextView amountTextView;
    TextView unitTextView;

    /**
    Purpose: handle delete and edit ingredient
    design rationale: implemented in IngredientsActivity
    */
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    /**
     Creates new instance of ViewIngredientDialogFragment
     @param ingredient ingredient to view
     @return fragment fragment of type ViewIngredientDialogFragment
     */
    static ViewIngredientDialogFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        ViewIngredientDialogFragment fragment = new ViewIngredientDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     assigns listener and trows error if Activity does not implement OnFragmentInteractionListener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     builds and creates dialog fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_ingredient_dialog_fragment, null);

        descriptionTextView = view.findViewById(R.id.view_ingredient_fragment_description_textView);
        locationTextView = view.findViewById(R.id.view_ingredient_fragment_location_textView);
        categoryTextView = view.findViewById(R.id.view_ingredient_fragment_category_textView);
        bestBeforeDateTextView = view.findViewById(R.id.view_ingredient_fragment_bestBeforeDate_textView);
        amountTextView = view.findViewById(R.id.view_ingredient_fragment_amount_textView);
        unitTextView = view.findViewById(R.id.view_ingredient_fragment_unit_textView);

        // Display ingredient passed as argument
        Ingredient ingredient = (Ingredient) getArguments().getSerializable("ingredient");

        descriptionTextView.setText(ingredient.getDescription());
        categoryTextView.setText(ingredient.getCategory());
        locationTextView.setText(ingredient.getLocation());

        bestBeforeDateTextView.setText(ingredient.getBestBeforeDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        amountTextView.setText(ingredient.getAmount().toString());
        unitTextView.setText(ingredient.getUnit());

        // Get action from user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setCancelable(false)
                .setNeutralButton("Cancel", null)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDelete(ingredient);
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onEdit(ingredient);
                    }
                }).create();
    }
}
