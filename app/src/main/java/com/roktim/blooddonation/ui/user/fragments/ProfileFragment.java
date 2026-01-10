package com.roktim.blooddonation.ui.user.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.ui.user.UserDashboardActivity;
import com.roktim.blooddonation.ui.user.UserViewModel;
import com.roktim.blooddonation.utils.Constants;
import com.roktim.blooddonation.utils.ImageUtils;

import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private UserViewModel viewModel;
    private User currentUser;

    private CircleImageView ivProfile;
    private MaterialButton btnChangePhoto, btnUpdate;
    private TextView tvName, tvBloodGroup;
    private TextInputEditText etName, etPhone, etEmail, etAddress, etLastDonation;
    private AutoCompleteTextView spinnerBloodGroup;

    private String selectedImageBase64 = null;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result. getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            Bitmap bitmap = ImageUtils.uriToBitmap(requireContext(), imageUri);
                            if (bitmap != null) {
                                ivProfile.setImageBitmap(bitmap);
                                selectedImageBase64 = ImageUtils.bitmapToBase64(bitmap);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Failed to load image", Toast. LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater. inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        initViewModel();
        setupSpinner();
        setupClickListeners();
        observeData();

        return view;
    }

    private void initViews(View view) {
        ivProfile = view. findViewById(R.id.iv_profile);
        btnChangePhoto = view. findViewById(R.id.btn_change_photo);
        btnUpdate = view.findViewById(R.id. btn_update);
        tvName = view.findViewById(R. id.tv_name);
        tvBloodGroup = view. findViewById(R.id.tv_blood_group);
        etName = view.findViewById(R.id. et_name);
        etPhone = view.findViewById(R. id.et_phone);
        etEmail = view.findViewById(R.id.et_email);
        etAddress = view. findViewById(R.id.et_address);
        etLastDonation = view. findViewById(R.id.et_last_donation);
        spinnerBloodGroup = view.findViewById(R.id.spinner_blood_group);
    }

    private void initViewModel() {
        if (getActivity() instanceof UserDashboardActivity) {
            viewModel = ((UserDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout. simple_dropdown_item_1line,
                Arrays. asList(Constants. BLOOD_GROUPS)
        );
        spinnerBloodGroup. setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnChangePhoto.setOnClickListener(v -> openImagePicker());
        etLastDonation.setOnClickListener(v -> showDatePicker());
        btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void observeData() {
        if (viewModel == null) return;

        viewModel. getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                currentUser = user;
                populateUI(user);
            }
        });
    }

    private void populateUI(User user) {
        tvName.setText(user.getName());
        tvBloodGroup. setText(user.getBloodGroup());

        etName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etEmail.setText(user. getEmail());
        etAddress.setText(user.getAddress());
        spinnerBloodGroup. setText(user.getBloodGroup(), false);

        if (user.getLastDonation() != null) {
            etLastDonation.setText(user.getLastDonation());
        }

        if (user. getProfileImage() != null && !user. getProfileImage().isEmpty()) {
            try {
                ivProfile.setImageBitmap(ImageUtils. base64ToBitmap(user.getProfileImage()));
            } catch (Exception e) {
                ivProfile.setImageResource(R.drawable. ic_profile);
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore. Images.Media. EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar. getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    etLastDonation.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateProfile() {
        if (currentUser == null) return;

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String address = etAddress.getText() != null ? etAddress. getText().toString().trim() : "";
        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String lastDonation = etLastDonation.getText() != null ?
                etLastDonation. getText().toString().trim() : "";

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        currentUser.setName(name);
        currentUser.setPhone(phone);
        currentUser.setEmail(email);
        currentUser.setAddress(address);
        currentUser.setBloodGroup(bloodGroup);
        currentUser. setLastDonation(lastDonation);

        if (selectedImageBase64 != null) {
            currentUser.setProfileImage(selectedImageBase64);
        }

        viewModel.updateUser(currentUser);
        Toast.makeText(getContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
    }
}