package com.taskit.rpadservice.Infrastucture.Outbound.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@Component
public class S3ConnectionTest implements CommandLineRunner {

    private final S3Client s3Client;

    public S3ConnectionTest(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void run(String... args) {
        ListBucketsResponse response = s3Client.listBuckets();
        response.buckets().forEach(b ->
                System.out.println("Bucket: " + b.name() + " | region: " + b.creationDate()));
    }
}
