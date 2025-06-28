package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.GetAppointmentForAdminResponse;

import java.util.UUID;

public interface IGetAppointmentDetailsForAdmin {
    GetAppointmentForAdminResponse execute(UUID appointmentId);
}
