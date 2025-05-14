package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.DoctorConsultationInformations;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.DoctorProfessionalInformations;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Doctor extends User {
    private UUID id;
    private DoctorStatus doctorStatus;
    private DoctorPersonnalInformations personalInformations;
    private DoctorProfessionalInformations professionalInformations;
    private DoctorConsultationInformations consultationInformations;
    private boolean isVerified;
    private boolean acceptPublicCoverage;

    public Doctor(UUID userId, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt, UUID id, DoctorStatus doctorStatus, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified, boolean acceptPublicCoverage) {
        super(userId, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
        this.id = id;
        this.doctorStatus = doctorStatus;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
        this.acceptPublicCoverage = acceptPublicCoverage;
    }

    private Doctor(User user, UUID id, DoctorStatus doctorStatus, DoctorPersonnalInformations personalInformations, DoctorProfessionalInformations professionalInformations, DoctorConsultationInformations consultationInformations, boolean isVerified, boolean acceptPublicCoverage) {
        super(user.getId(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.isEmailVerified(), user.isDoubleAuthActive(), user.getDoubleAuthCode(), user.getCreatedAt());
        this.id = id;
        this.doctorStatus = doctorStatus;
        this.personalInformations = personalInformations;
        this.professionalInformations = professionalInformations;
        this.consultationInformations = consultationInformations;
        this.isVerified = isVerified;
        this.acceptPublicCoverage = acceptPublicCoverage;
    }


    public static Doctor createFromOnBoarding(User user, OnBoardingDoctorRequest onBoardingDoctorRequest) {
        DoctorPersonnalInformations personalInformations = new DoctorPersonnalInformations(
                onBoardingDoctorRequest.profilePictureUrl(),
                onBoardingDoctorRequest.firstName(),
                onBoardingDoctorRequest.lastName(),
                Birthdate.of(onBoardingDoctorRequest.birthDate())
        );

        DoctorProfessionalInformations professionalInformations = new DoctorProfessionalInformations(
                onBoardingDoctorRequest.rpps(),
                onBoardingDoctorRequest.bio(),
                onBoardingDoctorRequest.specialty(),
                onBoardingDoctorRequest.experienceYears(),
                onBoardingDoctorRequest.languages(),
                onBoardingDoctorRequest.doctorDocuments()
        );

        DoctorConsultationInformations consultationInformations = new DoctorConsultationInformations(
                null,
                null,
                null,
                onBoardingDoctorRequest.medicalConcerns()
        );


        return new Doctor(
                user,
                UUID.randomUUID(),
                DoctorStatus.PENDING_VALIDATION,
                personalInformations,
                professionalInformations,
                consultationInformations,
                false,
                onBoardingDoctorRequest.acceptPublicCoverage()
        );
    }

    // todo : in admin
    public static void validateAccount(Doctor doctor) {
        doctor.setVerified(true);
        doctor.setDoctorStatus(DoctorStatus.ACTIVE);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public DoctorStatus getDoctorStatus() {
        return doctorStatus;
    }

    public void setDoctorStatus(DoctorStatus doctorStatus) {
        this.doctorStatus = doctorStatus;
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

    public boolean isAcceptPublicCoverage() {
        return acceptPublicCoverage;
    }

    public void setAcceptPublicCoverage(boolean acceptPublicCoverage) {
        this.acceptPublicCoverage = acceptPublicCoverage;
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
}
