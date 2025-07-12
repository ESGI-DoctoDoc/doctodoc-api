package fr.esgi.doctodocapi.use_cases.patient.report_doctor;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;
import fr.esgi.doctodocapi.model.patient.DoctorReportRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.ReportDoctorRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.report_doctor.IReportDoctor;
import org.springframework.http.HttpStatus;

public class ReportDoctor implements IReportDoctor {
    private final DoctorReportRepository doctorReportRepository;
    private final DoctorRepository doctorRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final MailSender mailSender;

    public ReportDoctor(DoctorReportRepository doctorReportRepository, DoctorRepository doctorRepository, GetPatientFromContext getPatientFromContext, MailSender mailSender) {
        this.doctorReportRepository = doctorReportRepository;
        this.doctorRepository = doctorRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.mailSender = mailSender;
    }

    public void report(ReportDoctorRequest reportDoctorRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Doctor doctor = this.doctorRepository.getById(reportDoctorRequest.doctorId());
            DoctorReport doctorReport = DoctorReport.create(
                    patient.getUserId(),
                    reportDoctorRequest.doctorId(),
                    reportDoctorRequest.explanation()
            );
            this.doctorReportRepository.save(doctorReport);
            sendMail(patient, doctor);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }

    private void sendMail(Patient patient, Doctor doctor) {
        String patientFirstName = patient.getFirstName();
        String doctorFirstName = doctor.getPersonalInformations().getFirstName();
        String doctorLastName = doctor.getPersonalInformations().getLastName();


        String subject = "Confirmation de votre signalement concernant le Dr " + doctorFirstName + " " + doctorLastName;

        String body = String.format("""
                        Bonjour %s,
                        
                        Votre signalement concernant le Dr %s %s a bien été pris en compte par notre équipe.
                        
                        Nous vous remercions de votre vigilance. Si des informations complémentaires sont nécessaires, nous reviendrons vers vous.
                        
                        Cordialement,
                        L’équipe Doctodoc.
                        """,
                patientFirstName,
                doctorFirstName,
                doctorLastName
        );

        this.mailSender.sendMail(
                patient.getEmail().getValue(),
                subject,
                body
        );
    }
}
