package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.email.Email;
import fr.esgi.doctodocapi.model.user.password.Password;
import fr.esgi.doctodocapi.model.user.phone_number.PhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Doctor extends User {
    private UUID id;
    private String rpps;
    private String profilePictureUrl;
    private String bio;
    private String firstName;
    private String lastName;
    private List<String> specialties;
    private Short experienceYears;
    private List<String> medicalConcerns;
    private List<String> languages;
    private BigDecimal consultationClinicPrice;
    private String address;
    private BigDecimal clinicLatitude;
    private BigDecimal clinicLongitude;
    private boolean isVerified;

    public Doctor(UUID userId, Email userEmail, Password password, PhoneNumber userPhoneNumber, boolean isEmailVerified,
                  boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, String rpps, String profilePictureUrl, String bio, String firstName, String lastName, List<String> specialties, Short experienceYears, List<String> medicalConcerns, List<String> languages, BigDecimal consultationClinicPrice, String address, BigDecimal clinicLatitude, BigDecimal clinicLongitude, boolean isVerified) {
        super(userId, userEmail, password, userPhoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.rpps = rpps;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialties = specialties;
        this.experienceYears = experienceYears;
        this.medicalConcerns = medicalConcerns;
        this.languages = languages;
        this.consultationClinicPrice = consultationClinicPrice;
        this.address = address;
        this.clinicLatitude = clinicLatitude;
        this.clinicLongitude = clinicLongitude;
        this.isVerified = isVerified;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getRpps() {
        return rpps;
    }

    public void setRpps(String rpps) {
        this.rpps = rpps;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public Short getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Short experienceYears) {
        this.experienceYears = experienceYears;
    }

    public List<String> getMedicalConcerns() {
        return medicalConcerns;
    }

    public void setMedicalConcerns(List<String> medicalConcerns) {
        this.medicalConcerns = medicalConcerns;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public BigDecimal getConsultationClinicPrice() {
        return consultationClinicPrice;
    }

    public void setConsultationClinicPrice(BigDecimal consultationClinicPrice) {
        this.consultationClinicPrice = consultationClinicPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getClinicLatitude() {
        return clinicLatitude;
    }

    public void setClinicLatitude(BigDecimal clinicLatitude) {
        this.clinicLatitude = clinicLatitude;
    }

    public BigDecimal getClinicLongitude() {
        return clinicLongitude;
    }

    public void setClinicLongitude(BigDecimal clinicLongitude) {
        this.clinicLongitude = clinicLongitude;
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

    public boolean getVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", rpps='" + rpps + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", bio='" + bio + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialties=" + specialties +
                ", experienceYears=" + experienceYears +
                ", medicalConcerns=" + medicalConcerns +
                ", languages=" + languages +
                ", consultationClinicPrice=" + consultationClinicPrice +
                ", address='" + address + '\'' +
                ", clinicLatitude=" + clinicLatitude +
                ", clinicLongitude=" + clinicLongitude +
                ", isVerified=" + isVerified +
                '}';
    }
}
