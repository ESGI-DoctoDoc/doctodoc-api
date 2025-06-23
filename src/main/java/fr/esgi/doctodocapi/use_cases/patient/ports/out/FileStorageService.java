package fr.esgi.doctodocapi.use_cases.patient.ports.out;

public interface FileStorageService {
    String getPresignedUrlToUpload(String path);

    void delete(String path);

    String getFile(String path) throws FileNotExistedException;
}
