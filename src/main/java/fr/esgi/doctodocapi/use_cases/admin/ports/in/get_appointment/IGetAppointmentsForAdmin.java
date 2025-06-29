package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.GetAppointmentForAdminResponse;

import java.util.List;

public interface IGetAppointmentsForAdmin {
    List<GetAppointmentForAdminResponse> getDoctorAppointment(int size, int page);
}
