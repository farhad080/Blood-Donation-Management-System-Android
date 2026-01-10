package com.roktim.blooddonation.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.data.database.entity.Request;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.data.repository.DonationRepository;
import com.roktim.blooddonation.data.repository.RequestRepository;
import com.roktim.blooddonation.data.repository.UserRepository;
import com.roktim.blooddonation.utils.Constants;
import com.roktim.blooddonation.utils.DateUtils;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final DonationRepository donationRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private LiveData<List<User>> allDonors;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        requestRepository = new RequestRepository(application);
        donationRepository = new DonationRepository(application);
    }

    public void loadUser(int userId) {
        userRepository.getUserById(userId).observeForever(user -> {
            if (user != null) {
                currentUser.setValue(user);
            }
        });
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<User> getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    public void updateUser(User user) {
        userRepository.update(user);
        currentUser.setValue(user);
    }

    public LiveData<List<User>> getAllDonors() {
        if (allDonors == null) {
            allDonors = userRepository.getAllDonors();
        }
        return allDonors;
    }

    public LiveData<List<User>> searchDonorsByBloodGroup(String bloodGroup) {
        return userRepository.getDonorsByBloodGroup(bloodGroup);
    }

    public LiveData<List<User>> searchDonorsByLocation(String location) {
        return userRepository.getDonorsByLocation(location);
    }

    public LiveData<List<User>> searchDonors(String bloodGroup, String location) {
        if (bloodGroup != null && ! bloodGroup.isEmpty() &&
                location != null && !location.isEmpty()) {
            return userRepository.searchDonors(bloodGroup, location);
        } else if (bloodGroup != null && !bloodGroup. isEmpty()) {
            return userRepository.getDonorsByBloodGroup(bloodGroup);
        } else if (location != null && !location.isEmpty()) {
            return userRepository.getDonorsByLocation(location);
        }
        return getAllDonors();
    }

    public void submitEmergencyRequest(int userId, String patientName, String bloodGroup,
                                       String location, String message) {
        Request request = new Request(
                userId,
                patientName,
                bloodGroup,
                location,
                message,
                Constants.STATUS_PENDING,
                DateUtils. getCurrentDate()
        );
        requestRepository. insert(request);
    }

    public LiveData<List<Request>> getUserRequests(int userId) {
        return requestRepository.getRequestsByUser(userId);
    }

    public LiveData<List<Donation>> getUserDonations(int userId) {
        return donationRepository. getDonationsByUser(userId);
    }
}