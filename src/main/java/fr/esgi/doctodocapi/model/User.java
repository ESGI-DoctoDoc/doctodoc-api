package fr.esgi.doctodocapi.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class User {
    private UUID id;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean isEmailVerified;
    private boolean isDoubleAuthActive;
    private String doubleAuthCode;
    private LocalDateTime createdAt;

    public User(UUID id, String email, String password, String phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isEmailVerified = isEmailVerified;
        this.isDoubleAuthActive = isDoubleAuthActive;
        this.doubleAuthCode = doubleAuthCode;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isDoubleAuthActive() {
        return isDoubleAuthActive;
    }

    public void setDoubleAuthActive(boolean doubleAuthActive) {
        isDoubleAuthActive = doubleAuthActive;
    }

    public String getDoubleAuthCode() {
        return doubleAuthCode;
    }

    public void setDoubleAuthCode(String doubleAuthCode) {
        this.doubleAuthCode = doubleAuthCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isEmailVerified == user.isEmailVerified && isDoubleAuthActive == user.isDoubleAuthActive && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(doubleAuthCode, user.doubleAuthCode) && Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", isDoubleAuthActive=" + isDoubleAuthActive +
                ", doubleAuthCode='" + doubleAuthCode + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
