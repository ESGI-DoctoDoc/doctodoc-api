package fr.esgi.doctodocapi.model.user;

public interface GeneratorToken {
    String generate(String username, String role, Integer expirationInMinutes);
    String extractRole(String token);
    String extractUserName(String token);
}
