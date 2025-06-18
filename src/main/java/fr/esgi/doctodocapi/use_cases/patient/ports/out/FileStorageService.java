package fr.esgi.doctodocapi.use_cases.patient.ports.out;

public interface FileStorageService {
    String getPresignedUrlToUpload(String path);

    void delete();

    String getFile(String path) throws FileNotExistedException;
}
