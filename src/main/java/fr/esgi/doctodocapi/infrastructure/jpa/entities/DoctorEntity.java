package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "doctors")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "doctor_id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "doctor")
    private List<MedicalConcernEntity> medicalConcerns;

    @Column(name = "rpps", nullable = false, unique = true)
    @Length(max = 11, min = 11)
    private String rpps;

    @Column(name = "profile_picture_url", nullable = false)
    private String profilePictureUrl;

    @Column(name ="bio", columnDefinition = "text")
    private String bio;

    @Column(name ="first_name", nullable = false)
    private String firstName;

    @Column(name ="last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "speciality", nullable = false)
    private String speciality;

    @Column(name = "experience_years", nullable = false)
    private Short experienceYears;

    @Column(name = "languages", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> languages;

    @Column(name = "consultation_clinic_price", precision = 10, scale = 2)
    private BigDecimal consultationClinicPrice;

    @Column(name = "address")
    private String address;

    @Column(name = "clinic_latitude", precision = 9, scale = 6)
    private BigDecimal clinicLatitude;

    @Column(name = "clinic_longitude", precision = 9, scale = 6)
    private BigDecimal clinicLongitude;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "accept_public_coverage", nullable = false)
    private boolean acceptPublicCoverage;

    @Column(name = "doctor_documents", columnDefinition = "text[]", nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> doctorDocuments;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Short getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Short experienceYears) {
        this.experienceYears = experienceYears;
    }

    public List<MedicalConcernEntity> getMedicalConcerns() {
        return medicalConcerns;
    }

    public void setMedicalConcerns(List<MedicalConcernEntity> medicalConcerns) {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isAcceptPublicCoverage() {
        return acceptPublicCoverage;
    }

    public void setAcceptPublicCoverage(boolean acceptPublicCoverage) {
        this.acceptPublicCoverage = acceptPublicCoverage;
    }

    public List<String> getDoctorDocuments() {
        return doctorDocuments;
    }

    public void setDoctorDocuments(List<String> doctorDocuments) {
        this.doctorDocuments = doctorDocuments;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DoctorEntity that = (DoctorEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
