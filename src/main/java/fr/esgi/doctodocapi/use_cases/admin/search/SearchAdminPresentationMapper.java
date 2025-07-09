package fr.esgi.doctodocapi.use_cases.admin.search;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SpecialityInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSubscriptionResponse;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class SearchAdminPresentationMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetSearchDoctorForAdminResponse toDoctorDto(Doctor doctor) {
        var personal = doctor.getPersonalInformations();
        var professional = doctor.getProfessionalInformations();
        SpecialityInfo specialityInfo = new SpecialityInfo(
                professional.getSpeciality().getId(),
                professional.getSpeciality().getName()
        );

        return new GetSearchDoctorForAdminResponse(
                doctor.getId(),
                personal.getFirstName(),
                personal.getLastName(),
                doctor.getEmail().getValue(),
                doctor.getPhoneNumber().getValue(),
                specialityInfo,
                doctor.getCreatedAt().format(DATE_FORMATTER)
        );
    }

    public GetSearchAppointmentResponse toAppointmentDto(Appointment appointment) {
        var doctor = appointment.getDoctor();
        var doctorUser = doctor.getPersonalInformations();
        var patient = appointment.getPatient();
        var concern = appointment.getMedicalConcern();

        return new GetSearchAppointmentResponse(
                appointment.getId(),
                new GetSearchAppointmentResponse.AppointmentDoctor(
                        doctor.getId(),
                        doctorUser.getFirstName(),
                        doctorUser.getLastName(),
                        doctor.getEmail().getValue()
                ),
                new GetSearchAppointmentResponse.AppointmentPatient(
                        patient.getId(),
                        patient.getFirstName() + " " + patient.getLastName(),
                        patient.getEmail().getValue(),
                        patient.getPhoneNumber().getValue(),
                        patient.getBirthdate().toString()
                ),
                new GetSearchAppointmentResponse.AppointmentMedicalConcern(
                        concern.getId(),
                        concern.getName()
                ),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getStatus().getValue()
        );
    }

    public GetSearchSpecialityResponse toSpecialityDto(Speciality speciality) {
        return new GetSearchSpecialityResponse(
                speciality.getId(),
                speciality.getName(),
                speciality.getCreatedAt().format(DATE_FORMATTER)
        );
    }

    public GetSearchSubscriptionResponse toSubscriptionDto(DoctorSubscription subscription, Doctor doctor, DoctorInvoice invoice, String status) {
        var personal = doctor.getPersonalInformations();

        return new GetSearchSubscriptionResponse(
                subscription.getId(),
                new GetSearchSubscriptionResponse.Doctor(
                        doctor.getId(),
                        personal.getFirstName(),
                        personal.getLastName(),
                        doctor.getEmail().getValue()
                ),
                subscription.getStartDate().toLocalDate().toString(),
                subscription.getEndDate().toLocalDate().toString(),
                invoice.getAmount().doubleValue(),
                status
        );
    }
}