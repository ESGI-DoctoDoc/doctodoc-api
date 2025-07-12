package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentForAbsenceMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.GetAppointmentsOnAbsenceBody;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.GetAppointmentsOnDateAbsenceBody;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAppointmentOnAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IGetAppointmentsOnAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class GetAppointmentsOnAbsence implements IGetAppointmentsOnAbsence {
    private static final List<String> VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN = Arrays.asList(
            AppointmentStatus.CONFIRMED.getValue(),
            AppointmentStatus.UPCOMING.getValue(),
            AppointmentStatus.WAITING_ROOM.getValue()
    );


    private final AppointmentRepository appointmentRepository;
    private final IGetDoctorFromContext getDoctorFromContext;
    private final AppointmentForAbsenceMapper appointmentForAbsenceMapper;

    public GetAppointmentsOnAbsence(AppointmentRepository appointmentRepository, IGetDoctorFromContext getDoctorFromContext, AppointmentForAbsenceMapper appointmentForAbsenceMapper) {
        this.appointmentRepository = appointmentRepository;
        this.getDoctorFromContext = getDoctorFromContext;
        this.appointmentForAbsenceMapper = appointmentForAbsenceMapper;
    }

    public List<GetAppointmentOnAbsenceResponse> execute(GetAppointmentsOnAbsenceBody body) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();

            List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorIdAndDateRangeAndHourRange(
                    doctor.getId(),
                    body.start(),
                    body.end(),
                    body.startHour(),
                    body.endHour(),
                    VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN
            );

            return appointmentForAbsenceMapper.toResponseList(appointments);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public List<GetAppointmentOnAbsenceResponse> execute(GetAppointmentsOnDateAbsenceBody body) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();

            List<Appointment> appointments = appointmentRepository
                    .findAppointmentsByDoctorIdAndDate(doctor.getId(), body.date(), VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN);

            return appointmentForAbsenceMapper.toResponseList(appointments);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
