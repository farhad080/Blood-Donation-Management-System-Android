package com.roktim.blooddonation.ui.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.ui.user.UserDashboardActivity;
import com.roktim.blooddonation.ui.user.UserViewModel;
import com.roktim.blooddonation.utils.DateUtils;
import com.roktim.blooddonation.utils.ImageUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeFragment extends Fragment {

    private UserViewModel viewModel;
    private CircleImageView ivProfile;
    private TextView tvUserName, tvBloodGroup, tvLastDonation, tvDonationStatus;
    private CardView cardSearchDonors, cardEmergency, cardHistory, cardProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        initViews(view);
        initViewModel();
        observeData();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        ivProfile = view.findViewById(R. id.iv_profile);
        tvUserName = view.findViewById(R.id. tv_user_name);
        tvBloodGroup = view. findViewById(R.id.tv_blood_group);
        tvLastDonation = view.findViewById(R.id.tv_last_donation);
        tvDonationStatus = view.findViewById(R.id. tv_donation_status);
        cardSearchDonors = view.findViewById(R. id.card_search_donors);
        cardEmergency = view.findViewById(R.id.card_emergency);
        cardHistory = view.findViewById(R.id. card_history);
        cardProfile = view.findViewById(R. id.card_profile);
    }

    private void initViewModel() {
        if (getActivity() instanceof UserDashboardActivity) {
            viewModel = ((UserDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void observeData() {
        if (viewModel == null) return;

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(User user) {
        if (user == null) return;

        tvUserName.setText("Welcome, " + user. getName() + "!");
        tvBloodGroup. setText(user.getBloodGroup());

        if (user. getProfileImage() != null && ! user.getProfileImage().isEmpty()) {
            try {
                ivProfile.setImageBitmap(ImageUtils.base64ToBitmap(user.getProfileImage()));
            } catch (Exception e) {
                ivProfile.setImageResource(R.drawable.ic_profile);
            }
        }

        if (user.getLastDonation() != null && ! user.getLastDonation().isEmpty()) {
            tvLastDonation.setText(DateUtils.formatForDisplay(user.getLastDonation()));
            tvDonationStatus.setText("Thank you for your donation!");
            tvDonationStatus. setTextColor(getResources().getColor(R.color.success));
        } else {
            tvLastDonation.setText("No donation yet");
            tvDonationStatus.setText("Consider donating blood to save lives!");
            tvDonationStatus.setTextColor(getResources().getColor(R.color.info));
        }
    }

    private void setupClickListeners() {
        cardSearchDonors. setOnClickListener(v -> navigateTo(R.id.nav_search));
        cardEmergency.setOnClickListener(v -> navigateTo(R.id.nav_emergency));
        cardHistory.setOnClickListener(v -> navigateTo(R.id.nav_history));
        cardProfile.setOnClickListener(v -> navigateTo(R. id.nav_profile));
    }

    private void navigateTo(int navItemId) {
        if (getActivity() instanceof UserDashboardActivity) {
            ((UserDashboardActivity) getActivity()).navigateToFragment(navItemId);
        }
    }
}