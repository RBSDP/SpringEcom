
 package com.example.SpringEcom.service;

import java.util.List;
import java.util.UUID;

import com.example.SpringEcom.model.Order;
import com.example.SpringEcom.model.OrderItem;
import com.example.SpringEcom.model.Product;
import com.example.SpringEcom.model.dto.OrderItemRequest;
import com.example.SpringEcom.model.dto.OrderItemResponse;
import com.example.SpringEcom.model.dto.OrderRequest;
import com.example.SpringEcom.model.dto.OrderResponse;
import com.example.SpringEcom.repo.ProductRepo;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<Order> orderItems = new ArrayList<>();
        for(OrderItemRequest itemreq : orderRequest.items()) {
            Product product =  productRepo.findById(itemreq.productId())
                                .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemreq.productId()));

            product.setQuantity(product.getQuantity() - itemreq.quantity());
            productRepo.save(product);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemreq.quantity());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemreq.quantity())));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : savedOrder.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getTotalPrice()
            );
            orderItemResponses.add(itemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                orderItemResponses
        );
        return orderResponse;
    };

    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponse itemResponse = new OrderItemResponse(
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity(),
                        orderItem.getTotalPrice()
                );
                orderItemResponses.add(itemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    orderItemResponses
            );
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}