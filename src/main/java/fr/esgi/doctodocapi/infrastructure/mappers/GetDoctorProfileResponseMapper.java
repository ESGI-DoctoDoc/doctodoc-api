package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.AddressProfileInfo;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetDoctorProfileInformationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.SpecialityProfileInfo;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.SubscriptionProfileInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GetDoctorProfileResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GetDoctorProfileInformationResponse toResponse(Doctor doctor, DoctorSubscription subscription) {
        SubscriptionProfileInfo subscriptionInfo = null;

        if (subscription != null) {
            subscriptionInfo = new SubscriptionProfileInfo(
                    subscription.getId(),
                    formatDate(subscription.getStartDate()),
                    formatDate(subscription.getEndDate())
            );
        }

        return new GetDoctorProfileInformationResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getEmail().getValue(),
                doctor.getPhoneNumber().getValue(),
                new AddressProfileInfo(
                        doctor.getConsultationInformations().getAddress(),
                        doctor.getConsultationInformations().getCoordinatesGps().getClinicLatitude(),
                        doctor.getConsultationInformations().getCoordinatesGps().getClinicLongitude()
                ),
                new SpecialityProfileInfo(
                        doctor.getProfessionalInformations().getSpeciality().getId(),
                        doctor.getProfessionalInformations().getSpeciality().getName()
                ),
                subscriptionInfo,
                doctor.getProfessionalInformations().getBio(),
                doctor.getPersonalInformations().getProfilePictureUrl()
        );
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }
}
