package fr.esgi.doctodocapi.use_cases.patient.ports.in.report_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.ReportDoctorRequest;

public interface IReportDoctor {
    void report(ReportDoctorRequest reportDoctorRequest);
}
