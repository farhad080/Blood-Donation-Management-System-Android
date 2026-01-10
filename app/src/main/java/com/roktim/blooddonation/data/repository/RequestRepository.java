package com.roktim.blooddonation.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.roktim.blooddonation.data.database.RoktimDatabase;
import com.roktim.blooddonation.data.database.dao.RequestDao;
import com.roktim.blooddonation.data.database.entity.Request;

import java.util.List;

public class RequestRepository {

    private final RequestDao requestDao;

    public RequestRepository(Application application) {
        RoktimDatabase database = RoktimDatabase. getInstance(application);
        requestDao = database.requestDao();
    }

    public void insert(Request request) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> requestDao.insert(request));
    }

    public void update(Request request) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> requestDao.update(request));
    }

    public void delete(Request request) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> requestDao.delete(request));
    }

    public LiveData<List<Request>> getAllRequests() {
        return requestDao.getAllRequests();
    }

    public LiveData<List<Request>> getRequestsByUser(int userId) {
        return requestDao.getRequestsByUser(userId);
    }

    public LiveData<List<Request>> getPendingRequests() {
        return requestDao.getPendingRequests();
    }

    public LiveData<Integer> getPendingRequestsCount() {
        return requestDao. getPendingRequestsCount();
    }

    public void markAsFulfilled(int requestId) {
        RoktimDatabase. databaseWriteExecutor.execute(() ->
                requestDao. markAsFulfilled(requestId));
    }
}