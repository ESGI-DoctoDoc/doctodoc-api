package fr.esgi.doctodocapi.presentation.doctor.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment.SaveDoctorAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.SaveDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ICancelDoctorAppointment;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IGetAppointmentsAvailabilityForDoctor;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IGetDoctorAppointments;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ISaveDoctorAppointment;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
public class ManageDoctorAppointmentsController {

    private final IGetDoctorAppointments getDoctorAppointments;
    private final ISaveDoctorAppointment saveDoctorAppointment;
    private final IGetAppointmentsAvailabilityForDoctor getAppointmentsAvailabilityForDoctor;
    private final ICancelDoctorAppointment cancelDoctorAppointment;

    public ManageDoctorAppointmentsController(IGetDoctorAppointments getDoctorAppointments, ISaveDoctorAppointment saveDoctorAppointment, IGetAppointmentsAvailabilityForDoctor getAppointmentsAvailabilityForDoctor, ICancelDoctorAppointment cancelDoctorAppointment) {
        this.getDoctorAppointments = getDoctorAppointments;
        this.saveDoctorAppointment = saveDoctorAppointment;
        this.getAppointmentsAvailabilityForDoctor = getAppointmentsAvailabilityForDoctor;
        this.cancelDoctorAppointment = cancelDoctorAppointment;
    }

    @GetMapping("appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorAppointmentResponse> getAllAppointments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getDoctorAppointments.execute(page, size);
    }

    @PostMapping("appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveDoctorAppointmentResponse create(@Valid @RequestBody SaveDoctorAppointmentRequest request) {
        return this.saveDoctorAppointment.execute(request);
    }

    @GetMapping("/medical-concerns/{id}/get-appointments-availability")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorAppointmentAvailabilityResponse> getAppointmentsAvailabilities(@PathVariable UUID id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return this.getAppointmentsAvailabilityForDoctor.execute(id, date);
    }

    @DeleteMapping("appointments/cancel/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable UUID id) {
        this.cancelDoctorAppointment.cancel(id);
    }
}