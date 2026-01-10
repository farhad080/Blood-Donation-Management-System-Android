package com.roktim.blooddonation.ui.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.ui.user.UserDashboardActivity;
import com.roktim.blooddonation.ui.user.UserViewModel;
import com.roktim.blooddonation.ui.user.adapters.UserRequestAdapter;
import com.roktim.blooddonation.utils.Constants;

import java.util.Arrays;

public class EmergencyRequestFragment extends Fragment {

    private UserViewModel viewModel;
    private int userId;
    private TextInputEditText etPatientName, etLocation, etMessage;
    private AutoCompleteTextView spinnerBloodGroup;
    private MaterialButton btnSubmit;
    private RecyclerView rvMyRequests;
    private UserRequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater. inflate(R.layout.fragment_emergency_request, container, false);
        initViews(view);
        initViewModel();
        setupSpinner();
        setupRecyclerView();
        setupClickListeners();
        loadMyRequests();
        return view;
    }

    private void initViews(View view) {
        etPatientName = view.findViewById(R. id.et_patient_name);
        etLocation = view. findViewById(R.id.et_location);
        etMessage = view.findViewById(R.id.et_message);
        spinnerBloodGroup = view.findViewById(R.id.spinner_blood_group);
        btnSubmit = view.findViewById(R.id.btn_submit);
        rvMyRequests = view.findViewById(R.id.rv_my_requests);
    }

    private void initViewModel() {
        if (getActivity() instanceof UserDashboardActivity) {
            UserDashboardActivity activity = (UserDashboardActivity) getActivity();
            viewModel = activity.getViewModel();
            userId = activity.getCurrentUserId();
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout. simple_dropdown_item_1line, Arrays.asList(Constants.BLOOD_GROUPS));
        spinnerBloodGroup. setAdapter(adapter);
    }

    private void setupRecyclerView() {
        adapter = new UserRequestAdapter();
        rvMyRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyRequests.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSubmit.setOnClickListener(v -> submitRequest());
    }

    private void loadMyRequests() {
        if (viewModel == null) return;
        viewModel.getUserRequests(userId).observe(getViewLifecycleOwner(), requests -> {
            if (requests != null && !requests.isEmpty()) {
                adapter.setRequests(requests);
                rvMyRequests.setVisibility(View.VISIBLE);
            } else {
                rvMyRequests.setVisibility(View.GONE);
            }
        });
    }

    private void submitRequest() {
        String patientName = etPatientName.getText() != null ? etPatientName.getText().toString().trim() : "";
        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String location = etLocation.getText() != null ? etLocation. getText().toString().trim() : "";
        String message = etMessage.getText() != null ? etMessage.getText().toString().trim() : "";

        if (patientName.isEmpty()) { etPatientName.setError("Patient name is required"); return; }
        if (bloodGroup.isEmpty()) { Toast.makeText(getContext(), "Please select blood group", Toast.LENGTH_SHORT).show(); return; }
        if (location.isEmpty()) { etLocation.setError("Location is required"); return; }

        viewModel.submitEmergencyRequest(userId, patientName, bloodGroup, location, message);
        etPatientName.setText("");
        spinnerBloodGroup. setText("", false);
        etLocation.setText("");
        etMessage.setText("");
        Toast.makeText(getContext(), "Emergency request submitted successfully!", Toast.LENGTH_SHORT).show();
    }
}