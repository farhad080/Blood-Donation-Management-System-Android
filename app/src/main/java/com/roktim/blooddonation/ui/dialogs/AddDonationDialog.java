package com.roktim.blooddonation.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.RoktimApplication;
import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.utils.Constants;
import com.roktim.blooddonation.utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class AddDonationDialog extends DialogFragment {

    private AutoCompleteTextView spinnerDonor, spinnerBloodGroup;
    private TextInputEditText etUnits, etDate;
    private final OnDonationAddedListener listener;
    private List<User> donorList = new ArrayList<>();

    public interface OnDonationAddedListener {
        void onDonationAdded(Donation donation);
    }

    public AddDonationDialog(OnDonationAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_donation, null);

        initViews(view);
        loadDonors();
        setupSpinners();
        setupDatePicker();

        builder.setView(view)
                .setTitle(R.string.add_donation)
                .setPositiveButton(R. string.save, null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dismiss());

        AlertDialog dialog = builder. create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (validateAndSave()) dismiss();
            });
        });
        return dialog;
    }

    private void initViews(View view) {
        spinnerDonor = view.findViewById(R.id. spinner_donor);
        spinnerBloodGroup = view.findViewById(R.id. spinner_blood_group);
        etUnits = view.findViewById(R.id. et_units);
        etDate = view.findViewById(R. id.et_date);
        etDate.setText(DateUtils.getCurrentDate());
    }

    private void loadDonors() {
        Executors.newSingleThreadExecutor().execute(() -> {
            donorList = RoktimApplication.getInstance().getDatabase().userDao().getAllDonorsSync();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    List<String> donorNames = new ArrayList<>();
                    for (User user : donorList) {
                        donorNames.add(user.getName() + " (" + user.getBloodGroup() + ")");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android. R.layout.simple_dropdown_item_1line, donorNames);
                    spinnerDonor.setAdapter(adapter);
                });
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(requireContext(),
                android. R.layout.simple_dropdown_item_1line, Arrays. asList(Constants. BLOOD_GROUPS));
        spinnerBloodGroup.setAdapter(bloodGroupAdapter);

        spinnerDonor. setOnItemClickListener((parent, view, position, id) -> {
            if (position < donorList.size()) {
                spinnerBloodGroup.setText(donorList.get(position).getBloodGroup(), false);
            }
        });
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        etDate.setText(date);
                    },
                    calendar.get(Calendar. YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private boolean validateAndSave() {
        String donorText = spinnerDonor. getText().toString().trim();
        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String unitsText = etUnits.getText() != null ? etUnits.getText().toString().trim() : "";
        String date = etDate.getText() != null ? etDate.getText().toString().trim() : "";

        if (donorText.isEmpty()) { Toast.makeText(getContext(), "Please select a donor", Toast.LENGTH_SHORT).show(); return false; }
        if (bloodGroup.isEmpty()) { Toast.makeText(getContext(), "Please select blood group", Toast. LENGTH_SHORT).show(); return false; }
        if (unitsText.isEmpty()) { etUnits.setError("Units required"); return false; }

        int units;
        try {
            units = Integer. parseInt(unitsText);
            if (units <= 0) { etUnits.setError("Invalid units"); return false; }
        } catch (NumberFormatException e) { etUnits.setError("Invalid number"); return false; }

        int selectedIndex = -1;
        for (int i = 0; i < donorList.size(); i++) {
            User user = donorList.get(i);
            if ((user.getName() + " (" + user.getBloodGroup() + ")").equals(donorText)) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex == -1) { Toast.makeText(getContext(), "Invalid donor selected", Toast.LENGTH_SHORT).show(); return false; }

        Donation donation = new Donation(donorList.get(selectedIndex).getId(), bloodGroup, units, date);
        if (listener != null) listener.onDonationAdded(donation);
        return true;
    }
}