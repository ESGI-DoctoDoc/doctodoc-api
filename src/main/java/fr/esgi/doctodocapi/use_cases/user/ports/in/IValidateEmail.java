package fr.esgi.doctodocapi.use_cases.user.ports.in;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateEmailRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.ValidateEmailResponse;

public interface IValidateEmail {
    ValidateEmailResponse validate(ValidateEmailRequest request);
}
