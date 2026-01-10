package com.roktim.blooddonation.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.ui.admin.AdminDashboardActivity;
import com.roktim.blooddonation.ui.user.UserDashboardActivity;
import com.roktim.blooddonation.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private SessionManager sessionManager;

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private MaterialButton btnDonorLogin;
    private MaterialButton btnAdminLogin;
    private MaterialButton btnRegisterDonor;

    private String loginType = "donor"; // donor | admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            redirectToDashboard();
            return;
        }

        initViews();
        initViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnDonorLogin = findViewById(R.id.btn_donor_login);
        btnAdminLogin = findViewById(R.id.btn_admin_login);
        btnRegisterDonor = findViewById(R.id.btn_register_donor);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel. class);

        viewModel.getLoginResult().observe(this, user -> {
            if (user != null) {
                boolean isAdminUser = user.isAdmin();
                if ("admin".equalsIgnoreCase(loginType) && !isAdminUser) {
                    Toast.makeText(this, "Please use an admin account for Admin Login", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("donor".equalsIgnoreCase(loginType) && isAdminUser) {
                    Toast.makeText(this, "Admin account cannot use Donor Login", Toast.LENGTH_SHORT).show();
                    return;
                }

                sessionManager.createLoginSession(
                        user.getId(),
                        user.getRole(),
                        user.getUsername()
                );
                Toast.makeText(this, "Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                redirectToDashboard();
            }
        });

        viewModel. getErrorMessage().observe(this, message -> {
            if (message != null && ! message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnDonorLogin.setOnClickListener(v -> {
            loginType = "donor";
            String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
            viewModel.login(username, password);
        });

        btnAdminLogin.setOnClickListener(v -> {
            loginType = "admin";
            String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
            viewModel.login(username, password);
        });

        btnRegisterDonor.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterDonorActivity.class);
            startActivity(intent);
        });
    }

    private void redirectToDashboard() {
        Intent intent;
        if (sessionManager.isAdmin()) {
            intent = new Intent(this, AdminDashboardActivity.class);
        } else {
            intent = new Intent(this, UserDashboardActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}