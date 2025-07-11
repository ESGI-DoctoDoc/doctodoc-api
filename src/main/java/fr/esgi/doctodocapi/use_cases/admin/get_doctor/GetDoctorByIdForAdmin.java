package fr.esgi.doctodocapi.use_cases.admin.get_doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;
import fr.esgi.doctodocapi.model.patient.DoctorReportRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorByIdResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorByIdForAdmin;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class GetDoctorByIdForAdmin implements IGetDoctorByIdForAdmin {

    private final DoctorRepository doctorRepository;
    private final SpecialityRepository specialityRepository;
    private final DoctorSubscriptionRepository subscriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorResponseMapper doctorResponseMapper;
    private final DoctorReportRepository doctorReportRepository;

    public GetDoctorByIdForAdmin(DoctorRepository doctorRepository, SpecialityRepository specialityRepository, DoctorSubscriptionRepository subscriptionRepository, AppointmentRepository appointmentRepository, DoctorResponseMapper doctorResponseMapper, DoctorReportRepository doctorReportRepository) {
        this.doctorRepository = doctorRepository;
        this.specialityRepository = specialityRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorResponseMapper = doctorResponseMapper;
        this.doctorReportRepository = doctorReportRepository;
    }

    public GetDoctorByIdResponse execute(UUID doctorId) {
        try {
            Doctor doctor = this.doctorRepository.getById(doctorId);

            String specialityName = doctor.getProfessionalInformations().getSpeciality().getName();
            Speciality speciality = specialityRepository.findByName(specialityName);

            List<DoctorSubscription> subscriptions = this.subscriptionRepository.findAllByDoctorId(doctorId);

            List<DoctorReport> reports = this.doctorReportRepository.getAllByDocteurId(doctor.getId());
            boolean isReported = !reports.isEmpty();

            int appointmentCount = this.appointmentRepository.countAppointmentsByDoctorId(doctorId);
            int patientCount = this.appointmentRepository.countDistinctPatientsByDoctorId(doctorId);


            return this.doctorResponseMapper.toAdminDetailResponse(
                    doctor,
                    speciality,
                    subscriptions,
                    appointmentCount,
                    patientCount,
                    isReported
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
