package com.roktim.blooddonation.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.roktim.blooddonation.data.database.RoktimDatabase;
import com.roktim.blooddonation.data.database.dao.InventoryDao;
import com.roktim.blooddonation.data.database.entity.Inventory;

import java.util.List;

public class InventoryRepository {

    private final InventoryDao inventoryDao;

    public InventoryRepository(Application application) {
        RoktimDatabase database = RoktimDatabase.getInstance(application);
        inventoryDao = database. inventoryDao();
    }

    public LiveData<List<Inventory>> getAllInventory() {
        return inventoryDao.getAllInventory();
    }

    public void addUnits(String bloodGroup, int units) {
        RoktimDatabase. databaseWriteExecutor.execute(() ->
                inventoryDao.addUnits(bloodGroup, units));
    }

    public void reduceUnits(String bloodGroup, int units) {
        RoktimDatabase.databaseWriteExecutor. execute(() ->
                inventoryDao. reduceUnits(bloodGroup, units));
    }

    public void setUnits(String bloodGroup, int units) {
        RoktimDatabase. databaseWriteExecutor.execute(() ->
                inventoryDao.setUnits(bloodGroup, units));
    }

    public LiveData<Integer> getTotalBloodUnits() {
        return inventoryDao.getTotalBloodUnits();
    }

    public LiveData<Inventory> getInventoryByBloodGroup(String bloodGroup) {
        return inventoryDao.getInventoryByBloodGroupLive(bloodGroup);
    }
}