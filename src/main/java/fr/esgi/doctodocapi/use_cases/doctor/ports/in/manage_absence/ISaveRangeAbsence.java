package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveRangeAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;

public interface ISaveRangeAbsence {
    GetAbsenceResponse execute(SaveRangeAbsenceRequest request);
}
