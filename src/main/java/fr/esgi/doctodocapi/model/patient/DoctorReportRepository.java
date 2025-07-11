package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;

import java.util.List;
import java.util.UUID;

public interface DoctorReportRepository {
    void save(DoctorReport doctorReport);

    List<DoctorReport> getAllByDocteurId(UUID id);
}
