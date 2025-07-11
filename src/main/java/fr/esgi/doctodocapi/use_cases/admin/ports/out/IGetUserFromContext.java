package fr.esgi.doctodocapi.use_cases.admin.ports.out;

import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;

public interface IGetUserFromContext {
    User get() throws UserNotFoundException;
}
