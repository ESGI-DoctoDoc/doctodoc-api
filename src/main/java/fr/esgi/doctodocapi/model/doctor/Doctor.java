    package fr.esgi.doctodocapi.model.doctor;

    import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
    import fr.esgi.doctodocapi.model.user.User;
    import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
    import fr.esgi.doctodocapi.model.vo.email.Email;
    import fr.esgi.doctodocapi.model.vo.password.Password;
    import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Objects;
    import java.util.UUID;

    public class Doctor extends User {
        private UUID id;
        private String rpps;
        private String doctorStatus;
        private String profilePictureUrl;
        private String bio;
        private String firstName;
        private String lastName;
        private Birthdate birthDate;
        private String specialty;
        private Short experienceYears;
        private List<String> medicalConcerns;
        private List<String> languages;
        private BigDecimal consultationClinicPrice;
        private String address;
        private BigDecimal clinicLatitude;
        private BigDecimal clinicLongitude;
        private boolean isVerified;
        private boolean acceptPublicCoverage;
        private List<String> doctorDocuments;

        private Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, String rpps, String doctorStatus, String profilePictureUrl, String bio, String firstName, String lastName,
                      Birthdate birthDate, String specialty, Short experienceYears, List<String> medicalConcerns, List<String> languages, boolean isVerified, boolean acceptPublicCoverage, List<String> doctorDocuments) {
            super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
            this.id = id;
            this.rpps = rpps;
            this.doctorStatus = doctorStatus;
            this.profilePictureUrl = profilePictureUrl;
            this.bio = bio;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.specialty = specialty;
            this.experienceYears = experienceYears;
            this.medicalConcerns = medicalConcerns;
            this.languages = languages;
            this.isVerified = isVerified;
            this.acceptPublicCoverage = acceptPublicCoverage;
            this.doctorDocuments = doctorDocuments;
        }

        public Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, String rpps, String doctorStatus, String profilePictureUrl, String bio, String firstName, String lastName,
                      Birthdate birthDate, String specialty, Short experienceYears, List<String> medicalConcerns, List<String> languages, BigDecimal consultationClinicPrice, String address, BigDecimal clinicLatitude, BigDecimal clinicLongitude, boolean isVerified, boolean acceptPublicCoverage, List<String> doctorDocuments) {
            super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
            this.id = id;
            this.rpps = rpps;
            this.doctorStatus = doctorStatus;
            this.profilePictureUrl = profilePictureUrl;
            this.bio = bio;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.specialty = specialty;
            this.experienceYears = experienceYears;
            this.medicalConcerns = medicalConcerns;
            this.languages = languages;
            this.consultationClinicPrice = consultationClinicPrice;
            this.address = address;
            this.clinicLatitude = clinicLatitude;
            this.clinicLongitude = clinicLongitude;
            this.isVerified = isVerified;
            this.acceptPublicCoverage = acceptPublicCoverage;
            this.doctorDocuments = doctorDocuments;
        }

        public static Doctor createFromOnBoarding(User user, OnBoardingDoctorRequest req) {
            return new Doctor(
                    user.getId(),
                    Email.of(user.getEmail().getValue()),
                    null,
                    PhoneNumber.of(user.getPhoneNumber().getValue()),
                    user.isEmailVerified(),
                    user.isDoubleAuthActive(),
                    user.getDoubleAuthCode(),
                    user.getCreatedAt(),
                    user.getId(),
                    req.rpps(),
                    DoctorStatus.PENDING_VALIDATION.getValue(),
                    req.profilePictureUrl(),
                    req.bio(),
                    req.firstName(),
                    req.lastName(),
                    Birthdate.of(req.birthDate()),
                    req.specialty(),
                    req.experienceYears(),
                    req.medicalConcerns(),
                    req.languages(),
                    false,
                    req.acceptPublicCoverage(),
                    req.doctorDocuments()
            );
        }

        public static void validateAccount(Doctor doctor) {
            doctor.setVerified(true);
            doctor.setDoctorStatus(DoctorStatus.ACTIVE.getValue());
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

        public String getDoctorStatus() {
            return doctorStatus;
        }

        public void setDoctorStatus(String doctorStatus) {
            this.doctorStatus = doctorStatus;
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

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
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

        public Birthdate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Birthdate birthDate) {
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

        public UUID getUserId() {
            return super.getId();
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
                    ", doctorStatus=" + doctorStatus +
                    ", profilePictureUrl='" + profilePictureUrl + '\'' +
                    ", bio='" + bio + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", birthDate=" + birthDate +
                    ", specialty='" + specialty + '\'' +
                    ", experienceYears=" + experienceYears +
                    ", medicalConcerns=" + medicalConcerns +
                    ", languages=" + languages +
                    ", consultationClinicPrice=" + consultationClinicPrice +
                    ", address='" + address + '\'' +
                    ", clinicLatitude=" + clinicLatitude +
                    ", clinicLongitude=" + clinicLongitude +
                    ", isVerified=" + isVerified +
                    ", acceptPublicCoverage=" + acceptPublicCoverage +
                    '}';
        }
    }
