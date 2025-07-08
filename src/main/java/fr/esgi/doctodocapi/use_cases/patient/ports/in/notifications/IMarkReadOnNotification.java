package fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications;

import java.util.UUID;

public interface IMarkReadOnNotification {
    void process(UUID id);
}
