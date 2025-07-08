package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;

public interface IGetReferentDoctor {
    GetSearchDoctorResponse process();
}
