package com.example.cookingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
/**
 * This is the AddMealPlanDialogFragment class, it pops up when the user needs to add a meal
 */
public class AddMealPlanDialogFragment extends DialogFragment {
    private AddMealPlanDialogFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onAddMeal(MealPlan mealplan, String mealType);
    }

    static AddMealPlanDialogFragment newInstance(MealPlan mealPlan, String mealType) {
        Bundle args = new Bundle();
        args.putSerializable("mealPlan", mealPlan);
        args.putSerializable("mealType",mealType);

        AddMealPlanDialogFragment fragment = new AddMealPlanDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddMealPlanDialogFragment.OnFragmentInteractionListener) {
            listener = (AddMealPlanDialogFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }
    /**
     * This method sets the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_meal_dialog_fragment, null);

        // Display ingredient passed as argument
        assert getArguments() != null;
        MealPlan meal = (MealPlan) getArguments().getSerializable("mealPlan");
        String mealType = getArguments().getString("mealType");

        // Get action from user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setCancelable(false)
                .setNeutralButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onAddMeal(meal,mealType);
                    }
                })
                .create();

    }

}
