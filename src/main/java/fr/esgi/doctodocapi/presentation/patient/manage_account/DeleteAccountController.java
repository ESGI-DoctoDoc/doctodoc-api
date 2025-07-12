package fr.esgi.doctodocapi.presentation.patient.manage_account;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IDeletePatientAccount;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
@RequestMapping("/patients")
public class DeleteAccountController {
    private final IDeletePatientAccount deletePatientAccount;

    public DeleteAccountController(IDeletePatientAccount deletePatientAccount) {
        this.deletePatientAccount = deletePatientAccount;
    }

    @PatchMapping("/delete-account")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePatientAccount() {
        this.deletePatientAccount.process();
    }
}
