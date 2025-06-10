package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account;

import fr.esgi.doctodocapi.presentation.patient.dtos.requests.UpdateProfileRequest;
import fr.esgi.doctodocapi.presentation.patient.dtos.responses.GetProfileResponse;

public interface IManageProfile {
    GetProfileResponse update(UpdateProfileRequest updateProfileRequest);
}
