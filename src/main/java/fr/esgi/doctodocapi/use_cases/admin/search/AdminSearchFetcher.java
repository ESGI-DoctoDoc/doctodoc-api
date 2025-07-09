package fr.esgi.doctodocapi.use_cases.admin.search;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.InvoiceState;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSubscriptionResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.search.IAdminSearchFetcher;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.RetrieveSearchData;

import java.util.List;

public class AdminSearchFetcher implements IAdminSearchFetcher {
    private final RetrieveSearchData retrieveSearchData;
    private final SearchAdminPresentationMapper searchAdminPresentationMapper;
    private final DoctorInvoiceRepository doctorInvoiceRepository;
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;

    public AdminSearchFetcher(RetrieveSearchData retrieveSearchData, SearchAdminPresentationMapper searchAdminPresentationMapper, DoctorInvoiceRepository doctorInvoiceRepository, DoctorSubscriptionRepository doctorSubscriptionRepository) {
        this.retrieveSearchData = retrieveSearchData;
        this.searchAdminPresentationMapper = searchAdminPresentationMapper;
        this.doctorInvoiceRepository = doctorInvoiceRepository;
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
    }

    public List<GetSearchDoctorForAdminResponse> searchDoctors(String name, int page, int size) {
        List<Doctor> doctors = this.retrieveSearchData.getDoctors(name, page, size);
        return doctors.stream().map(this.searchAdminPresentationMapper::toDoctorDto).toList();
    }

    public List<GetSearchAppointmentResponse> searchAppointments(String patientName, int page, int size) {
        List<Appointment> appointments = this.retrieveSearchData.getAppointments(patientName, page, size);
        return appointments.stream().map(this.searchAdminPresentationMapper::toAppointmentDto).toList();
    }

    public List<GetSearchSpecialityResponse> searchSpecialities(String name, int page, int size) {
        List<Speciality> specialities = this.retrieveSearchData.getSpecialities(name, page, size);
        return specialities.stream().map(this.searchAdminPresentationMapper::toSpecialityDto).toList();
    }

    public List<GetSearchSubscriptionResponse> searchSubscriptions(String name, int page, int size) {
        List<Doctor> doctors = retrieveSearchData.getDoctors(name, page, size);

        return doctors.stream()
                .flatMap(doctor -> doctorSubscriptionRepository.findAllByDoctorId(doctor.getId()).stream()
                        .map(subscription -> {
                            DoctorInvoice invoice = doctorInvoiceRepository.findBySubscriptionId(subscription.getId());
                            String status = resolveSubscriptionStatus(subscription, invoice);
                            return searchAdminPresentationMapper.toSubscriptionDto(subscription, doctor, invoice, status);
                        }))
                .toList();
    }

    private String resolveSubscriptionStatus(DoctorSubscription subscription, DoctorInvoice invoice) {
        if (invoice.getState() == InvoiceState.PAYMENT_ERROR) return "payment_error";
        if (subscription.getEndDate().isBefore(java.time.LocalDateTime.now())) return "expired";
        return "active";
    }
}