package fr.esgi.doctodocapi.infrastructure.s3;

import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileNotExistedException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;


@Service
public class S3FileStorageService implements FileStorageService {
    private final S3Client s3Client;
    private final PresignedUrlService presignedUrlService;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public S3FileStorageService(S3Client s3Client, PresignedUrlService presignedUrlService) {
        this.s3Client = s3Client;
        this.presignedUrlService = presignedUrlService;
    }

    @Override
    public String getPresignedUrlToUpload(String path) {
        return this.presignedUrlService.generatePresignedUploadUrl(path);
    }

    @Override
    public void delete(String path) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    @Override
    public String getFile(String path) {
        this.isFileExist(path);
        return this.presignedUrlService.generateDownloadUrl(path);
    }

    public void isFileExist(String key) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            HeadObjectResponse head = s3Client.headObject(headRequest);
            boolean isFile = !key.endsWith("/") || head.contentLength() > 0;

            if (!isFile) throw new FileNotExistedException();

        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new FileNotExistedException();
            }
            throw e;
        }
    }
}
