package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Patient user with personal information, birthdate, linked doctor, and account details.
 * <p>
 * Extends User and adds patient-specific fields such as first and last name, linked doctor, birthdate,
 * and a flag indicating if this is the main account.
 * <p>
 * Enforces minimum age of 18 years for patient creation.
 */
public class Patient extends User {
    private UUID id;
    private Doctor doctor;
    private String firstName;
    private String lastName;
    private Email email;
    private PhoneNumber phoneNumber;
    private Birthdate birthdate;
    private boolean isMainAccount;


    public Patient(UUID userId, Email emailUser, Password password, PhoneNumber userPhoneNumber, boolean isEmailVerified,
                   boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, Doctor doctor,
                   String firstName, String lastName, Email email, PhoneNumber phoneNumber, Birthdate birthdate, boolean isMainAccount) {
        super(userId, emailUser, password, userPhoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.doctor = doctor;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.isMainAccount = isMainAccount;
    }

    private Patient(User user, Doctor doctor, String firstName, String lastName, Email email, PhoneNumber phoneNumber,
                    Birthdate birthdate, boolean isMainAccount) {
        super(user.getId(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.isEmailVerified(), user.isDoubleAuthActive(),
                user.getDoubleAuthCode(), user.getCreatedAt());

        this.id = UUID.randomUUID();
        if (doctor != null) {
            this.doctor = doctor;
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.isMainAccount = isMainAccount;
    }

    /**
     * Creates a Patient from onboarding data.
     * Checks that the patient is at least 18 years old.
     *
     * @param user           base User instance
     * @param firstName      patient's first name
     * @param lastName       patient's last name
     * @param birthDateValue patient's birthdate
     * @param doctor         linked Doctor
     * @return new Patient instance marked as main account
     * @throws PatientMustHaveMajority if patient is under 18 years old
     */
    public static Patient createFromOnBoarding(User user, String firstName, String lastName, LocalDate birthDateValue, Doctor doctor) {
        Birthdate birthDate = Birthdate.of(birthDateValue);
        verifyAge(birthDate.getValue());
        return new Patient(user, doctor, firstName, lastName, user.getEmail(), user.getPhoneNumber(), birthDate, true);
    }


    /**
     * Verifies that the birthdate indicates an adult (18+ years).
     *
     * @param birthDate date of birth to verify
     * @throws PatientMustHaveMajority if patient is underage
     */
    private static void verifyAge(LocalDate birthDate) {
        int minimumAgeToHave = 18;

        LocalDate now = LocalDate.now().minusYears(minimumAgeToHave);
        if (birthDate.isAfter(now)) {
            throw new PatientMustHaveMajority();
        }
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
    public Email getEmail() {
        return email;
    }

    @Override
    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Birthdate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Birthdate birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isMainAccount() {
        return isMainAccount;
    }

    public void setMainAccount(boolean mainAccount) {
        isMainAccount = mainAccount;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public UUID getUserId() {
        return super.getId();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return isMainAccount == patient.isMainAccount && Objects.equals(id, patient.id) && Objects.equals(firstName, patient.firstName) && Objects.equals(lastName, patient.lastName) && Objects.equals(email, patient.email) && Objects.equals(phoneNumber, patient.phoneNumber) && Objects.equals(birthdate, patient.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, birthdate, isMainAccount);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", birthdate=" + birthdate +
                ", isMainAccount=" + isMainAccount +
                '}';
    }
}
