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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Request;
import com.roktim.blooddonation.ui.admin.AdminDashboardActivity;
import com.roktim.blooddonation.ui.admin.AdminViewModel;
import com.roktim.blooddonation.ui.admin.adapters.EmergencyRequestAdapter;

import java.util.List;

public class EmergencyRequestsFragment extends Fragment
        implements EmergencyRequestAdapter.OnRequestClickListener {

    private AdminViewModel viewModel;
    private RecyclerView rvRequests;
    private LinearLayout layoutEmpty;
    private TabLayout tabLayout;
    private EmergencyRequestAdapter adapter;

    private boolean showPendingOnly = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R. layout.fragment_emergency_requests, container, false);

        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupTabLayout();
        loadRequests();

        return view;
    }

    private void initViews(View view) {
        rvRequests = view.findViewById(R.id.rv_requests);
        layoutEmpty = view.findViewById(R.id. layout_empty);
        tabLayout = view.findViewById(R. id.tab_layout);
    }

    private void initViewModel() {
        if (getActivity() instanceof AdminDashboardActivity) {
            viewModel = ((AdminDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void setupRecyclerView() {
        adapter = new EmergencyRequestAdapter(this);
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRequests.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout. Tab tab) {
                showPendingOnly = tab.getPosition() == 1;
                loadRequests();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadRequests() {
        if (viewModel == null) return;

        if (showPendingOnly) {
            viewModel.getPendingRequests().observe(getViewLifecycleOwner(), this::updateUI);
        } else {
            viewModel.getAllRequests().observe(getViewLifecycleOwner(), this::updateUI);
        }
    }

    private void updateUI(List<Request> requests) {
        if (requests != null && !requests.isEmpty()) {
            adapter.setRequests(requests);
            rvRequests.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        } else {
            rvRequests.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMarkFulfilled(Request request) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Mark as Fulfilled")
                .setMessage("Are you sure you want to mark this request as fulfilled?")
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    viewModel.markRequestFulfilled(request. getId());
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onFindDonors(Request request) {
        DonorManagementFragment fragment = new DonorManagementFragment();
        Bundle args = new Bundle();
        args.putString("blood_group", request. getBloodGroup());
        fragment.setArguments(args);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id. fragment_container, fragment)
                    . addToBackStack(null)
                    . commit();
        }
    }
}