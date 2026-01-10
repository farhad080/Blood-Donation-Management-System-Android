package com.roktim.blooddonation.data.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "blood_group")
    private String bloodGroup;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "profile_image")
    private String profileImage;

    @ColumnInfo(name = "last_donation")
    private String lastDonation;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "role")
    private String role;

    public User(String name, String bloodGroup, String phone, String email,
                String address, String profileImage, String lastDonation,
                String username, String password, String role) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this. address = address;
        this.profileImage = profileImage;
        this.lastDonation = lastDonation;
        this.username = username;
        this. password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getLastDonation() { return lastDonation; }
    public void setLastDonation(String lastDonation) { this.lastDonation = lastDonation; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
}