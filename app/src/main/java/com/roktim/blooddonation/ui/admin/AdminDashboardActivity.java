package com.roktim.blooddonation.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.ui.admin.fragments.AdminHomeFragment;
import com.roktim.blooddonation.ui.admin.fragments.DonationRecordsFragment;
import com.roktim.blooddonation.ui.admin.fragments.DonorManagementFragment;
import com.roktim.blooddonation.ui.admin.fragments.EmergencyRequestsFragment;
import com.roktim.blooddonation.ui.admin.fragments.InventoryFragment;
import com.roktim.blooddonation.ui.auth.LoginActivity;
import com.roktim.blooddonation.utils.SessionManager;

public class AdminDashboardActivity extends AppCompatActivity {

    private AdminViewModel viewModel;
    private SessionManager sessionManager;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(AdminViewModel. class);

        setupToolbar();
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new AdminHomeFragment());
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("রক্তিম - Admin");
        }
    }

    private void setupBottomNavigation() {
        bottomNav = findViewById(R.id. bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                fragment = new AdminHomeFragment();
            } else if (itemId == R.id.nav_donors) {
                fragment = new DonorManagementFragment();
            } else if (itemId == R.id.nav_inventory) {
                fragment = new InventoryFragment();
            } else if (itemId == R.id. nav_donations) {
                fragment = new DonationRecordsFragment();
            } else if (itemId == R.id.nav_requests) {
                fragment = new EmergencyRequestsFragment();
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R. id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu. menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(this, LoginActivity. class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public AdminViewModel getViewModel() {
        return viewModel;
    }
}