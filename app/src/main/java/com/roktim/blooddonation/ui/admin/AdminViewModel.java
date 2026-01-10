package com.roktim.blooddonation.ui.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.data.database.entity.Inventory;
import com.roktim.blooddonation.data.database.entity.Request;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.data.repository.DonationRepository;
import com.roktim.blooddonation.data.repository.InventoryRepository;
import com.roktim.blooddonation.data.repository.RequestRepository;
import com.roktim.blooddonation.data.repository.UserRepository;

import java.util.List;

public class AdminViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final RequestRepository requestRepository;
    private final DonationRepository donationRepository;

    private LiveData<List<User>> allDonors;
    private LiveData<List<Inventory>> allInventory;
    private LiveData<List<Request>> allRequests;
    private LiveData<List<Request>> pendingRequests;
    private LiveData<List<Donation>> allDonations;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        inventoryRepository = new InventoryRepository(application);
        requestRepository = new RequestRepository(application);
        donationRepository = new DonationRepository(application);
    }

    // Dashboard Stats
    public LiveData<Integer> getTotalUsersCount() {
        return userRepository.getTotalUsersCount();
    }

    public LiveData<Integer> getTotalDonorsCount() {
        return userRepository.getTotalDonorsCount();
    }

    public LiveData<Integer> getTotalBloodUnits() {
        return inventoryRepository.getTotalBloodUnits();
    }

    public LiveData<Integer> getPendingRequestsCount() {
        return requestRepository.getPendingRequestsCount();
    }

    // Donor Management
    public LiveData<List<User>> getAllDonors() {
        if (allDonors == null) {
            allDonors = userRepository.getAllDonors();
        }
        return allDonors;
    }

    public LiveData<List<User>> searchDonors(String bloodGroup, String location) {
        if (bloodGroup != null && !bloodGroup.isEmpty() && location != null && !location.isEmpty()) {
            return userRepository.searchDonors(bloodGroup, location);
        } else if (bloodGroup != null && !bloodGroup. isEmpty()) {
            return userRepository.getDonorsByBloodGroup(bloodGroup);
        } else if (location != null && !location.isEmpty()) {
            return userRepository.getDonorsByLocation(location);
        }
        return getAllDonors();
    }

    public void updateDonor(User user) {
        userRepository.update(user);
    }

    public void deleteDonor(User user) {
        userRepository. delete(user);
    }

    // Inventory Management
    public LiveData<List<Inventory>> getAllInventory() {
        if (allInventory == null) {
            allInventory = inventoryRepository.getAllInventory();
        }
        return allInventory;
    }

    public void addBloodUnits(String bloodGroup, int units) {
        inventoryRepository.addUnits(bloodGroup, units);
    }

    public void reduceBloodUnits(String bloodGroup, int units) {
        inventoryRepository.reduceUnits(bloodGroup, units);
    }

    public void setBloodUnits(String bloodGroup, int units) {
        inventoryRepository.setUnits(bloodGroup, units);
    }

    // Donation Records
    public LiveData<List<Donation>> getAllDonations() {
        if (allDonations == null) {
            allDonations = donationRepository.getAllDonations();
        }
        return allDonations;
    }

    public void addDonation(Donation donation) {
        donationRepository.insert(donation);
    }

    // Request Management
    public LiveData<List<Request>> getAllRequests() {
        if (allRequests == null) {
            allRequests = requestRepository.getAllRequests();
        }
        return allRequests;
    }

    public LiveData<List<Request>> getPendingRequests() {
        if (pendingRequests == null) {
            pendingRequests = requestRepository. getPendingRequests();
        }
        return pendingRequests;
    }

    public void markRequestFulfilled(int requestId) {
        requestRepository.markAsFulfilled(requestId);
    }

    public LiveData<List<User>> getMatchingDonors(String bloodGroup) {
        return userRepository.getDonorsByBloodGroup(bloodGroup);
    }
}