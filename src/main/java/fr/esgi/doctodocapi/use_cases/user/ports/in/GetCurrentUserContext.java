package fr.esgi.doctodocapi.use_cases.user.ports.in;

public interface GetCurrentUserContext {
    String getUsername();
    String[] getRole();

}
