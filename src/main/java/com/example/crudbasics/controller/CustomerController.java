package com.example.crudbasics.controller;

import com.example.crudbasics.model.Customer;
import com.example.crudbasics.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers")
    public List<Customer> list(){
        return service.listAll();
    }
}
