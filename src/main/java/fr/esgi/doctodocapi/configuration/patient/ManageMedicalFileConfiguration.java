package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.use_cases.patient.manage_medical_file.GetFile;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_file.Upload;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IGetFile;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IUpload;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageMedicalFileConfiguration {

    @Bean
    public IUpload iUpload(FileStorageService fileStorageService) {
        return new Upload(fileStorageService);
    }

    @Bean
    public IGetFile iGetFile(FileStorageService fileStorageService) {
        return new GetFile(fileStorageService);
    }


}
