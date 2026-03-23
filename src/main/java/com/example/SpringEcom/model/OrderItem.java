package com.example.SpringEcom.model;



import java.math.BigDecimal;

import javax.annotation.processing.Generated;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;    
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor  
public class OrderItem {    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Product product;
    private Integer quantity;
    private BigDecimal totalPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    
}