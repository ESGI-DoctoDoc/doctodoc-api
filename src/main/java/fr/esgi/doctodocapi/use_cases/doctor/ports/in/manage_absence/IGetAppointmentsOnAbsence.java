package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.GetAppointmentsOnAbsenceBody;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.GetAppointmentsOnDateAbsenceBody;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAppointmentOnAbsenceResponse;

import java.util.List;

public interface IGetAppointmentsOnAbsence {
    List<GetAppointmentOnAbsenceResponse> execute(GetAppointmentsOnAbsenceBody body);
    List<GetAppointmentOnAbsenceResponse> execute(GetAppointmentsOnDateAbsenceBody body);
}
