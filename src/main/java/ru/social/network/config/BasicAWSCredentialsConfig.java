package ru.social.network.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicAWSCredentialsConfig {
    private static final Logger LOG = LoggerFactory.getLogger(BasicAWSCredentialsConfig.class);
    private final String accessKey;
    private final String secretKey;

    public BasicAWSCredentialsConfig(@Value("${aws.access.key}") String accessKey,
                                     @Value("${aws.secret.key}") String secretKey) {
        LOG.info("AWSCredentialsConfig: accessKey={} secretKey{}", accessKey, secretKey);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Bean
    public BasicAWSCredentials getBasicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 getAmazonS3(BasicAWSCredentials credentials) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_WEST_1)
                .build();
    }
}
