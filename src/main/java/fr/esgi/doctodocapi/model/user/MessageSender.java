package fr.esgi.doctodocapi.model.user;

/**
 * Interface for sending SMS or text messages.
 */
public interface MessageSender {

    /**
     * Sends a message to the specified phone number.
     *
     * @param phoneNumber the recipient's phone number
     * @param message     the content of the message
     * @throws MessageFailed if the message could not be sent
     */
    void sendMessage(String phoneNumber, String message) throws MessageFailed;
}
