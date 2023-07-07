package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.bean.Order;
import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.repository.BackOrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.CustomerRepository;
import com.admin.SpringBootDepartmentalStore.repository.OrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Autowired
    private BackOrderRepository backOrderRepository;

    public final List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public final Order getOrderById(final Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    public final void updateOtherEntities(final Order order) {
        Customer customer = customerRepository.findById(order.getCustomer().getCustomerId()).orElse(null);
        ProductInventory productInventory = productInventoryRepository.findById(order.getProduct().getProductId()).orElse(null);
        order.setCustomer(customer);
        order.setProduct(productInventory);
    }

    public final void checkProductAvailability(final Order order) {
        ProductInventory productInventory = order.getProduct();
        if (order.getProduct().getQuantity() > 0) {
            orderRepository.save(order);
            productInventory.setQuantity(productInventory.getQuantity() - order.getQuantity());
            productInventoryRepository.save(productInventory);
        } else {
            Order savedOrder = orderRepository.save(order);

            BackOrder backorder = new BackOrder();
            backorder.setOrder(savedOrder);
            backOrderService.createBackOrder(backorder);
            throw new IllegalStateException("Order placed successfully but product is out of stock. We will notify you once it is in stock and process the order");
        }
    }

    public final void discount(final Order order) {
        ProductInventory productInventory = order.getProduct();
        double productPrice = productInventory.getPrice();

        double discountedPrice = productPrice - (productPrice * (order.getDiscount() / 100.0));
        order.setDiscountedPrice(discountedPrice);

        double totalPrice = discountedPrice * order.getQuantity();

        order.setTotalPrice(totalPrice);
    }


    public final void addOrder(final Order order) {
       updateOtherEntities(order);
        discount(order);
        orderRepository.save(order);
        checkProductAvailability(order);

    }

    public final void updateOrder(final Order order) {
        updateOtherEntities(order);
        discount(order);
        orderRepository.save(order);
        checkProductAvailability(order);
    }

    public final void deleteOrder(final Long orderId, final Order order) {
        Order existingOrder = getOrderById(orderId);
        ProductInventory productInventory = existingOrder.getProduct();

        if (productInventory.getQuantity() > 0) {
            productInventory.setQuantity(productInventory.getQuantity() + existingOrder.getQuantity());
        }

        Optional<BackOrder> optionalBackOrder = backOrderRepository.findByOrder(existingOrder);
        optionalBackOrder.ifPresent(backOrder -> backOrderService.deleteBackOrder(backOrder.getBackOrderId()));

        orderRepository.deleteById(orderId);
    }

}
