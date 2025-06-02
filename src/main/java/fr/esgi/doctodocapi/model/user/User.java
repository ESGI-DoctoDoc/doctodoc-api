package fr.esgi.doctodocapi.model.user;

import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a system user with authentication and contact details.
 * Contains identity, email, password, phone number, verification status, 2FA settings, and account creation time.
 */
public class User {
    private UUID id;
    private Email email;
    private Password password;
    private PhoneNumber phoneNumber;
    private boolean isEmailVerified;
    private boolean isDoubleAuthActive;
    private String doubleAuthCode;
    private LocalDateTime createdAt;

    private User(Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isEmailVerified = isEmailVerified;
        this.isDoubleAuthActive = isDoubleAuthActive;
        this.doubleAuthCode = doubleAuthCode;
        this.createdAt = createdAt;
    }

    public User(UUID id, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified,
                boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isEmailVerified = isEmailVerified;
        this.isDoubleAuthActive = isDoubleAuthActive;
        this.doubleAuthCode = doubleAuthCode;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create a new User instance from raw String values.
     * Sets emailVerified and doubleAuthActive to false by default.
     *
     * @param emailValue       email as string
     * @param passwordValue    password as string
     * @param phoneNumberValue phone number as string
     * @return new User instance with current timestamp as createdAt
     */
    public static User create(String emailValue, String passwordValue, String phoneNumberValue) {
        Email email = Email.of(emailValue);
        Password password = Password.of(passwordValue);
        PhoneNumber phoneNumber = PhoneNumber.of(phoneNumberValue);

        return new User(
                email,
                password,
                phoneNumber,
                false,
                false,
                null,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
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
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", password=" + password +
                ", phoneNumber=" + phoneNumber +
                ", isEmailVerified=" + isEmailVerified +
                ", isDoubleAuthActive=" + isDoubleAuthActive +
                ", doubleAuthCode='" + doubleAuthCode + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
