package com.example.crudbasics.services;

import com.example.crudbasics.model.Customer;
import com.example.crudbasics.repository.CustomerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerInterface repo;

    public List<Customer> listAll() {
        return repo.findAll();
    }

    public void save(Customer customer) {
        repo.save(customer);
    }

    public Customer get(Integer id) {
        return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
