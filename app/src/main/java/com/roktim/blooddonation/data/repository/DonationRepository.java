package com.roktim.blooddonation.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.roktim.blooddonation.data.database.RoktimDatabase;
import com.roktim.blooddonation.data.database.dao.DonationDao;
import com.roktim.blooddonation.data.database.dao.InventoryDao;
import com.roktim.blooddonation.data.database.entity.Donation;

import java.util.List;

public class DonationRepository {

    private final DonationDao donationDao;
    private final InventoryDao inventoryDao;

    public DonationRepository(Application application) {
        RoktimDatabase database = RoktimDatabase. getInstance(application);
        donationDao = database.donationDao();
        inventoryDao = database.inventoryDao();
    }

    public void insert(Donation donation) {
        RoktimDatabase.databaseWriteExecutor.execute(() -> {
            donationDao.insert(donation);
            // Automatically update inventory
            inventoryDao.addUnits(donation. getBloodGroup(), donation.getUnits());
        });
    }

    public void delete(Donation donation) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> donationDao.delete(donation));
    }

    public LiveData<List<Donation>> getAllDonations() {
        return donationDao.getAllDonations();
    }

    public LiveData<List<Donation>> getDonationsByUser(int userId) {
        return donationDao.getDonationsByUser(userId);
    }

    public LiveData<Integer> getTotalDonatedUnits() {
        return donationDao.getTotalDonatedUnits();
    }
}