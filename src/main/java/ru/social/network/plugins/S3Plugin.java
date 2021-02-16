package ru.social.network.plugins;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@ConditionalOnProperty(value = "file.store.impl", havingValue = "s3", matchIfMissing = true)
public class S3Plugin implements FilePlugin {

    private final AmazonS3 amazonS3;
    private final String bucketName;


    public S3Plugin(AmazonS3 amazonS3, @Value("${aws.s3.bucket.name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Override
    public void putFile(File file) {
        var putObjectRequest = new PutObjectRequest(bucketName, file.getName(), file);
        amazonS3.putObject(putObjectRequest);
    }

    @Override
    public S3ObjectInputStream getFile(String fileName) {
        var s3object = amazonS3.getObject(bucketName, fileName);
        return s3object.getObjectContent();
    }
}