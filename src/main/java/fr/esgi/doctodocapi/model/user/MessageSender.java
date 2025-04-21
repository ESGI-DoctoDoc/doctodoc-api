package fr.esgi.doctodocapi.model.user;

public interface MessageSender {
    void sendMessage(String phoneNumber, String message) throws MessageFailed;
}
