package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.GetCareTrackingsResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.IGetCareTrackings;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.*;

public class GetCareTrackings implements IGetCareTrackings {
    private final CareTrackingRepository careTrackingRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final CareTrackingResponseMapper careTrackingResponseMapper;
    private final AppointmentRepository appointmentRepository;

    public GetCareTrackings(CareTrackingRepository careTrackingRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, CareTrackingResponseMapper careTrackingResponseMapper, AppointmentRepository appointmentRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.careTrackingResponseMapper = careTrackingResponseMapper;
        this.appointmentRepository = appointmentRepository;
    }

    public List<GetCareTrackingsResponse> execute(int page, int size) {
        String username = getCurrentUserContext.getUsername();

        try {
            User user = userRepository.findByEmail(username);
            Doctor creator = doctorRepository.findDoctorByUserId(user.getId());

            List<CareTracking> careTrackings = careTrackingRepository.findAll(creator.getId(), page, size);

            return careTrackings.stream()
                    .map(careTracking -> {
                        List<Appointment> appointments = getAppointments(careTracking);

                        List<Doctor> doctors = getDoctorList(careTracking);

                        return careTrackingResponseMapper.toResponse(careTracking, appointments, doctors, creator);
                    })
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public GetCareTrackingsResponse execute(UUID careTrackingId) {

        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor creator = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctor(careTrackingId, creator);

            List<Appointment> appointments = getAppointments(careTracking);

            List<Doctor> doctors = getDoctorList(careTracking);

            return this.careTrackingResponseMapper.toResponse(careTracking, appointments, doctors, creator);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private List<Doctor> getDoctorList(CareTracking careTracking) {
        return careTracking.getDoctors().stream()
                .map(doctorRepository::getById)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Appointment> getAppointments(CareTracking careTracking) {
        return Optional.ofNullable(careTracking.getAppointmentsId())
                .orElseGet(List::of)
                .stream()
                .map(appointmentRepository::getById)
                .filter(Objects::nonNull)
                .toList();
    }
}
