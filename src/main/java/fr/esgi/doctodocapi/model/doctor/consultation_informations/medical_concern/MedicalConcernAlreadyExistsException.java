package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.DomainException;

public class MedicalConcernAlreadyExistsException extends DomainException {
  private static final String CODE = "medical.concern.already-exist";
  private static final String MESSAGE = "A medical concern with this name already exists for the doctor.";

  public MedicalConcernAlreadyExistsException() {
    super(CODE, MESSAGE);
  }
}
