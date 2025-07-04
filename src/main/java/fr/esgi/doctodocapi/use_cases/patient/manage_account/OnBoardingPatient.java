package fr.esgi.doctodocapi.use_cases.patient.manage_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitment;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.on_boarding.HasAlreadyMainAccount;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.PatientOnBoardingRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDoctorRecruitmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IOnBoardingPatient;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Service responsible for onboarding new patients into the system.
 * <p>
 * Handles patient creation and optional association with a treating doctor,
 * and allows submission of unregistered doctor suggestions for recruitment.
 * </p>
 */
public class OnBoardingPatient implements IOnBoardingPatient {

    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorRecruitmentRepository doctorRecruitmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ProfilePresentationMapper profilePresentationMapper;

    /**
     * Constructs the OnBoardingPatient service with its dependencies.
     *
     * @param getCurrentUserContext       service for accessing the current user's context
     * @param userRepository              repository for accessing user data
     * @param patientRepository           repository for storing and retrieving patient entities
     * @param doctorRepository            repository for accessing doctor entities
     * @param doctorRecruitmentRepository repository for storing doctor recruitment suggestions
     */
    public OnBoardingPatient(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, DoctorRecruitmentRepository doctorRecruitmentRepository, MedicalRecordRepository medicalRecordRepository, ProfilePresentationMapper profilePresentationMapper) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.doctorRecruitmentRepository = doctorRecruitmentRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.profilePresentationMapper = profilePresentationMapper;
    }

    /**
     * Processes the onboarding of a new patient, creating their account and linking to a treating doctor if provided.
     *
     * @param patientOnBoardingRequest the onboarding form input
     * @return basic patient information after successful registration
     * @throws ApiException if the user already has a main patient account or a domain validation fails
     */
    public GetProfileResponse process(PatientOnBoardingRequest patientOnBoardingRequest) {
        String firstName = patientOnBoardingRequest.firstName().trim();
        String lastName = patientOnBoardingRequest.lastName().trim();
        LocalDate birthdate = patientOnBoardingRequest.birthdate();
        UUID treatingDoctorId = patientOnBoardingRequest.doctorId();
        String gender = patientOnBoardingRequest.gender();

        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);
            this.checkIfHasMainAccount(user.getId());

            Doctor doctor = null;
            if (treatingDoctorId != null) {
                doctor = this.doctorRepository.getById(treatingDoctorId);
            }

            Patient patient = Patient.createFromOnBoarding(user, firstName, lastName, birthdate, doctor, gender);
            Patient patientSaved = this.patientRepository.save(patient);

            MedicalRecord medicalRecord = MedicalRecord.init(patientSaved.getId());
            this.medicalRecordRepository.save(medicalRecord);

            return this.profilePresentationMapper.toDto(patientSaved);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /**
     * Saves a doctor suggestion submitted by a patient when the desired doctor is not in the system.
     *
     * @param saveDoctorRecruitmentRequest the doctor recruitment request containing first and last name
     */
    public void addDoctorForRecruitment(SaveDoctorRecruitmentRequest saveDoctorRecruitmentRequest) {
        String firstName = saveDoctorRecruitmentRequest.firstName().trim();
        String lastName = saveDoctorRecruitmentRequest.lastName().trim();

        DoctorRecruitment doctorToRecruit = DoctorRecruitment.create(firstName, lastName);
        this.doctorRecruitmentRepository.save(doctorToRecruit);
    }

    /**
     * Checks if the user already has a main patient account and throws an exception if so.
     *
     * @param userId the ID of the user to check
     * @throws HasAlreadyMainAccount if a main account already exists for the user
     */
    private void checkIfHasMainAccount(UUID userId) {
        boolean hasAlreadyMainAccount = this.patientRepository.isExistMainAccount(userId);
        if (hasAlreadyMainAccount) {
            throw new HasAlreadyMainAccount();
        }
    }
}
