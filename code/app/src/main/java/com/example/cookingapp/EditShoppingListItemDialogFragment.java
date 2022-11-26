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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecipeDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditShoppingListItemDialogFragment extends DialogFragment {

    ShoppingListActivityViewModel viewModel;
    TextView descriptionTextView, unitTextView;

    EditText amountEditText, locationEditText;
    DatePicker expiryDatePicker;

    ListView ingredientListView;
    ArrayList<RecipeIngredient> ingredientList;
    RecipeIngredientAdapter recipeIngredientAdapter;
    // TODO: ADD Ingredients
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onEdit(Recipe recipe);

        void onAdd(Ingredient ingredient);

        void onDelete(Recipe recipe);
    }

    static EditShoppingListItemDialogFragment newInstance(ShoppingListItem shoppingListItem) {
        Bundle args = new Bundle();
        args.putSerializable("shoppingListItem", shoppingListItem);

        EditShoppingListItemDialogFragment fragment = new EditShoppingListItemDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        viewModel = new ViewModelProvider(this).get(ShoppingListActivityViewModel.class);
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            listener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
//        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_shopping_list_item_dialog_fragment, null);

        descriptionTextView = view.findViewById(R.id.edit_shopping_item_fragment_description_textView);
        locationEditText = view.findViewById(R.id.edit_shopping_item_fragment_location_editText);
        expiryDatePicker = view.findViewById(R.id.edit_shopping_item_fragment_expiry_datePicker);
        unitTextView = view.findViewById(R.id.edit_shopping_item_fragment_unit_textView);
        amountEditText = view.findViewById(R.id.edit_shopping_item_fragment_amount_editText);

        // Display ingredient passed as argument
        ShoppingListItem shoppingListItem = (ShoppingListItem) getArguments().getSerializable("shoppingListItem");
        descriptionTextView.setText(shoppingListItem.getIngredient().getDescription());
        unitTextView.setText(shoppingListItem.getIngredient().getUnit());


        // Get action from user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setCancelable(false)
//                .setNeutralButton("Cancel", null)
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
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RecipeIngredient toAdd = shoppingListItem.getIngredient();
                        int year = expiryDatePicker.getYear();
                        int month = expiryDatePicker.getMonth();
                        int day = expiryDatePicker.getDayOfMonth();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        Date date = calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        viewModel.addIngredient(new Ingredient(toAdd.getDescription(),
                                LocalDate.parse(sdf.format(date)),
                                String.valueOf(locationEditText.getText()),
                                Double.parseDouble(String.valueOf(amountEditText.getText())),
                                toAdd.getUnit(),
                                toAdd.getCategory()
                        ));

                    }
                })
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