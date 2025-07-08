package fr.esgi.doctodocapi.use_cases.patient.manage_referent_doctor;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctorPresentationMapper;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor.IGetReferentDoctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class GetReferentDoctor implements IGetReferentDoctor {
    private static final Logger logger = LoggerFactory.getLogger(GetReferentDoctor.class);

    private final GetPatientFromContext getPatientFromContext;
    private final SearchDoctorPresentationMapper searchDoctorPresentationMapper;
    private final DoctorRepository doctorRepository;

    public GetReferentDoctor(GetPatientFromContext getPatientFromContext, SearchDoctorPresentationMapper searchDoctorPresentationMapper, DoctorRepository doctorRepository) {
        this.getPatientFromContext = getPatientFromContext;
        this.searchDoctorPresentationMapper = searchDoctorPresentationMapper;
        this.doctorRepository = doctorRepository;
    }

    public GetSearchDoctorResponse process() {
        try {
            Patient patient = this.getPatientFromContext.get();
            if (patient.getDoctor() != null) {
                Doctor doctor = this.doctorRepository.getById(patient.getDoctor().getId());
                return this.searchDoctorPresentationMapper.toDto(doctor);
            } else {
                logger.info("No referent doctor for this patient.");
                return null;
            }

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
