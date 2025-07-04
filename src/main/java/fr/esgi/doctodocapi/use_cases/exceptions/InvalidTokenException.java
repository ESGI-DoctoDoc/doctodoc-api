package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
  private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
  private static final String CODE = "invalid.token";
  private static final String MESSAGE = "Une erreur est survenue avec votre session. Veuillez vous reconnecter.";

  public InvalidTokenException() {
    super(STATUS, CODE, MESSAGE);
  }
}
