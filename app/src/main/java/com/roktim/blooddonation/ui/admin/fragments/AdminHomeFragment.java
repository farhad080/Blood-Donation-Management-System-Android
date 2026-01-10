package com.roktim.blooddonation.ui.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.ui.admin.AdminDashboardActivity;
import com.roktim.blooddonation.ui.admin.AdminViewModel;

public class AdminHomeFragment extends Fragment {

    private AdminViewModel viewModel;
    private TextView tvTotalUsers, tvTotalDonors, tvBloodUnits, tvPendingRequests;
    private MaterialButton btnAddDonation, btnViewRequests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater. inflate(R.layout.fragment_admin_home, container, false);

        initViews(view);
        initViewModel();
        observeData();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        tvTotalUsers = view.findViewById(R. id.tv_total_users);
        tvTotalDonors = view.findViewById(R.id. tv_total_donors);
        tvBloodUnits = view.findViewById(R. id.tv_blood_units);
        tvPendingRequests = view.findViewById(R.id. tv_pending_requests);
        btnAddDonation = view.findViewById(R.id.btn_add_donation);
        btnViewRequests = view.findViewById(R.id.btn_view_requests);
    }

    private void initViewModel() {
        if (getActivity() instanceof AdminDashboardActivity) {
            viewModel = ((AdminDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void observeData() {
        if (viewModel == null) return;

        viewModel.getTotalUsersCount().observe(getViewLifecycleOwner(), count -> {
            tvTotalUsers.setText(count != null ? String.valueOf(count) : "0");
        });

        viewModel.getTotalDonorsCount().observe(getViewLifecycleOwner(), count -> {
            tvTotalDonors.setText(count != null ? String. valueOf(count) : "0");
        });

        viewModel.getTotalBloodUnits().observe(getViewLifecycleOwner(), units -> {
            tvBloodUnits. setText(units != null ? String.valueOf(units) : "0");
        });

        viewModel.getPendingRequestsCount().observe(getViewLifecycleOwner(), count -> {
            tvPendingRequests.setText(count != null ? String.valueOf(count) : "0");
        });
    }

    private void setupClickListeners() {
        btnAddDonation.setOnClickListener(v -> {
            if (getActivity() instanceof AdminDashboardActivity) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R. id.fragment_container, new DonationRecordsFragment())
                        .commit();
            }
        });

        btnViewRequests. setOnClickListener(v -> {
            if (getActivity() instanceof AdminDashboardActivity) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        . replace(R.id.fragment_container, new EmergencyRequestsFragment())
                        .commit();
            }
        });
    }
}