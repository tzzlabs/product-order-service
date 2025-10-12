package com.example.productorderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class SQSService {

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private ObjectMapper objectMapper;

    private final String QUEUE_URL = "https://sqs.eu-north-1.amazonaws.com/079910998608/order-updates-queue";

    public void sendOrderStatusUpdate(Long orderId, String status, String customerEmail) {
        try {
            Map<String, Object> messageBody = new HashMap<>();
            messageBody.put("orderId", orderId);
            messageBody.put("status", status);
            messageBody.put("customerEmail", customerEmail);
            messageBody.put("timestamp", System.currentTimeMillis());

            String messageBodyJson = objectMapper.writeValueAsString(messageBody);

            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(messageBodyJson)
                    .build();

            sqsClient.sendMessage(sendMessageRequest);
            System.out.println("Order status update sent to SQS for order: " + orderId);
        } catch (Exception e) {
            System.err.println("Failed to send SQS message: " + e.getMessage());
        }
    }
}