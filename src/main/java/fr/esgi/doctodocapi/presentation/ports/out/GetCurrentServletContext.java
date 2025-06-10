package fr.esgi.doctodocapi.presentation.ports.out;

import fr.esgi.doctodocapi.domain.use_cases.user.ports.out.GetCurrentRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Implementation of GetCurrentRequestContext to retrieve the current HTTP request domain URL.
 * Uses Spring's RequestContextHolder to extract protocol, server name, and port.
 */
@Service
public class GetCurrentServletContext implements GetCurrentRequestContext {

    /**
     * Returns the full domain URL of the current HTTP request, including protocol, server name, and port.
     * If the request context is not available, returns an empty string.
     *
     * @return the current domain URL or empty string if unavailable
     */
    @Override
    public String getCurrentDomain() {
        String domainName = "";

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attr != null) {
            String protocol = attr.getRequest().getScheme();
            String serverName = attr.getRequest().getServerName();
            int port = attr.getRequest().getServerPort();

            domainName = protocol + "://" + serverName + ":" + port;
        }
        return domainName;
    }
}
