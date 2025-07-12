package fr.esgi.doctodocapi.use_cases.patient.manage_account;

import fr.esgi.doctodocapi.infrastructure.security.service.GetUserFromContext;
import fr.esgi.doctodocapi.model.Anonymizer;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IDeletePatientAccount;
import org.springframework.http.HttpStatus;

public class DeletePatientAccount implements IDeletePatientAccount {
    private final GetUserFromContext getUserFromContext;
    private final UserRepository userRepository;

    public DeletePatientAccount(GetUserFromContext getUserFromContext, UserRepository userRepository) {
        this.getUserFromContext = getUserFromContext;
        this.userRepository = userRepository;
    }

    public void process() {
        try {
            User user = this.getUserFromContext.get();
            Anonymizer.anonymize(user);
            this.userRepository.anonymise(user);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
