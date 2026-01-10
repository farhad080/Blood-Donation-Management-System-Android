package com.roktim.blooddonation.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.utils.Constants;

import java.util.Arrays;
import java.util.Calendar;

public class EditDonorDialog extends DialogFragment {

    private final User user;
    private final OnDonorUpdateListener listener;
    private TextInputEditText etName, etPhone, etEmail, etAddress, etLastDonation;
    private AutoCompleteTextView spinnerBloodGroup;

    public interface OnDonorUpdateListener {
        void onDonorUpdate(User user);
    }

    public EditDonorDialog(User user, OnDonorUpdateListener listener) {
        this. user = user;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog. Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_donor, null);

        initViews(view);
        setupSpinner();
        setupDatePicker();
        populateData();

        builder.setView(view)
                .setTitle(R. string.edit_donor)
                .setPositiveButton(R.string.save, (dialog, which) -> saveChanges())
                .setNegativeButton(R. string.cancel, (dialog, which) -> dismiss());

        return builder.create();
    }

    private void initViews(View view) {
        etName = view.findViewById(R. id.et_name);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view. findViewById(R.id.et_email);
        etAddress = view.findViewById(R.id.et_address);
        etLastDonation = view.findViewById(R.id.et_last_donation);
        spinnerBloodGroup = view.findViewById(R.id. spinner_blood_group);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R. layout.simple_dropdown_item_1line, Arrays.asList(Constants.BLOOD_GROUPS));
        spinnerBloodGroup.setAdapter(adapter);
    }

    private void setupDatePicker() {
        etLastDonation.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        String date = String. format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        etLastDonation.setText(date);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private void populateData() {
        etName.setText(user. getName());
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());
        etAddress.setText(user. getAddress());
        spinnerBloodGroup. setText(user.getBloodGroup(), false);
        if (user.getLastDonation() != null) etLastDonation. setText(user.getLastDonation());
    }

    private void saveChanges() {
        String name = etName. getText() != null ? etName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone. getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String address = etAddress. getText() != null ? etAddress.getText().toString().trim() : "";
        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String lastDonation = etLastDonation.getText() != null ? etLastDonation.getText().toString().trim() : "";

        if (name.isEmpty()) { etName.setError("Name required"); return; }

        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);
        user.setBloodGroup(bloodGroup);
        user.setLastDonation(lastDonation);

        if (listener != null) listener.onDonorUpdate(user);
    }
}