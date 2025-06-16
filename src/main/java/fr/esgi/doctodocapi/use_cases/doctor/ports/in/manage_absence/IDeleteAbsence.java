package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.DeleteAbsenceResponse;

import java.util.UUID;

public interface IDeleteAbsence {
    DeleteAbsenceResponse execute(UUID absenceId);
}
