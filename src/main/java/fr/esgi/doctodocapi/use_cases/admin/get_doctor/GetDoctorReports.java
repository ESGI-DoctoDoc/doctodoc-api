package fr.esgi.doctodocapi.use_cases.admin.get_doctor;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;
import fr.esgi.doctodocapi.model.patient.DoctorReportRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_report_response.GetReportResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_report_response.GetReporterResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorReports;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class GetDoctorReports implements IGetDoctorReports {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorReportRepository doctorReportRepository;

    public GetDoctorReports(DoctorRepository doctorRepository, PatientRepository patientRepository, DoctorReportRepository doctorReportRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorReportRepository = doctorReportRepository;
    }

    public List<GetReportResponse> get(UUID id) {
        try {
            Doctor doctor = this.doctorRepository.getById(id);
            List<DoctorReport> reports = this.doctorReportRepository.getAllByDocteurId(doctor.getId());
            return reports
                    .stream()
                    .map(report -> {
                        Patient patient = this.patientRepository.getByUserId(report.getReporterId()).orElseThrow(PatientNotFoundException::new);

                        GetReporterResponse reporter = new GetReporterResponse(
                                report.getReporterId(),
                                patient.getFirstName(),
                                patient.getLastName(),
                                patient.getEmail().getValue()
                        );
                        return new GetReportResponse(
                                report.getId(),
                                reporter,
                                report.getExplanation(),
                                report.getStatus().name().toLowerCase(),
                                report.getReportedAt()
                        );
                    })
                    .toList();


        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
