package fr.esgi.doctodocapi.model.doctor.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class DoctorAlreadyExistInCareTrackingException extends DomainException {
  private static final String CODE = "doctor.already-exist";
  private static final String MESSAGE = "doctor already exist in this care tracking.";

  public DoctorAlreadyExistInCareTrackingException() {
    super(CODE, MESSAGE);
  }
}
