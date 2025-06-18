package fr.esgi.doctodocapi.use_cases.patient.ports.out;

public interface FileStorageService {
    void upload();

    void delete();

    String getFile(String path) throws FileNotExistedException;
}
