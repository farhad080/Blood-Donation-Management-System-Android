package com.roktim.blooddonation.ui.user;

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
import com.roktim.blooddonation.ui.auth.LoginActivity;
import com.roktim.blooddonation.ui.user.fragments.DonationHistoryFragment;
import com.roktim.blooddonation.ui.user.fragments.DonorSearchFragment;
import com.roktim.blooddonation.ui.user.fragments.EmergencyRequestFragment;
import com.roktim.blooddonation.ui.user.fragments.ProfileFragment;
import com.roktim.blooddonation.ui.user.fragments.UserHomeFragment;
import com.roktim.blooddonation.utils.SessionManager;

public class UserDashboardActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private SessionManager sessionManager;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R. layout.activity_user_dashboard);

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(UserViewModel. class);

        viewModel.loadUser(sessionManager.getUserId());

        setupToolbar();
        setupBottomNavigation();

        if (savedInstanceState == null) {
            loadFragment(new UserHomeFragment());
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R. id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("রক্তিম");
        }
    }

    private void setupBottomNavigation() {
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id. nav_home) {
                fragment = new UserHomeFragment();
            } else if (itemId == R.id.nav_search) {
                fragment = new DonorSearchFragment();
            } else if (itemId == R.id.nav_emergency) {
                fragment = new EmergencyRequestFragment();
            } else if (itemId == R.id. nav_history) {
                fragment = new DonationHistoryFragment();
            } else if (itemId == R. id.nav_profile) {
                fragment = new ProfileFragment();
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id. fragment_container, fragment)
                    . commit();
            return true;
        }
        return false;
    }

    public void navigateToFragment(int navItemId) {
        bottomNav.setSelectedItemId(navItemId);
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
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public UserViewModel getViewModel() {
        return viewModel;
    }

    public int getCurrentUserId() {
        return sessionManager. getUserId();
    }
}