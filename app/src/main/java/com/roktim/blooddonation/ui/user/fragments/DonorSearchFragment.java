package com.roktim.blooddonation.ui.user.fragments;

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
import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.ui.user.UserDashboardActivity;
import com.roktim.blooddonation.ui.user.UserViewModel;
import com.roktim.blooddonation.ui.user.adapters.DonorSearchAdapter;
import com.roktim.blooddonation.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DonorSearchFragment extends Fragment implements DonorSearchAdapter.OnDonorClickListener {

    private UserViewModel viewModel;
    private RecyclerView rvDonors;
    private LinearLayout layoutEmpty;
    private AutoCompleteTextView spinnerBloodGroup;
    private TextInputEditText etLocation;
    private MaterialButton btnSearch;
    private DonorSearchAdapter adapter;
    private LiveData<List<User>> currentLiveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater. inflate(R.layout.fragment_donor_search, container, false);
        initViews(view);
        initViewModel();
        setupSpinner();
        setupRecyclerView();
        setupClickListeners();
        return view;
    }

    private void initViews(View view) {
        rvDonors = view.findViewById(R. id.rv_donors);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        spinnerBloodGroup = view.findViewById(R.id.spinner_blood_group);
        etLocation = view.findViewById(R.id.et_location);
        btnSearch = view.findViewById(R.id. btn_search);
    }

    private void initViewModel() {
        if (getActivity() instanceof UserDashboardActivity) {
            viewModel = ((UserDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void setupSpinner() {
        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add("All Blood Groups");
        for (String bg :  Constants.BLOOD_GROUPS) {
            bloodGroups. add(bg);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout. simple_dropdown_item_1line, bloodGroups);
        spinnerBloodGroup.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        adapter = new DonorSearchAdapter(this);
        rvDonors.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDonors.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSearch. setOnClickListener(v -> searchDonors());
    }

    private void searchDonors() {
        if (viewModel == null) return;
        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String location = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
        if (bloodGroup.equals("All Blood Groups")) bloodGroup = "";
        if (currentLiveData != null) currentLiveData.removeObservers(getViewLifecycleOwner());
        currentLiveData = viewModel.searchDonors(bloodGroup, location);
        currentLiveData. observe(getViewLifecycleOwner(), this::updateUI);
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
    public void onCallClick(User user) {
        String phone = user.getPhone();
        if (phone != null && ! phone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }
}