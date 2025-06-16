package fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment;

import java.util.UUID;

public interface ICancelAppointment {
    void cancel(UUID id);
}
