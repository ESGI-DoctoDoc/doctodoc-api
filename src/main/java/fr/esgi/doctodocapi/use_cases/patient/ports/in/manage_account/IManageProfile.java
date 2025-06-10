package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account;

import fr.esgi.doctodocapi.dtos.requests.patient.UpdateProfileRequest;
import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;

public interface IManageProfile {
    GetProfileResponse update(UpdateProfileRequest updateProfileRequest);
}
