package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requests.patient.PatientOnBoardingRequest;
import fr.esgi.doctodocapi.dtos.requests.patient.SaveDoctorRecruitmentRequest;
import fr.esgi.doctodocapi.dtos.responses.GetBasicPatientInfo;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.exceptions.on_boarding.HasAlreadyMainAccount;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitment;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class OnBoardingPatient {

    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorRecruitmentRepository doctorRecruitmentRepository;

    public OnBoardingPatient(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository,
                             PatientRepository patientRepository, DoctorRepository doctorRepository,
                             DoctorRecruitmentRepository doctorRecruitmentRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.doctorRecruitmentRepository = doctorRecruitmentRepository;
    }

    public GetBasicPatientInfo process(PatientOnBoardingRequest patientOnBoardingRequest) {
        String firstName = patientOnBoardingRequest.firstName().trim();
        String lastName = patientOnBoardingRequest.lastName().trim();
        LocalDate birthdate = patientOnBoardingRequest.birthdate();
        UUID treatingDoctorId = patientOnBoardingRequest.doctorId();

        String username = this.getCurrentUserContext.getUsername();

        try {

            User user = this.userRepository.findByEmail(username);
            this.checkIfHasMainAccount(user.getId());

            Doctor doctor = null;

            if (treatingDoctorId != null) {
                doctor = this.doctorRepository.getById(treatingDoctorId);
            }

            Patient patient = Patient.createFromOnBoarding(user, firstName, lastName, birthdate, doctor);
            this.patientRepository.save(patient);

            return new GetBasicPatientInfo(
                    patient.getId(),
                    patient.getEmail().getValue(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getPhoneNumber().getValue()
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public void addDoctorForRecruitment(SaveDoctorRecruitmentRequest saveDoctorRecruitmentRequest) {
        String firstName = saveDoctorRecruitmentRequest.firstName().trim();
        String lastName = saveDoctorRecruitmentRequest.lastName().trim();

        DoctorRecruitment doctorToRecruit = DoctorRecruitment.create(firstName, lastName);

        this.doctorRecruitmentRepository.save(doctorToRecruit);
    }

    private void checkIfHasMainAccount(UUID userId) {
        boolean hasAlreadyMainAccount = this.patientRepository.isExistMainAccount(userId);
        if (hasAlreadyMainAccount) {
            throw new HasAlreadyMainAccount();
        }
    }

}
