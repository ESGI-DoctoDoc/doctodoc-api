package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IGetAppointmentsAvailabilityForDoctor;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.get_appointment_availability_response.GetTodayAppointmentAvailabilityResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class GetAppointmentsAvailabilityForDoctor implements IGetAppointmentsAvailabilityForDoctor {
    private final MedicalConcernRepository medicalConcernRepository;
    private final AppointmentsAvailabilityService appointmentsAvailabilityService;

    public GetAppointmentsAvailabilityForDoctor(MedicalConcernRepository medicalConcernRepository, AppointmentsAvailabilityService appointmentsAvailabilityService) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.appointmentsAvailabilityService = appointmentsAvailabilityService;
    }

    public List<GetDoctorAppointmentAvailabilityResponse> execute(UUID medicalConcernId, LocalDate date) {
        try {
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);
            List<GetTodayAppointmentAvailabilityResponse> appointments = this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern, date);
            return appointments.stream()
                    .map(appointment -> new GetDoctorAppointmentAvailabilityResponse(
                            appointment.slotId(),
                            appointment.date(),
                            appointment.start(),
                            appointment.end(),
                            appointment.isBooked()
                    ))
                    .sorted(Comparator.comparing(GetDoctorAppointmentAvailabilityResponse::start))
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
