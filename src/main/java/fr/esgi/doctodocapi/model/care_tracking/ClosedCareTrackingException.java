package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class ClosedCareTrackingException extends DomainException {
  private static final String CODE = "care-tracking.is-closed";
  private static final String MESSAGE = "Le suivi de dossier est fermé";

  public ClosedCareTrackingException() {
    super(CODE, MESSAGE);
  }
}