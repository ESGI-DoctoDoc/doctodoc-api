package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.DoctorConsultationInformations;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorMustHaveMajority;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.DoctorProfessionalInformations;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class Doctor extends User {
    private UUID id;
    private DoctorPersonnalInformations personalInformations;
    private DoctorProfessionalInformations professionalInformations;
    private DoctorConsultationInformations consultationInformations;
    private Calendar calendar;
    private boolean isVerified;

    private Doctor(User user, UUID id, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified) {
        super(user.getId(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.isEmailVerified(), user.isDoubleAuthActive(), user.getDoubleAuthCode(), user.getCreatedAt());
        this.id = id;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
    }

    public Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified) {
        super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
    }

    public Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified, Calendar calendar) {
        super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
        this.calendar = calendar;
    }

    private static void verifyAge(LocalDate birthDate) {
        int minimumAgeToHave = 18;

        LocalDate now = LocalDate.now().minusYears(minimumAgeToHave);
        if (birthDate.isAfter(now)) {
            throw new DoctorMustHaveMajority();
        }
    }

    public static Doctor createFromOnBoarding(User user, OnBoardingDoctorRequest onBoardingDoctorRequest) {
        Birthdate birthdate = Birthdate.of(onBoardingDoctorRequest.birthDate());
        verifyAge(birthdate.getValue());
        DoctorPersonnalInformations personalInformations = new DoctorPersonnalInformations(
                onBoardingDoctorRequest.profilePictureUrl(),
                onBoardingDoctorRequest.firstName(),
                onBoardingDoctorRequest.lastName(),
                birthdate
        );

        DoctorProfessionalInformations professionalInformations = new DoctorProfessionalInformations(
                onBoardingDoctorRequest.rpps(),
                onBoardingDoctorRequest.bio(),
                onBoardingDoctorRequest.specialty(),
                onBoardingDoctorRequest.experienceYears(),
                onBoardingDoctorRequest.languages(),
                onBoardingDoctorRequest.doctorDocuments(),
                onBoardingDoctorRequest.acceptPublicCoverage()
        );

        DoctorConsultationInformations consultationInformations = new DoctorConsultationInformations(
                null,
                null,
                null,
                null
        );


        return new Doctor(
                user,
                UUID.randomUUID(),
                personalInformations,
                professionalInformations,
                consultationInformations,
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
