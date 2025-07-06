package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveTokenFcmRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetProfileResponse;

public interface IGetInformations {
    GetProfileResponse getBasicPatientInfo();

    void setTokenFcm(SaveTokenFcmRequest saveTokenFcmRequest);
}
