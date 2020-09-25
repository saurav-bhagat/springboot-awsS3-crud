package com.example.crudbasics.repository;

import com.example.crudbasics.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInterface extends JpaRepository<Customer, Integer> {
}
