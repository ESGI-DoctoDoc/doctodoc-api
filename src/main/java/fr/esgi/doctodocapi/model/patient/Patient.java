package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Patient extends User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private boolean isMainAccount;

    public Patient(UUID userId, String userEmail, String password, String userPhoneNumber, boolean isEmailVerified,
                   boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, String firstName,
                   String lastName, String email, String phoneNumber, LocalDate birthDate, boolean isMainAccount) {
        super(userId, userEmail, password, userPhoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.isMainAccount = isMainAccount;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isMainAccount() {
        return isMainAccount;
    }

    public void setMainAccount(boolean mainAccount) {
        isMainAccount = mainAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return isMainAccount == patient.isMainAccount && Objects.equals(id, patient.id) && Objects.equals(firstName, patient.firstName) && Objects.equals(lastName, patient.lastName) && Objects.equals(email, patient.email) && Objects.equals(phoneNumber, patient.phoneNumber) && Objects.equals(birthDate, patient.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, birthDate, isMainAccount);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", isMainAccount=" + isMainAccount +
                '}';
    }
}
