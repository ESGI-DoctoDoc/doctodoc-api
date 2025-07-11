package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.UpdateSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence.UpdateRangeAbsenceRequest;

import java.util.UUID;

public interface IUpdateAbsence {
    GetAbsenceResponse updateSingleDay(UUID absenceId, UpdateSingleDayAbsenceRequest request);
    GetAbsenceResponse updateRange(UUID id, UpdateRangeAbsenceRequest request);
}
