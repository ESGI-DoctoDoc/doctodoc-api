package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment.UpdateDoctorAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.SaveDoctorAppointmentResponse;

import java.util.UUID;

public interface IUpdateDoctorAppointment {
    SaveDoctorAppointmentResponse execute(UUID appointmentId, UpdateDoctorAppointmentRequest request);
}
