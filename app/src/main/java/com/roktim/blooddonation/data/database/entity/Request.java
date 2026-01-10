package com.roktim.blooddonation.data.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "requests",
        foreignKeys = @ForeignKey(
                entity = User. class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE))
public class Request {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "patient_name")
    private String patientName;

    @ColumnInfo(name = "blood_group")
    private String bloodGroup;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "date")
    private String date;

    public Request(int userId, String patientName, String bloodGroup,
                   String location, String message, String status, String date) {
        this. userId = userId;
        this.patientName = patientName;
        this. bloodGroup = bloodGroup;
        this. location = location;
        this.message = message;
        this.status = status;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
}