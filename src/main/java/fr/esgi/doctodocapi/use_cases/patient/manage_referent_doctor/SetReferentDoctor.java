package fr.esgi.doctodocapi.use_cases.patient.manage_referent_doctor;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctorPresentationMapper;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveReferentDoctorRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor.ISetReferentDoctor;
import org.springframework.http.HttpStatus;

public class SetReferentDoctor implements ISetReferentDoctor {
    private final GetPatientFromContext getPatientFromContext;
    private final SearchDoctorPresentationMapper searchDoctorPresentationMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public SetReferentDoctor(GetPatientFromContext getPatientFromContext, SearchDoctorPresentationMapper searchDoctorPresentationMapper, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.getPatientFromContext = getPatientFromContext;
        this.searchDoctorPresentationMapper = searchDoctorPresentationMapper;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public GetSearchDoctorResponse process(SaveReferentDoctorRequest saveReferentDoctorRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Doctor doctor = this.doctorRepository.getById(saveReferentDoctorRequest.doctorId());
            patient.setDoctor(doctor);

            Patient patientSaved = this.patientRepository.update(patient);
            return this.searchDoctorPresentationMapper.toDto(patientSaved.getDoctor());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
