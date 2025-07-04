package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class TraceAlreadyExistInCareTrackingException extends DomainException {
  private static final String CODE = "trace.already-exist";
  private static final String MESSAGE = "trace already exist in this care tracking.";

  public TraceAlreadyExistInCareTrackingException() {
    super(CODE, MESSAGE);
  }
}