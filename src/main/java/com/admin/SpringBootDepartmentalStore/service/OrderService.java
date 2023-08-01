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

    @Autowired
    private EmailSender emailSender;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(final Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    public void updateOtherEntities(final Order order) {
        Customer customer = order.getCustomer();
        if (customer != null) {
            customer = customerRepository.findById(customer.getCustomerId()).orElse(null);
        }
        ProductInventory productInventory = order.getProduct();
        if (productInventory != null) {
            productInventory = productInventoryRepository.findById(productInventory.getProductId()).orElse(null);
        }
        order.setCustomer(customer);
        order.setProduct(productInventory);
    }

    public void checkProductAvailability(final Order order) {
        ProductInventory productInventory = order.getProduct();
        if (productInventory != null) {
            if (productInventory.getQuantity() > 0) {
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
        } else {
            throw new IllegalArgumentException("Invalid order: product is null");
        }
    }

    public void discount(final Order order) {
        ProductInventory productInventory = order.getProduct();
        if (productInventory != null) {
            double productPrice = productInventory.getPrice();
            double discountedPrice = productPrice - (productPrice * (order.getDiscount() / 100.0));
            order.setDiscountedPrice(discountedPrice);
            double totalPrice = discountedPrice * order.getQuantity();
            order.setTotalPrice(totalPrice);
        }
    }


    public void addOrder(final Order order) {
        updateOtherEntities(order);
        discount(order);
        orderRepository.save(order);
        checkProductAvailability(order);
        String email = order.getCustomer().getEmail();
        String fullName = order.getCustomer().getFullName();
        Long orderId = order.getOrderId();
        emailSender.sendEmail(email, "Order confirmation : #OrderID" + orderId, "Hi " + fullName + ", your order has been confirmed and will be delivered to you soon!"
                + " \n Thank you for shopping with us!");
    }

    public void updateOrder(final Order order) {
        updateOtherEntities(order);
        discount(order);
        orderRepository.save(order);
        checkProductAvailability(order);
    }

    public void deleteOrder(final Long orderId, final Order order) {
        Order existingOrder = getOrderById(orderId);
        ProductInventory productInventory = existingOrder.getProduct();

        if (productInventory.getQuantity() > 0) {
            productInventory.setQuantity(productInventory.getQuantity() + existingOrder.getQuantity());
        }

        Optional<BackOrder> optionalBackOrder = backOrderRepository.findByOrder(existingOrder);
        optionalBackOrder.ifPresent(backOrder -> backOrderService.deleteBackOrder(backOrder.getBackOrderId()));

        orderRepository.deleteById(orderId);
    }

    public void setBackOrderService(final BackOrderService backOrderService) {
        this.backOrderService = backOrderService;
    }

    public void setProductInventoryRepository(final ProductInventoryRepository productInventoryRepository) {
        this.productInventoryRepository = productInventoryRepository;
    }

    public void setOrderRepository(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void setCustomerRepository(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void setBackOrderRepository(final BackOrderRepository backOrderRepository) {
        this.backOrderRepository = backOrderRepository;
    }

    public void setEmailSender(final EmailSender emailSender) {
        this.emailSender = emailSender;
    }
}
