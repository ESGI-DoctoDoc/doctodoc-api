package fr.esgi.doctodocapi.infrastructure.security.service;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetDoctorFromContext implements IGetDoctorFromContext {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public GetDoctorFromContext(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public Doctor get() {
        String username = this.getCurrentUserContext.getUsername();

        User user = this.userRepository.findByEmail(username);

        Optional<Doctor> doctor = this.doctorRepository.getByUserId(user.getId());
        if (doctor.isEmpty()) throw new PatientNotFoundException();

        return doctor.get();
    }
}
