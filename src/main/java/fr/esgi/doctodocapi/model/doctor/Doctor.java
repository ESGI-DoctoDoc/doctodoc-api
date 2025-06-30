package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.model.doctor.calendar.Calendar;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.DoctorConsultationInformations;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorMustHaveMajority;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.model.doctor.personal_information.Gender;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.DoctorProfessionalInformations;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Doctor user with personal, professional, and consultation-related information.
 * <p>
 * Extends the User class and adds doctor-specific data, including verification status and calendar.
 * <p>
 * Enforces that the doctor must be at least 18 years old.
 */
public class Doctor extends User {
    private UUID id;
    private DoctorPersonnalInformations personalInformations;
    private DoctorProfessionalInformations professionalInformations;
    private DoctorConsultationInformations consultationInformations;
    private Calendar calendar;
    private boolean isVerified;
    private boolean isRefused;
    private String customerId;


    /**
     * Private constructor used internally for creating a Doctor from an existing User and doctor-specific data.
     *
     * @param user                     base User instance
     * @param id                       unique identifier of the doctor
     * @param personalInformations     personal information of the doctor
     * @param professionalInformations professional information of the doctor
     * @param consultationInformations consultation-related information
     * @param isVerified               verification status of the doctor
     */
    private Doctor(User user, UUID id, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified, String customerId, boolean isRefused) {
        super(user.getId(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.isEmailVerified(), user.isDoubleAuthActive(), user.getDoubleAuthCode(), user.getCreatedAt());
        this.id = id;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
        this.customerId = customerId;
        this.isRefused = isRefused;
    }


    public Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified, boolean isRefused) {
        super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
        this.isRefused = isRefused;
    }

    /**
     * Full constructor for Doctor including calendar.
     *
     * @param userId                   base User id
     * @param email                    user email
     * @param password                 user password
     * @param phoneNumber              user phone number
     * @param isEmailVerified          email verification status
     * @param isDoubleAuthActive       two-factor authentication status
     * @param doubleAuthCode           two-factor auth code
     * @param createdAt                user creation timestamp
     * @param id                       unique doctor identifier
     * @param personalInformations     personal information of the doctor
     * @param professionalInformations professional information of the doctor
     * @param consultationInformations consultation-related information
     * @param isVerified               doctor verification status
     * @param calendar                 the doctor's calendar
     */
    public Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified, Calendar calendar, String customerId, boolean isRefused) {
        super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
        this.calendar = calendar;
        this.customerId = customerId;
        this.isRefused = isRefused;
    }

    public void validate() {
        this.isVerified = true;
        this.isRefused = false;
    }

    public void refuse() {
        this.isRefused = true;
        this.isVerified = false;
    }

    /**
     * Verifies that the given birthdate corresponds to an adult (18 years or older).
     *
     * @param birthDate the birthdate to check
     * @throws DoctorMustHaveMajority if the doctor is under 18
     */
    private static void verifyAge(LocalDate birthDate) {
        int minimumAgeToHave = 18;

        LocalDate now = LocalDate.now().minusYears(minimumAgeToHave);
        if (birthDate.isAfter(now)) {
            throw new DoctorMustHaveMajority();
        }
    }

    /**
     * Creates a Doctor instance from an onboarding request, verifying age and initializing personal and professional info.
     *
     * @param user the base User instance
     * @param onBoardingDoctorRequest onboarding data for the doctor
     * @return a new Doctor instance (unverified)
     * @throws DoctorMustHaveMajority if the doctor is under 18 years old
     */
    public static Doctor createFromOnBoarding(User user, OnBoardingDoctorRequest onBoardingDoctorRequest, List<Document> uploadedDocuments, String profilePictureUrl) {
        Birthdate birthdate = Birthdate.of(onBoardingDoctorRequest.birthDate());
        verifyAge(birthdate.getValue());
        DoctorPersonnalInformations personalInformations = new DoctorPersonnalInformations(
                profilePictureUrl,
                onBoardingDoctorRequest.firstName(),
                onBoardingDoctorRequest.lastName(),
                birthdate,
                Gender.valueOf(onBoardingDoctorRequest.gender())
        );

        DoctorProfessionalInformations professionalInformations = new DoctorProfessionalInformations(
                onBoardingDoctorRequest.rpps(),
                onBoardingDoctorRequest.bio(),
                onBoardingDoctorRequest.speciality(),
                onBoardingDoctorRequest.experienceYears(),
                onBoardingDoctorRequest.languages(),
                uploadedDocuments,
                onBoardingDoctorRequest.acceptPublicCoverage()
        );

        DoctorConsultationInformations consultationInformations = new DoctorConsultationInformations(
                null,
                null,
                null,
                List.of()
        );


        return new Doctor(
                user,
                UUID.randomUUID(),
                personalInformations,
                professionalInformations,
                consultationInformations,
                false,
                null,
                false
        );
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public DoctorPersonnalInformations getPersonalInformations() {
        return personalInformations;
    }

    public void setPersonalInformations(DoctorPersonnalInformations personalInformations) {
        this.personalInformations = personalInformations;
    }

    public DoctorProfessionalInformations getProfessionalInformations() {
        return professionalInformations;
    }

    public void setProfessionalInformations(DoctorProfessionalInformations professionalInformations) {
        this.professionalInformations = professionalInformations;
    }

    public DoctorConsultationInformations getConsultationInformations() {
        return consultationInformations;
    }

    public void setConsultationInformations(DoctorConsultationInformations consultationInformations) {
        this.consultationInformations = consultationInformations;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public UUID getUserId() {
        return super.getId();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isRefused() {
        return isRefused;
    }

    public void setRefused(boolean refused) {
        isRefused = refused;
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
}
