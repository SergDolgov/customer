package com.example.customer.repository;

import com.example.customer.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository  extends MongoRepository<Order, String> {
}
