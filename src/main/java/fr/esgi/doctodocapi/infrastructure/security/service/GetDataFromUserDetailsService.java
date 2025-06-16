package fr.esgi.doctodocapi.infrastructure.security.service;

import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GetDataFromUserDetailsService implements GetCurrentUserContext {

    @Override
    public String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public String getRole() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String[] roles = userDetails.getAuthorities().toString()
                .replace("[", "")
                .replace("]", "")
                .replace("ROLE_", "")
                .split(",");

        return roles.length == 1 ? roles[0] : "";
    }
}
