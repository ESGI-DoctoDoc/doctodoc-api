package fr.esgi.doctodocapi.use_cases.patient.utils;

import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileNotExistedException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetDoctorProfileUrl {
    private static final Logger logger = LoggerFactory.getLogger(GetDoctorProfileUrl.class);

    private final FileStorageService fileStorageService;

    public GetDoctorProfileUrl(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public String getUrl(String path) {
        String profileUrl = path;

        try {
            profileUrl = this.fileStorageService.getFile(path);
        } catch (FileNotExistedException e) {
            logger.info("Cannot get doctor profile url : code {}, message {}", e.getCode(), e.getMessage());
        }
        return profileUrl;
    }
}
