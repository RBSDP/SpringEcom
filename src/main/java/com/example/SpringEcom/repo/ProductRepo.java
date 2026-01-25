package com.example.SpringEcom.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SpringEcom.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    
}
