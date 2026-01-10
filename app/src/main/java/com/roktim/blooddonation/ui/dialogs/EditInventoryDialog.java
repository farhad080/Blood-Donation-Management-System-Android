package com.roktim.blooddonation.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Inventory;

public class EditInventoryDialog extends DialogFragment {

    private final Inventory inventory;
    private final boolean isAddMode;
    private final OnInventoryUpdateListener listener;
    private TextView tvBloodGroup, tvCurrentUnits, tvTitle;
    private TextInputEditText etUnits;

    public interface OnInventoryUpdateListener {
        void onInventoryUpdate(String bloodGroup, int units, boolean isAdd);
    }

    public EditInventoryDialog(Inventory inventory, OnInventoryUpdateListener listener) {
        this. inventory = inventory;
        this.isAddMode = true;
        this. listener = listener;
    }

    public EditInventoryDialog(Inventory inventory, boolean isAddMode, OnInventoryUpdateListener listener) {
        this.inventory = inventory;
        this.isAddMode = isAddMode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog. Builder(requireContext());
        View view = LayoutInflater. from(getContext()).inflate(R.layout.dialog_edit_inventory, null);

        initViews(view);
        populateData();

        String title = isAddMode ? "Add Blood Units" : "Reduce Blood Units";
        String positiveButtonText = isAddMode ? "Add" :  "Reduce";

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(positiveButtonText, (dialog, which) -> saveChanges())
                .setNegativeButton(R.string. cancel, (dialog, which) -> dismiss());

        return builder.create();
    }

    private void initViews(View view) {
        tvBloodGroup = view.findViewById(R.id.tv_blood_group);
        tvCurrentUnits = view.findViewById(R.id. tv_current_units);
        tvTitle = view.findViewById(R.id.tv_title);
        etUnits = view.findViewById(R. id.et_units);
    }

    private void populateData() {
        tvBloodGroup.setText(inventory.getBloodGroup());
        tvCurrentUnits. setText("Current:  " + inventory.getUnits() + " units");
        tvTitle.setText(isAddMode ? "Enter units to add:" : "Enter units to reduce:");
    }

    private void saveChanges() {
        String unitsText = etUnits.getText() != null ? etUnits.getText().toString().trim() : "";
        if (unitsText.isEmpty()) return;

        try {
            int units = Integer.parseInt(unitsText);
            if (units <= 0) return;
            if (! isAddMode && units > inventory.getUnits()) { etUnits.setError("Cannot reduce more than available"); return; }
            if (listener != null) listener.onInventoryUpdate(inventory.getBloodGroup(), units, isAddMode);
        } catch (NumberFormatException e) {
            etUnits.setError("Invalid number");
        }
    }
}