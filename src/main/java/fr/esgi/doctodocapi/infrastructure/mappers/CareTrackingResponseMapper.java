package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.*;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CareTrackingResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public CareTrackingResponseMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }

    public GetCareTrackingsResponse toResponse(CareTracking careTracking, List<Appointment> appointments, List<Doctor> doctors, Doctor creator, List<String> documentsUrl) {

        List<Doctor> fullDoctorList = new ArrayList<>(doctors);
        fullDoctorList.add(creator);

        List<CareTrackingDoctorInfo> doctorInfos = fullDoctorList.stream()
                .map(doc -> new CareTrackingDoctorInfo(
                        doc.getId(),
                        doc.getPersonalInformations().getFirstName(),
                        doc.getPersonalInformations().getLastName(),
                        this.getDoctorProfileUrl.getUrl(doc.getPersonalInformations().getProfilePictureUrl())
                ))
                .toList();

        return new GetCareTrackingsResponse(
                careTracking.getId(),
                careTracking.getCaseName(),
                careTracking.getCreatedAt().format(DATE_FORMATTER),
                documentsUrl,
                new CareTrackingPatientInfo(
                        careTracking.getPatient().getId(),
                        careTracking.getPatient().getFirstName(),
                        careTracking.getPatient().getLastName(),
                        careTracking.getPatient().getEmail().getValue(),
                        careTracking.getPatient().getPhoneNumber().getValue()
                ),
                doctorInfos,
                appointments.stream()
                        .map(appointment -> new AppointmentInfo(
                                appointment.getId(),
                                new MedicalConcernInfoForCareTracking(
                                        appointment.getMedicalConcern().getId(),
                                        appointment.getMedicalConcern().getName()
                                ),
                                appointment.getDate().format(DATE_FORMATTER),
                                appointment.getHoursRange().getStart().toString(),
                                appointment.getStatus().getValue(),
                                appointment.getDoctorNotes()
                        ))
                        .toList()
        );
    }
}