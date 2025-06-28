package fr.esgi.doctodocapi.infrastructure.security.service;

import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetPatientFromContext implements IGetPatientFromContext {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public GetPatientFromContext(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    public Patient get() {
        String username = this.getCurrentUserContext.getUsername();

        User user = this.userRepository.findByEmail(username);

        Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
        if (patient.isEmpty()) throw new PatientNotFoundException();

        return patient.get();
    }
}
