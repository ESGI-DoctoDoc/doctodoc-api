package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveReferentDoctorRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;

public interface ISetReferentDoctor {
    GetSearchDoctorResponse process(SaveReferentDoctorRequest saveReferentDoctorRequest);
}
