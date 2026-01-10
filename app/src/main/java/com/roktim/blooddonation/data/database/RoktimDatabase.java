package com.roktim.blooddonation.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.roktim.blooddonation.data.database.dao.DonationDao;
import com.roktim.blooddonation.data.database.dao.InventoryDao;
import com.roktim.blooddonation.data.database.dao.RequestDao;
import com.roktim.blooddonation.data.database.dao.UserDao;
import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.data.database.entity.Inventory;
import com.roktim.blooddonation.data.database.entity.Request;
import com.roktim.blooddonation.data.database.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Inventory.class, Request.class, Donation.class},
        version = 1, exportSchema = false)
public abstract class RoktimDatabase extends RoomDatabase {

    private static volatile RoktimDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract UserDao userDao();
    public abstract InventoryDao inventoryDao();
    public abstract RequestDao requestDao();
    public abstract DonationDao donationDao();

    public static RoktimDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoktimDatabase. class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context. getApplicationContext(),
                                    RoktimDatabase.class,
                                    "roktim_database")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                // Initialize default admin user
                UserDao userDao = INSTANCE.userDao();
                User admin = new User(
                        "Hospital Admin",
                        "O+",
                        "01700000000",
                        "admin@roktim.com",
                        "Dhaka Medical College Hospital",
                        null,
                        null,
                        "admin",
                        "admin123",
                        "admin"
                );
                userDao. insert(admin);

                // Add sample donor users
                User donor1 = new User(
                        "Rahim Ahmed",
                        "A+",
                        "01712345678",
                        "rahim@email.com",
                        "Dhanmondi, Dhaka",
                        null,
                        "2025-10-15",
                        "rahim",
                        "123456",
                        "user"
                );
                userDao.insert(donor1);

                User donor2 = new User(
                        "Karim Khan",
                        "B+",
                        "01812345678",
                        "karim@email.com",
                        "Gulshan, Dhaka",
                        null,
                        "2025-11-20",
                        "karim",
                        "123456",
                        "user"
                );
                userDao.insert(donor2);

                // Initialize blood inventory
                InventoryDao inventoryDao = INSTANCE.inventoryDao();
                String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
                int[] initialUnits = {25, 10, 30, 8, 15, 5, 40, 12};

                for (int i = 0; i < bloodGroups.length; i++) {
                    inventoryDao.insert(new Inventory(bloodGroups[i], initialUnits[i]));
                }
            });
        }
    };
}