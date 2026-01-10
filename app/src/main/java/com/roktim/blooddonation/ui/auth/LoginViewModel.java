package com.roktim.blooddonation.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.data.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void login(String username, String password) {
        if (username. isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Please enter both username and password");
            return;
        }

        new Thread(() -> {
            User user = userRepository. login(username, password);
            if (user != null) {
                loginResult. postValue(user);
            } else {
                errorMessage.postValue("Invalid username or password");
            }
        }).start();
    }

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}