package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;

public interface DoctorReportRepository {
    void save(DoctorReport doctorReport);
}
