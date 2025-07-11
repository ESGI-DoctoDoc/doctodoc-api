package fr.esgi.doctodocapi.use_cases.user.manage_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.user.InvalidPassword;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.password.WrongPasswordFormatException;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ChangePasswordRequest;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IChangePassword;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

public class ChangePassword implements IChangePassword {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;

    public ChangePassword(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
    }

    public void process(ChangePasswordRequest changePasswordRequest) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            Password newPassword = Password.of(changePasswordRequest.newPassword());
            this.userRepository.changePassword(user.getId(), newPassword.getValue(), changePasswordRequest.oldPassword());

        } catch (WrongPasswordFormatException | InvalidPassword e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (DomainException e) {
            throw new CannotChangePassword();
        }
    }
}
