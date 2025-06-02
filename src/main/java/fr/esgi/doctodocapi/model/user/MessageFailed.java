package fr.esgi.doctodocapi.model.user;

public class MessageFailed extends RuntimeException {
  private static final String MESSAGE = "Une erreur a été rencontrée dans l'envoi du message";

    public MessageFailed() {
        super(MESSAGE);
    }
}
