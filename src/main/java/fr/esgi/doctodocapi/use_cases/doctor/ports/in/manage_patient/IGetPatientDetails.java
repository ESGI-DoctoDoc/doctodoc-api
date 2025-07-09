package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientDetailResponse;

import java.util.UUID;

public interface IGetPatientDetails {
    GetDoctorPatientDetailResponse execute(UUID patientId);
}