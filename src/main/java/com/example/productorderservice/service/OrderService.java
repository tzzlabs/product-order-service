package com.example.productorderservice.service;

import com.example.productorderservice.dto.OrderRequest;
import com.example.productorderservice.dto.OrderResponse;
import com.example.productorderservice.model.Order;
import com.example.productorderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order != null ? toResponse(order) : null;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = toEntity(orderRequest);
        order.setId(null); // ensure it's treated as new
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pending");
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order existing = orderRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setProductName(orderRequest.getProductName());
            existing.setQuantity(orderRequest.getQuantity());
            existing.setPrice(orderRequest.getPrice());
            existing.setCustomerName(orderRequest.getCustomerName());
            existing.setCustomerEmail(orderRequest.getCustomerEmail());
            existing.setStatus(orderRequest.getStatus());
            return toResponse(orderRepository.save(existing));
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // Mapping: DTO -> Entity
    private Order toEntity(OrderRequest req) {
        Order o = new Order();
        o.setProductName(req.getProductName());
        o.setQuantity(req.getQuantity());
        o.setPrice(req.getPrice());
        o.setStatus(req.getStatus());
        o.setCustomerName(req.getCustomerName());
        o.setCustomerEmail(req.getCustomerEmail());
        o.setOrderDate(req.getOrderDate());
        return o;
    }

    // Mapping: Entity -> DTO
    private OrderResponse toResponse(Order o) {
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setProductName(o.getProductName());
        r.setQuantity(o.getQuantity());
        r.setPrice(o.getPrice());
        r.setStatus(o.getStatus());
        r.setCustomerName(o.getCustomerName());
        r.setCustomerEmail(o.getCustomerEmail());
        r.setOrderDate(o.getOrderDate());
        return r;
    }
}
