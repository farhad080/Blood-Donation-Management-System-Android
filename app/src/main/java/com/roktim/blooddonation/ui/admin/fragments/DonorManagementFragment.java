package com.roktim.blooddonation.ui.admin.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.ui.admin.AdminDashboardActivity;
import com.roktim.blooddonation.ui.admin.AdminViewModel;
import com.roktim.blooddonation.ui.admin.adapters.AdminDonorAdapter;
import com.roktim.blooddonation.ui.dialogs.EditDonorDialog;
import com.roktim.blooddonation.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DonorManagementFragment extends Fragment implements AdminDonorAdapter.OnDonorClickListener {

    private AdminViewModel viewModel;
    private RecyclerView rvDonors;
    private LinearLayout layoutEmpty;
    private AutoCompleteTextView spinnerBloodGroup;
    private TextInputEditText etLocation;
    private MaterialButton btnSearch;
    private AdminDonorAdapter adapter;

    private LiveData<List<User>> currentLiveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donor_management, container, false);

        initViews(view);
        initViewModel();
        setupSpinner();
        setupRecyclerView();
        setupClickListeners();
        loadDonors();

        return view;
    }

    private void initViews(View view) {
        rvDonors = view.findViewById(R.id.rv_donors);
        layoutEmpty = view. findViewById(R.id.layout_empty);
        spinnerBloodGroup = view. findViewById(R.id.spinner_blood_group);
        etLocation = view.findViewById(R.id. et_location);
        btnSearch = view.findViewById(R. id.btn_search);
    }

    private void initViewModel() {
        if (getActivity() instanceof AdminDashboardActivity) {
            viewModel = ((AdminDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void setupSpinner() {
        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add("All");
        for (String bg : Constants.BLOOD_GROUPS) {
            bloodGroups.add(bg);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android. R.layout.simple_dropdown_item_1line,
                bloodGroups
        );
        spinnerBloodGroup. setAdapter(adapter);
    }

    private void setupRecyclerView() {
        adapter = new AdminDonorAdapter(this);
        rvDonors.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDonors.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> searchDonors());
    }

    private void loadDonors() {
        if (viewModel == null) return;

        if (currentLiveData != null) {
            currentLiveData.removeObservers(getViewLifecycleOwner());
        }

        currentLiveData = viewModel.getAllDonors();
        currentLiveData.observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void searchDonors() {
        if (viewModel == null) return;

        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String location = etLocation.getText() != null ?
                etLocation.getText().toString().trim() : "";

        if (bloodGroup.equals("All")) {
            bloodGroup = "";
        }

        if (currentLiveData != null) {
            currentLiveData.removeObservers(getViewLifecycleOwner());
        }

        currentLiveData = viewModel.searchDonors(bloodGroup, location);
        currentLiveData.observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<User> donors) {
        if (donors != null && !donors.isEmpty()) {
            adapter.setDonors(donors);
            rvDonors.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        } else {
            rvDonors.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditClick(User user) {
        EditDonorDialog dialog = new EditDonorDialog(user, updatedUser -> {
            viewModel.updateDonor(updatedUser);
        });
        dialog.show(getChildFragmentManager(), "EditDonorDialog");
    }

    @Override
    public void onDeleteClick(User user) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_donor)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(R. string.delete, (dialog, which) -> {
                    viewModel.deleteDonor(user);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onCallClick(User user) {
        String phone = user.getPhone();
        if (phone != null && ! phone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }
}