package com.roktim.blooddonation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.roktim.blooddonation.data.database.entity.Request;

import java.util.List;

@Dao
public interface RequestDao {

    @Insert
    long insert(Request request);

    @Update
    void update(Request request);

    @Delete
    void delete(Request request);

    @Query("SELECT * FROM requests ORDER BY date DESC")
    LiveData<List<Request>> getAllRequests();

    @Query("SELECT * FROM requests WHERE user_id = :userId ORDER BY date DESC")
    LiveData<List<Request>> getRequestsByUser(int userId);

    @Query("SELECT * FROM requests WHERE status = 'pending' ORDER BY date DESC")
    LiveData<List<Request>> getPendingRequests();

    @Query("SELECT COUNT(*) FROM requests WHERE status = 'pending'")
    LiveData<Integer> getPendingRequestsCount();

    @Query("SELECT COUNT(*) FROM requests WHERE status = 'pending'")
    int getPendingRequestsCountSync();

    @Query("UPDATE requests SET status = 'fulfilled' WHERE id = :requestId")
    void markAsFulfilled(int requestId);

    @Query("SELECT * FROM requests WHERE id = :id")
    Request getRequestById(int id);
}