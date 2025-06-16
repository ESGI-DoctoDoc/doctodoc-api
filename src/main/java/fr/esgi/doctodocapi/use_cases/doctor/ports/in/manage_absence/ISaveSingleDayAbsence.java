package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;

public interface ISaveSingleDayAbsence {
    GetAbsenceResponse execute(SaveSingleDayAbsenceRequest request);
}
