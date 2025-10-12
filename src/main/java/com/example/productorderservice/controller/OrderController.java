package com.example.productorderservice.controller;

import com.example.productorderservice.dto.OrderRequest;
import com.example.productorderservice.dto.OrderResponse;
import com.example.productorderservice.service.OrderService;
import com.example.productorderservice.service.S3Service;
import com.example.productorderservice.service.SQSService;
import com.example.productorderservice.service.DynamoDBService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SQSService sqsService;

    @Autowired
    private DynamoDBService dynamoDBService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can view all orders
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Both users and admins can view specific orders
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse savedOrder = orderService.createOrder(orderRequest);

        // AWS Integrations
        try {
            // 1. Upload invoice to S3
            s3Service.uploadInvoice(
                savedOrder.getId(), 
                savedOrder.getCustomerName(), 
                savedOrder.getPrice() * savedOrder.getQuantity()
            );
            
            // 2. Send order creation notification via SQS
            sqsService.sendOrderStatusUpdate(
                savedOrder.getId(), 
                "CREATED", 
                savedOrder.getCustomerEmail()
            );
            
            // 3. Log order creation event to DynamoDB
            dynamoDBService.logOrderEvent(
                savedOrder.getId(), 
                "ORDER_CREATED", 
                "Order created successfully"
            );
            
        } catch (Exception e) {
            System.err.println("AWS integrations failed: " + e.getMessage());
            // Continue without failing the order creation
        }

        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.updateOrder(id, orderRequest);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        // Log update event to DynamoDB
        dynamoDBService.logOrderEvent(id, "ORDER_UPDATED", "Order status updated to: " + response.getStatus());
        
        // Send status update via SQS
        sqsService.sendOrderStatusUpdate(id, "UPDATED", response.getCustomerEmail());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete orders for now
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        
        // Log deletion event to DynamoDB
        try {
            dynamoDBService.logOrderEvent(id, "ORDER_DELETED", "Order deleted successfully");
        } catch (Exception e) {
            System.err.println("Failed to log deletion event: " + e.getMessage());
        }
        
        return ResponseEntity.noContent().build();
    }
}