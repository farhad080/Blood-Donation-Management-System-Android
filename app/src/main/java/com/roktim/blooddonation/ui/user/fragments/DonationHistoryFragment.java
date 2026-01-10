package com.roktim.blooddonation.ui.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.ui.user.UserDashboardActivity;
import com.roktim.blooddonation.ui.user.UserViewModel;
import com.roktim.blooddonation.ui.user.adapters.UserDonationAdapter;

import java.util.List;

public class DonationHistoryFragment extends Fragment {

    private UserViewModel viewModel;
    private int userId;
    private RecyclerView rvDonations;
    private LinearLayout layoutEmpty;
    private TextView tvTotalDonations;
    private UserDonationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout. fragment_donation_history, container, false);
        initViews(view);
        initViewModel();
        setupRecyclerView();
        loadDonations();
        return view;
    }

    private void initViews(View view) {
        rvDonations = view.findViewById(R.id. rv_donations);
        layoutEmpty = view.findViewById(R. id.layout_empty);
        tvTotalDonations = view.findViewById(R.id.tv_total_donations);
    }

    private void initViewModel() {
        if (getActivity() instanceof UserDashboardActivity) {
            UserDashboardActivity activity = (UserDashboardActivity) getActivity();
            viewModel = activity. getViewModel();
            userId = activity.getCurrentUserId();
        }
    }

    private void setupRecyclerView() {
        adapter = new UserDonationAdapter();
        rvDonations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDonations.setAdapter(adapter);
    }

    private void loadDonations() {
        if (viewModel == null) return;
        viewModel.getUserDonations(userId).observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<Donation> donations) {
        if (donations != null && !donations.isEmpty()) {
            adapter.setDonations(donations);
            rvDonations.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            int totalUnits = 0;
            for (Donation d : donations) totalUnits += d.getUnits();
            tvTotalDonations.setText("Total Donations: " + donations.size() + " (" + totalUnits + " units)");
            tvTotalDonations.setVisibility(View.VISIBLE);
        } else {
            rvDonations.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            tvTotalDonations.setVisibility(View.GONE);
        }
    }
}