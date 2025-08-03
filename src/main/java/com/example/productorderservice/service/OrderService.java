package com.example.productorderservice.service;

import com.example.productorderservice.model.Order;
import com.example.productorderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) {
    	order.setId(null); // ensure it's treated as new
    	order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus("Pending");
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);
        if (order != null) {
            order.setProductName(orderDetails.getProductName());
            order.setQuantity(orderDetails.getQuantity());
            order.setPrice(orderDetails.getPrice());
            order.setCustomerName(orderDetails.getCustomerName());
            order.setCustomerEmail(orderDetails.getCustomerEmail());
            order.setStatus(orderDetails.getStatus());
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
