package com.roktim.blooddonation.ui.auth;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.data.repository.UserRepository;
import com.roktim.blooddonation.utils.Constants;

import java.util.Arrays;
import java.util.Calendar;

public class RegisterDonorActivity extends AppCompatActivity {

    private TextInputEditText etName, etPhone, etEmail, etAddress, etLastDonation, etUsername, etPassword;
    private AutoCompleteTextView spinnerBloodGroup;
    private MaterialButton btnRegister;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_donor);

        userRepository = new UserRepository(getApplication());

        initViews();
        setupSpinner();
        setupDatePicker();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etLastDonation = findViewById(R.id.et_last_donation);
        spinnerBloodGroup = findViewById(R.id.spinner_blood_group);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, Arrays.asList(Constants.BLOOD_GROUPS));
        spinnerBloodGroup.setAdapter(adapter);
    }

    private void setupDatePicker() {
        etLastDonation.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        etLastDonation.setText(date);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> {
            String name = getTextOrEmpty(etName);
            String phone = getTextOrEmpty(etPhone);
            String email = getTextOrEmpty(etEmail);
            String address = getTextOrEmpty(etAddress);
            String lastDonation = getTextOrEmpty(etLastDonation);
            String bloodGroup = spinnerBloodGroup.getText() != null ? spinnerBloodGroup.getText().toString().trim() : "";
            String username = getTextOrEmpty(etUsername);
            String password = getTextOrEmpty(etPassword);

            if (name.isEmpty() || bloodGroup.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User existing = userRepository.getUserByUsernameSync(username);
                if (existing != null) {
                    runOnUiThread(() -> Toast.makeText(this, getString(R.string.username_taken), Toast.LENGTH_SHORT).show());
                    return;
                }

                User user = new User(
                        name,
                        bloodGroup,
                        phone,
                        email,
                        address,
                        null,
                        lastDonation,
                        username,
                        password,
                        Constants.ROLE_USER
                );

                userRepository.insert(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private String getTextOrEmpty(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
