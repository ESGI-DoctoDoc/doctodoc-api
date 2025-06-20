package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;

import java.time.LocalDate;
import java.util.List;

public interface IGetAbsences {
    List<GetAbsenceResponse> execute(int page, int size, LocalDate startDate);
}
