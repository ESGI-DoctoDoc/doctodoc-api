package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.personal_information.CoordinatesGps;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.UpdateDoctorProfileRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchCoordinatesResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetUpdatedProfileResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IUpdateDoctorProfile;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressCoordinatesFetcher;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UpdateDoctorProfile implements IUpdateDoctorProfile {
    private final IGetDoctorFromContext getDoctorFromContext;
    private final DoctorRepository doctorRepository;
    private final AddressCoordinatesFetcher addressCoordinatesFetcher;
    private final DocumentRepository documentRepository;

    public UpdateDoctorProfile(IGetDoctorFromContext getDoctorFromContext, DoctorRepository doctorRepository, AddressCoordinatesFetcher addressCoordinatesFetcher, DocumentRepository documentRepository) {
        this.getDoctorFromContext = getDoctorFromContext;
        this.doctorRepository = doctorRepository;
        this.addressCoordinatesFetcher = addressCoordinatesFetcher;
        this.documentRepository = documentRepository;
    }

    public GetUpdatedProfileResponse execute(UpdateDoctorProfileRequest request) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();

            FetchCoordinatesResponse coordinatesResponse = this.addressCoordinatesFetcher.fetchCoordinates(request.address());
            CoordinatesGps coordinates = CoordinatesGps.of(
                    coordinatesResponse.latitude(),
                    coordinatesResponse.longitude()
            );

            UUID profilePictureId = UUID.fromString(request.profilePictureUrl());
            Document profilePicture = this.documentRepository.getById(profilePictureId);
            String profilePictureUrl = profilePicture.getPath();

            doctor.update(
                    request.firstname(),
                    request.lastname(),
                    request.address(),
                    request.bio(),
                    coordinates,
                    profilePictureUrl
            );

            Doctor savedDoctor = this.doctorRepository.updateProfile(doctor);

            return new GetUpdatedProfileResponse(
                    savedDoctor.getPersonalInformations().getFirstName(),
                    savedDoctor.getPersonalInformations().getLastName(),
                    savedDoctor.getConsultationInformations().getAddress(),
                    savedDoctor.getProfessionalInformations().getBio(),
                    savedDoctor.getPersonalInformations().getProfilePictureUrl()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
