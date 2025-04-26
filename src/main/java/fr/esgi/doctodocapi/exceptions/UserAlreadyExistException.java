package fr.esgi.doctodocapi.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    // todo : extends an ApiException with a status, a code and a message
    public UserAlreadyExistException() {
        super();
    }
}
