package fr.esgi.doctodocapi.use_cases.doctor.ports.out;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchCoordinatesResponse;

public interface AddressCoordinatesFetcher {
    FetchCoordinatesResponse fetchCoordinates(String address);
}
