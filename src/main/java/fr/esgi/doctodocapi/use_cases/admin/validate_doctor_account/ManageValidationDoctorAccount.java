package fr.esgi.doctodocapi.use_cases.admin.validate_doctor_account;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IManageValidationDoctorAccount;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.ManageDoctorValidationAccount;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Service responsible for validating doctor accounts.
 * <p>
 * This service allows an admin to validate a doctor's registration in the system.
 * It uses the doctor's user ID to find the corresponding entity and mark it as validated.
 * </p>
 */
public class ManageValidationDoctorAccount implements IManageValidationDoctorAccount {

    private final DoctorRepository doctorRepository;
    private final ManageDoctorValidationAccount manageDoctorValidationAccount;
    private final MailSender mailSender;

    /**
     * Constructs the service with the required repository.
     *
     * @param doctorRepository the repository to access doctor data
     */
    public ManageValidationDoctorAccount(DoctorRepository doctorRepository, ManageDoctorValidationAccount manageDoctorValidationAccount, MailSender mailSender) {
        this.doctorRepository = doctorRepository;
        this.manageDoctorValidationAccount = manageDoctorValidationAccount;
        this.mailSender = mailSender;
    }

    /**
     * Validates a doctor account based on the given request.
     * <p>
     * Retrieves the doctor by their user ID, validates the account, and persists the change.
     * If the doctor is not found, a {@link DoctorNotFoundException} is thrown.
     * If any domain exception occurs during validation, an {@link ApiException} is thrown.
     * </p>
     *
     * @param doctorId the doctor validation request containing the doctor's user ID
     */
    public void validateDoctorAccount(UUID doctorId) {
        Doctor doctor = this.doctorRepository.findDoctorByUserId(doctorId);

        if (doctor == null) {
            throw new DoctorNotFoundException();
        }

        try {
            this.manageDoctorValidationAccount.validateDoctorAccount(doctor.getId());
            sendValidateAccountMail(doctor);
        } catch (DoctorNotFoundException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public void refuseDoctorAccount(UUID doctorId) {
        Doctor doctor = this.doctorRepository.findDoctorByUserId(doctorId);

        if (doctor == null) {
            throw new DoctorNotFoundException();
        }

        try {
            this.manageDoctorValidationAccount.refuseDoctorAccount(doctor.getId());
            sendRefuseAccountMail(doctor);
        } catch (DoctorNotFoundException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /// Gestion des notifications et mail (à déplacer)

    private void sendValidateAccountMail(Doctor doctor) {
        String doctorFirstName = doctor.getPersonalInformations().getFirstName();
        String doctorLastName = doctor.getPersonalInformations().getLastName();

        String subject = "Validation de votre compte";

        String body = String.format("""
                        Bonjour Dr %s %s,
                        
                        Nous vous informons que votre compte a été validé par notre équipe.
                        Vous avez désormais accès à notre application.
                        
                        Cordialement,
                        L’équipe Doctodoc.
                        """,
                doctorFirstName,
                doctorLastName
        );

        this.mailSender.sendMail(
                doctor.getEmail().getValue(),
                subject,
                body
        );
    }

    private void sendRefuseAccountMail(Doctor doctor) {
        String doctorFirstName = doctor.getPersonalInformations().getFirstName();
        String doctorLastName = doctor.getPersonalInformations().getLastName();

        String subject = "Compte non validé";

        String body = String.format("""
                        Bonjour %s %s,
                        
                        Nous vous informons que votre compte n'a été validé par notre équipe.
                        Merci de nous contacter pour avoir plus d'informations.
                        
                        Cordialement,
                        L’équipe Doctodoc.
                        """,
                doctorFirstName,
                doctorLastName
        );

        this.mailSender.sendMail(
                doctor.getEmail().getValue(),
                subject,
                body
        );
    }
}
