package com.roktim.blooddonation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.roktim.blooddonation.data.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> getUserById(int id);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserByIdSync(int id);

    @Query("SELECT * FROM users WHERE role = 'user'")
    LiveData<List<User>> getAllDonors();

    @Query("SELECT * FROM users WHERE role = 'user'")
    List<User> getAllDonorsSync();

    @Query("SELECT * FROM users WHERE role = 'user' AND blood_group = :bloodGroup")
    LiveData<List<User>> getDonorsByBloodGroup(String bloodGroup);

    @Query("SELECT * FROM users WHERE role = 'user' AND address LIKE '%' || :location || '%'")
    LiveData<List<User>> getDonorsByLocation(String location);

    @Query("SELECT * FROM users WHERE role = 'user' AND blood_group = :bloodGroup AND address LIKE '%' || :location || '%'")
    LiveData<List<User>> searchDonors(String bloodGroup, String location);

    @Query("SELECT COUNT(*) FROM users")
    LiveData<Integer> getTotalUsersCount();

    @Query("SELECT COUNT(*) FROM users WHERE role = 'user'")
    LiveData<Integer> getTotalDonorsCount();

    @Query("SELECT COUNT(*) FROM users WHERE role = 'user'")
    int getTotalDonorsCountSync();

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);
}