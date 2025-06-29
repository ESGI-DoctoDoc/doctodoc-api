package fr.esgi.doctodocapi.use_cases.admin.dtos.responses;

import java.util.UUID;

public record PatientInfoForAdmin(
      UUID id,
      String name,
      String email,
      String phone,
      String birthdate
) {
}