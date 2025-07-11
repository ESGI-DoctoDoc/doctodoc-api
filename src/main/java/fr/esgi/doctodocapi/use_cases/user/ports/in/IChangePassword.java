package fr.esgi.doctodocapi.use_cases.user.ports.in;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ChangePasswordRequest;

public interface IChangePassword {
    void process(ChangePasswordRequest changePasswordRequest);
}
