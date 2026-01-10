package com.roktim.blooddonation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.roktim.blooddonation.data.database.entity.Donation;

import java.util.List;

@Dao
public interface DonationDao {

    @Insert
    long insert(Donation donation);

    @Delete
    void delete(Donation donation);

    @Query("SELECT * FROM donations ORDER BY date DESC")
    LiveData<List<Donation>> getAllDonations();

    @Query("SELECT * FROM donations WHERE user_id = :userId ORDER BY date DESC")
    LiveData<List<Donation>> getDonationsByUser(int userId);

    @Query("SELECT * FROM donations WHERE user_id = :userId ORDER BY date DESC")
    List<Donation> getDonationsByUserSync(int userId);

    @Query("SELECT SUM(units) FROM donations")
    LiveData<Integer> getTotalDonatedUnits();

    @Query("SELECT * FROM donations WHERE blood_group = :bloodGroup ORDER BY date DESC")
    LiveData<List<Donation>> getDonationsByBloodGroup(String bloodGroup);
}