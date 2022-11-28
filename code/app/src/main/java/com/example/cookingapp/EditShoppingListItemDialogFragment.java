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

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecipeDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditShoppingListItemDialogFragment extends DialogFragment {

    ShoppingListActivityViewModel viewModel;
    TextView descriptionTextView, unitTextView, categoryTextView;
    EditText amountEditText, locationEditText;
    DatePicker expiryDatePicker;
    Button confirmButton;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {

    }

    static EditShoppingListItemDialogFragment newInstance(ShoppingListItem shoppingListItem) {
        Bundle args = new Bundle();
        args.putSerializable("shoppingListItem", shoppingListItem);

        EditShoppingListItemDialogFragment fragment = new EditShoppingListItemDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            confirmButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            confirmButton.setEnabled(false);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_shopping_list_item_dialog_fragment, null);
        viewModel = new ViewModelProvider(this).get(ShoppingListActivityViewModel.class);
        descriptionTextView = view.findViewById(R.id.edit_shopping_item_fragment_description_textView);
        categoryTextView = view.findViewById(R.id.edit_shopping_item_fragment_category_textView);
        locationEditText = view.findViewById(R.id.edit_shopping_item_fragment_location_editText);
        expiryDatePicker = view.findViewById(R.id.edit_shopping_item_fragment_expiry_datePicker);
        unitTextView = view.findViewById(R.id.edit_shopping_item_fragment_unit_textView);
        amountEditText = view.findViewById(R.id.edit_shopping_item_fragment_amount_editText);
        amountEditText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3, 2)});



        // Display ingredient passed as argument
        ShoppingListItem shoppingListItem = (ShoppingListItem) getArguments().getSerializable("shoppingListItem");
        descriptionTextView.setText(shoppingListItem.getIngredient().getDescription());
        categoryTextView.setText(shoppingListItem.getIngredient().getCategory());
        unitTextView.setText(shoppingListItem.getIngredient().getUnit());
        TextWatcher textFilledWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (locationEditText.getText().length() >= 1 && amountEditText.getText().length() >= 1 ) {
                    confirmButton.setEnabled(true);
                }
            }
        };





        locationEditText.addTextChangedListener(textFilledWatcher);
        amountEditText.addTextChangedListener(textFilledWatcher);
        // Get action from user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setCancelable(false)
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


    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;
        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern= Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }
    }//DecimalDigitsInputFilter



}