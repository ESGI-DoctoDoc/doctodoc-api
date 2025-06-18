package fr.esgi.doctodocapi.infrastructure.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
public class PresignedUrlService {
    private static final int EXPIRATION_URL_IN_HOUR = 1;
    private final S3Presigner presigner;
    @Value("${aws.s3.bucket}")
    private String bucket;

    public PresignedUrlService(S3Presigner presigner) {
        this.presigner = presigner;
    }

    public String generateDownloadUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofHours(EXPIRATION_URL_IN_HOUR))
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);

        return presignedGetObjectRequest.url().toString();
    }
}
