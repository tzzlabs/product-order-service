package com.example.productorderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    private final String BUCKET_NAME = "product-order-invoices";

    public void uploadInvoice(Long orderId, String customerName, double amount) {
        try {
            String fileName = "invoice-order-" + orderId + ".txt";
            String invoiceContent = generateInvoiceContent(orderId, customerName, amount);
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType("text/plain")
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromBytes(invoiceContent.getBytes(StandardCharsets.UTF_8)));

            System.out.println("Invoice uploaded to S3: " + fileName);
        } catch (Exception e) {
            System.err.println("Failed to upload invoice to S3: " + e.getMessage());
        }
    }

    private String generateInvoiceContent(Long orderId, String customerName, double amount) {
        return String.format(
            "INVOICE\nOrder ID: %d\nCustomer: %s\nAmount: $%.2f\nDate: %s\nStatus: PAID",
            orderId, customerName, amount, 
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}