package fr.esgi.doctodocapi.infrastructure.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class PresignedUrlService {
    private static final int EXPIRATION_URL_GET_IN_HOUR = 1;
    private static final int EXPIRATION_UPLOAD_URL_IN_MINUTES = 10;

    private final S3Presigner presigner;
    @Value("${aws.s3.bucket}")
    private String bucket;

    public PresignedUrlService(S3Presigner presigner) {
        this.presigner = presigner;
    }

    public String generatePresignedUploadUrl(String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(EXPIRATION_UPLOAD_URL_IN_MINUTES))
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = presigner.presignPutObject(presignRequest);

        return presignedPutObjectRequest.url().toString();
    }


    public String generateDownloadUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofHours(EXPIRATION_URL_GET_IN_HOUR))
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);

        return presignedGetObjectRequest.url().toString();
    }
}
