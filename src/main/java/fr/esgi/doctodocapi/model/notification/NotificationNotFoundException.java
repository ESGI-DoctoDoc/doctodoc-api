package fr.esgi.doctodocapi.model.notification;

import fr.esgi.doctodocapi.model.DomainException;

public class NotificationNotFoundException extends DomainException {
    private static final String CODE = "notification.not-found";
    private static final String MESSAGE = "Notification not found";

    public NotificationNotFoundException() {
        super(CODE, MESSAGE);
    }
}
