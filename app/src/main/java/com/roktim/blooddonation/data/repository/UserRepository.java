package com.roktim.blooddonation.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.roktim.blooddonation.data.database.RoktimDatabase;
import com.roktim.blooddonation.data.database.dao.UserDao;
import com.roktim.blooddonation.data.database.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application) {
        RoktimDatabase database = RoktimDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public User login(String username, String password) {
        Future<User> future = RoktimDatabase. databaseWriteExecutor.submit(
                () -> userDao.login(username, password));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(User user) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> userDao.update(user));
    }

    public void delete(User user) {
        RoktimDatabase. databaseWriteExecutor.execute(() -> userDao.delete(user));
    }

    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public User getUserByIdSync(int id) {
        Future<User> future = RoktimDatabase.databaseWriteExecutor.submit(
                () -> userDao.getUserByIdSync(id));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<User>> getAllDonors() {
        return userDao.getAllDonors();
    }

    public LiveData<List<User>> getDonorsByBloodGroup(String bloodGroup) {
        return userDao. getDonorsByBloodGroup(bloodGroup);
    }

    public LiveData<List<User>> getDonorsByLocation(String location) {
        return userDao.getDonorsByLocation(location);
    }

    public LiveData<List<User>> searchDonors(String bloodGroup, String location) {
        return userDao.searchDonors(bloodGroup, location);
    }

    public LiveData<Integer> getTotalUsersCount() {
        return userDao.getTotalUsersCount();
    }

    public LiveData<Integer> getTotalDonorsCount() {
        return userDao.getTotalDonorsCount();
    }

    public User getUserByUsernameSync(String username) {
        Future<User> future = RoktimDatabase.databaseWriteExecutor.submit(
                () -> userDao.getUserByUsername(username));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}