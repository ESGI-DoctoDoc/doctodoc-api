package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.CounterInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SpecialityInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SubscriptionInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.AddressInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorByIdResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorForAdminResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorResponseMapper {
    public GetDoctorForAdminResponse toAdminResponse(Doctor doctor, Speciality speciality) {
        return new GetDoctorForAdminResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getEmail().getValue(),
                doctor.getPhoneNumber().getValue(),
                doctor.getPersonalInformations().getBirthDate().getValue().toString(),
                doctor.getProfessionalInformations().getRpps().getValue(),
                doctor.isVerified(),
                doctor.getCreatedAt().toString(),
                new SpecialityInfo(
                        speciality.getId(),
                        speciality.getName()
                ),
                new AddressInfo(
                        doctor.getConsultationInformations().getAddress(),
                        doctor.getConsultationInformations().getCoordinatesGps().getClinicLatitude(),
                        doctor.getConsultationInformations().getCoordinatesGps().getClinicLatitude()
                )
        );
    }

    public GetDoctorByIdResponse toAdminDetailResponse(Doctor doctor, Speciality speciality, List<DoctorSubscription> subscriptions, int appointmentCount, int patientCount) {
        return new GetDoctorByIdResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getEmail().getValue(),
                doctor.getPhoneNumber().getValue(),
                doctor.getPersonalInformations().getBirthDate().getValue().toString(),
                doctor.getProfessionalInformations().getRpps().getValue(),
                doctor.isVerified(),
                doctor.isEmailVerified(),
                doctor.getCreatedAt().toString(),
                new SpecialityInfo(speciality.getId(), speciality.getName()),
                subscriptions.stream().map(subscription -> new SubscriptionInfo(
                        subscription.getId(),
                        subscription.getStartDate().toString(),
                        subscription.getEndDate().toString()
                )).toList(),
                new CounterInfo(
                        appointmentCount,
                        patientCount)
        );
    }
}
