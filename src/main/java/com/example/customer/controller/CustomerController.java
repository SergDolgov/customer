package com.example.customer.controller;

import com.example.customer.service.CustomerService;
import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CustomerController {

    private static final String ENTITY_NAME = "customer";

    @Value("${spring.application.name}")
    private String applicationName;

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);
        if (customer.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new customer cannot already have an ID");
        }
        final var result = customerRepository.save(customer);

        HttpHeaders headers = new HttpHeaders();
        String message = String.format("A new %s is created with identifier %s", ENTITY_NAME, customer.getId());
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", customer.getId());
        return ResponseEntity.created(new URI("/api/customers/" + result.getId())).headers(headers).body(result);
    }
}
