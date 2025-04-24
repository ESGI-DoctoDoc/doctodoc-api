package fr.esgi.doctodocapi.use_cases.user.ports.in;

import org.springframework.stereotype.Service;

@Service
public interface GetCurrentRequestContext {
    String getCurrentDomain();
}
