package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_report_response.GetReportResponse;

import java.util.List;
import java.util.UUID;

public interface IGetDoctorReports {
    List<GetReportResponse> get(UUID id);
}
