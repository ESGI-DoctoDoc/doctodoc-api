package fr.esgi.doctodocapi.presentation.ports.out;

import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class GetCurrentServletContext implements GetCurrentRequestContext {
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
