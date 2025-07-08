package fr.esgi.doctodocapi.use_cases.doctor.search;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSearchPresentationMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IDoctorSearchFetcher;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.RetrieveDoctorSearchData;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;

import java.util.List;

public class DoctorSearchFetcher implements IDoctorSearchFetcher {

    private final RetrieveDoctorSearchData retrieveDoctorSearchData;
    private final GetCurrentUserContext currentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorSearchPresentationMapper doctorSearchPresentationMapper;
    private final MedicalConcernRepository medicalConcernRepository;

    public DoctorSearchFetcher(RetrieveDoctorSearchData retrieveDoctorSearchData, GetCurrentUserContext currentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, DoctorSearchPresentationMapper doctorSearchPresentationMapper, MedicalConcernRepository medicalConcernRepository) {
        this.retrieveDoctorSearchData = retrieveDoctorSearchData;
        this.currentUserContext = currentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.doctorSearchPresentationMapper = doctorSearchPresentationMapper;
        this.medicalConcernRepository = medicalConcernRepository;
    }

    public List<GetDoctorCareTrackingResponse> searchCareTracking(String patientName, int page, int size) {
        Doctor doctor = getCurrentDoctor();

        List<CareTracking> tracking = retrieveDoctorSearchData.getCareTrackingByDoctor(doctor.getId(), patientName, page, size);
        return tracking.stream().map(doctorSearchPresentationMapper::toCareTrackingResponse).toList();
    }

    public List<GetDoctorMedicalConcernResponse> searchMedicalConcerns(String concernName, int page, int size) {
        Doctor doctor = getCurrentDoctor();
        List<MedicalConcern> concerns = retrieveDoctorSearchData.searchMedicalConcerns(doctor.getId(), concernName, page, size);

        return concerns.stream().map(concern -> {
            List<Question> questions = this.medicalConcernRepository.getDoctorQuestions(concern);
            concern.setQuestions(questions);
            return doctorSearchPresentationMapper.toMedicalConcernResponse(concern);
        }).toList();
    }

    public List<GetSearchAppointmentResponse> searchAppointments(String patientName, int page, int size) {
        Doctor doctor = getCurrentDoctor();

        List<Appointment> appointments = this.retrieveDoctorSearchData.searchAppointmentsByPatientName(doctor.getId(), patientName, page, size);
        return appointments.stream().map(this.doctorSearchPresentationMapper::toAppointmentDto).toList();
    }

    public List<GetDoctorPatientResponse> searchPatients(String name, int page, int size) {
        Doctor doctor = getCurrentDoctor();

        List<Patient> patients = retrieveDoctorSearchData.searchPatientsByDoctor(doctor.getId(), name, page, size);
        return patients.stream().map(doctorSearchPresentationMapper::toPatientResponse).toList();
    }


    private Doctor getCurrentDoctor() {
        String username = currentUserContext.getUsername();
        User user = userRepository.findByEmail(username);
        return doctorRepository.findDoctorByUserId(user.getId());
    }
}
