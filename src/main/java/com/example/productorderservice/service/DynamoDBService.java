package com.example.productorderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DynamoDBService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    private final String TABLE_NAME = "OrderEvents";

    public void logOrderEvent(Long orderId, String eventType, String details) {
        try {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("eventId", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
            item.put("orderId", AttributeValue.builder().n(orderId.toString()).build());
            item.put("eventType", AttributeValue.builder().s(eventType).build());
            item.put("timestamp", AttributeValue.builder().n(String.valueOf(Instant.now().getEpochSecond())).build());
            item.put("details", AttributeValue.builder().s(details).build());

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(putItemRequest);
            System.out.println("Order event logged to DynamoDB: " + eventType + " for order " + orderId);
        } catch (Exception e) {
            System.err.println("Failed to log DynamoDB event: " + e.getMessage());
        }
    }
}