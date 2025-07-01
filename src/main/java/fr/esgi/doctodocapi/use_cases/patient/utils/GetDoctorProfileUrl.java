package fr.esgi.doctodocapi.use_cases.patient.utils;

import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileNotExistedException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.stereotype.Service;

@Service
public class GetDoctorProfileUrl {
    private final FileStorageService fileStorageService;

    public GetDoctorProfileUrl(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public String getUrl(String path) {
        String profileUrl = path;

        try {
            profileUrl = this.fileStorageService.getFile(path);
        } catch (FileNotExistedException e) {
//            logger.info("Error : code {}, message {}", e.getCode(), e.getMessage());
        }
        return profileUrl;
    }
}
