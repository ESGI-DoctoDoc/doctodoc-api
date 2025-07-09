package fr.esgi.doctodocapi.use_cases.patient.report_doctor;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;
import fr.esgi.doctodocapi.model.patient.DoctorReportRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.ReportDoctorRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.report_doctor.IReportDoctor;
import org.springframework.http.HttpStatus;

public class ReportDoctor implements IReportDoctor {
    private final DoctorReportRepository doctorReportRepository;
    private final GetPatientFromContext getPatientFromContext;

    public ReportDoctor(DoctorReportRepository doctorReportRepository, GetPatientFromContext getPatientFromContext) {
        this.doctorReportRepository = doctorReportRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    public void report(ReportDoctorRequest reportDoctorRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            DoctorReport doctorReport = DoctorReport.create(
                    patient.getUserId(),
                    reportDoctorRequest.doctorId(),
                    reportDoctorRequest.explanation()
            );
            this.doctorReportRepository.save(doctorReport);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
