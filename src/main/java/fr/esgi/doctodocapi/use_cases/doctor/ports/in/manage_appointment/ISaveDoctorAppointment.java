package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment.SaveDoctorAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.SaveDoctorAppointmentResponse;

public interface ISaveDoctorAppointment {
    SaveDoctorAppointmentResponse execute(SaveDoctorAppointmentRequest request);
}
