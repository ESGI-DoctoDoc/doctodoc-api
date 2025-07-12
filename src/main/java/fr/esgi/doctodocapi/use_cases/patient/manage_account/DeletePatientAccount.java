package fr.esgi.doctodocapi.use_cases.patient.manage_account;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.Anonymizer;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IDeletePatientAccount;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class DeletePatientAccount implements IDeletePatientAccount {
    private final GetPatientFromContext getPatientFromContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public DeletePatientAccount(GetPatientFromContext getPatientFromContext, UserRepository userRepository, PatientRepository patientRepository) {
        this.getPatientFromContext = getPatientFromContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    public void process() {
        try {

            Patient mainPatient = this.getPatientFromContext.get();
            List<Patient> closesMembers = this.patientRepository.getCloseMembers(mainPatient.getUserId());

            List<Patient> allPatientsToAnonymize = new ArrayList<>(closesMembers);
            allPatientsToAnonymize.add(mainPatient);
            User user = this.userRepository.findById(mainPatient.getUserId());

            allPatientsToAnonymize.forEach(Anonymizer::anonymize);
            Anonymizer.anonymize(user);

            allPatientsToAnonymize.forEach(patientRepository::update);
            this.userRepository.anonymise(user);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
