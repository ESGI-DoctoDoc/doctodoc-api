package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientResponse;

import java.util.List;

public interface IGetDoctorPatients {
    List<GetDoctorPatientResponse> execute(int page, int size);
}
