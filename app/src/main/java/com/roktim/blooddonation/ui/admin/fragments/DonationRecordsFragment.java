package com.roktim.blooddonation.ui.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.ui.admin.AdminDashboardActivity;
import com.roktim.blooddonation.ui.admin.AdminViewModel;
import com.roktim.blooddonation.ui.admin.adapters.DonationRecordAdapter;
import com.roktim.blooddonation.ui.dialogs.AddDonationDialog;

import java.util.List;

public class DonationRecordsFragment extends Fragment {

    private AdminViewModel viewModel;
    private RecyclerView rvDonations;
    private LinearLayout layoutEmpty;
    private MaterialButton btnAddDonation;
    private DonationRecordAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater. inflate(R.layout.fragment_donation_records, container, false);

        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupClickListeners();
        observeData();

        return view;
    }

    private void initViews(View view) {
        rvDonations = view.findViewById(R.id.rv_donations);
        layoutEmpty = view. findViewById(R.id.layout_empty);
        btnAddDonation = view. findViewById(R.id.btn_add_donation);
    }

    private void initViewModel() {
        if (getActivity() instanceof AdminDashboardActivity) {
            viewModel = ((AdminDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void setupRecyclerView() {
        adapter = new DonationRecordAdapter();
        rvDonations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDonations.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnAddDonation.setOnClickListener(v -> showAddDonationDialog());
    }

    private void observeData() {
        if (viewModel == null) return;

        viewModel.getAllDonations().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<Donation> donations) {
        if (donations != null && !donations.isEmpty()) {
            adapter.setDonations(donations);
            rvDonations.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        } else {
            rvDonations.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void showAddDonationDialog() {
        AddDonationDialog dialog = new AddDonationDialog(donation -> {
            viewModel.addDonation(donation);
        });
        dialog.show(getChildFragmentManager(), "AddDonationDialog");
    }
}