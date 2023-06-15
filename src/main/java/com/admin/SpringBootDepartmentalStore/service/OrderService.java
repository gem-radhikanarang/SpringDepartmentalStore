package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.bean.Order;
import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.repository.CustomerRepository;
import com.admin.SpringBootDepartmentalStore.repository.OrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductInventoryService productInventoryService;

    @Autowired
    private BackOrderService backOrderService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    public void updateOtherEntities(Order order) {
        Customer customer = customerRepository.findById(order.getCustomer().getCustomerId()).orElse(null);
        ProductInventory productInventory = productInventoryRepository.findById(order.getProduct().getProductId()).orElse(null);
        order.setCustomer(customer);
        order.setProduct(productInventory);
    }

    public void checkProductAvailability(Order order)
    {
        ProductInventory productInventory = order.getProduct();
        if(order.getProduct().getCount() > 0 && order.getProduct().isAvailability()) {
            orderRepository.save(order);
            productInventory.setCount(productInventory.getCount() - order.getQuantity());
            productInventoryRepository.save(productInventory);
        }
        else {
            Order savedOrder = orderRepository.save(order);
            // Create a backorder for the order
            BackOrder backorder = new BackOrder();
            backorder.setOrder(savedOrder);
            backOrderService.createBackOrder(backorder);
            throw new IllegalStateException("Order placed successfully but out of stock. We will notify you once it is in stock");
        }
    }

    public void discount(Order order){
        ProductInventory productInventory = order.getProduct();
        double productPrice = productInventory.getPrice();

        double discountedPrice = productPrice - (productPrice * (order.getDiscount() / 100.0));
        order.setDiscountedPrice(discountedPrice);

        double totalPrice = discountedPrice * order.getQuantity();

        order.setTotalPrice(totalPrice);
    }


    public void addOrder(Order order) {
       updateOtherEntities(order);

        discount(order);
        orderRepository.save(order);
        checkProductAvailability(order);

    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public void deleteOrder(Long orderId, Order order) {
        orderRepository.deleteById(orderId);
    }

}