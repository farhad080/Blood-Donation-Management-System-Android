package com.roktim.blooddonation.data.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inventory")
public class Inventory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "blood_group")
    private String bloodGroup;

    @ColumnInfo(name = "units")
    private int units;

    public Inventory(String bloodGroup, int units) {
        this. bloodGroup = bloodGroup;
        this. units = units;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }
}