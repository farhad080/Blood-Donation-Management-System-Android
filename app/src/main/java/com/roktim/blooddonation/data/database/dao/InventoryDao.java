package com.roktim.blooddonation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.roktim.blooddonation.data.database.entity.Inventory;

import java.util.List;

@Dao
public interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy. REPLACE)
    void insert(Inventory inventory);

    @Update
    void update(Inventory inventory);

    @Query("SELECT * FROM inventory")
    LiveData<List<Inventory>> getAllInventory();

    @Query("SELECT * FROM inventory WHERE blood_group = :bloodGroup LIMIT 1")
    Inventory getInventoryByBloodGroup(String bloodGroup);

    @Query("SELECT * FROM inventory WHERE blood_group = :bloodGroup LIMIT 1")
    LiveData<Inventory> getInventoryByBloodGroupLive(String bloodGroup);

    @Query("UPDATE inventory SET units = units + :units WHERE blood_group = :bloodGroup")
    void addUnits(String bloodGroup, int units);

    @Query("UPDATE inventory SET units = units - :units WHERE blood_group = :bloodGroup AND units >= :units")
    void reduceUnits(String bloodGroup, int units);

    @Query("UPDATE inventory SET units = :units WHERE blood_group = :bloodGroup")
    void setUnits(String bloodGroup, int units);

    @Query("SELECT SUM(units) FROM inventory")
    LiveData<Integer> getTotalBloodUnits();

    @Query("SELECT SUM(units) FROM inventory")
    int getTotalBloodUnitsSync();
}