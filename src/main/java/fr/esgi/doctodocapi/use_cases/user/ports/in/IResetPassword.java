package fr.esgi.doctodocapi.use_cases.user.ports.in;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ResetPasswordRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.UpdatePasswordRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.RequestResetPasswordResponse;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.UpdatePasswordResponse;

public interface IResetPassword {
    RequestResetPasswordResponse requestResetPassword(ResetPasswordRequest request);
    UpdatePasswordResponse updatePassword(UpdatePasswordRequest request);
}
