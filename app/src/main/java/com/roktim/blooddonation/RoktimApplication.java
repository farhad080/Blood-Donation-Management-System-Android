package com.roktim.blooddonation;

import android.app.Application;

import com.roktim.blooddonation.data.database.RoktimDatabase;

public class RoktimApplication extends Application {

    private static RoktimApplication instance;
    private RoktimDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = RoktimDatabase.getInstance(this);
    }

    public static RoktimApplication getInstance() {
        return instance;
    }

    public RoktimDatabase getDatabase() {
        return database;
    }
}