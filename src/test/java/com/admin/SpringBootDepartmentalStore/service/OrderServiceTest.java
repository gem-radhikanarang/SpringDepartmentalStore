package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.*;
import com.admin.SpringBootDepartmentalStore.repository.BackOrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.CustomerRepository;
import com.admin.SpringBootDepartmentalStore.repository.OrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.ProductInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BackOrderRepository backOrderRepository;

    @Mock
    private ProductInventoryRepository productInventoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductInventoryService productInventoryService;

    @Mock
    private EmailSender emailSender;

    @MockBean
    private BackOrderService backOrderService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService.setBackOrderService(backOrderService);  // Set the mocked BackOrderService in OrderService
        orderService.setBackOrderRepository(backOrderRepository);
        orderService.setProductInventoryRepository(productInventoryRepository);
        orderService.setCustomerRepository(customerRepository);

    }

    @Test
    void testGetAllOrders() {
        // Create some sample orders
        List<Order> expectedOrders = List.of(
                new Order(/* order details */),
                new Order(/* order details */),
                new Order(/* order details */)
        );
        // Mock the orderRepository to return the sample orders
        Mockito.when(orderRepository.findAll()).thenReturn(expectedOrders);

        // Call the method under test
        List<Order> actualOrders = orderService.getAllOrders();

        // Assert the result
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void testGetAllOrders_EmptyList() {
        // Mock the orderRepository to return an empty list of orders
        Mockito.when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method under test
        List<Order> actualOrders = orderService.getAllOrders();

        // Assert the result
        assertTrue(actualOrders.isEmpty());
    }

    @Test
    void testGetOrderById_ValidOrderId() {
        // Create a sample order with a known ID
        Long orderId = 1L;
        Order expectedOrder = new Order(/* order details */);
        expectedOrder.setOrderId(orderId);

        // Mock the orderRepository to return the sample order
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Call the method under test
        Order actualOrder = orderService.getOrderById(orderId);

        // Assert the result
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGetOrderById_InvalidOrderId() {
        // Create an invalid (non-existing) order ID
        Long orderId = 100L;

        // Mock the orderRepository to return an empty Optional
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Call the method under test and expect it to throw a NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void testCheckProductAvailability_WithAvailableProduct() {
        // Create a sample order with a product quantity > 0
        Order order = new Order();
        ProductInventory productInventory = new ProductInventory(/* product details */);
        productInventory.setQuantity(10);
        order.setProduct(productInventory);

        // Mock the necessary repositories and services

        // Call the method under test
        orderService.checkProductAvailability(order);

        // Assert that the order is saved and the product quantity is reduced
        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
        Mockito.verify(productInventoryRepository, Mockito.times(1)).save(productInventory);
        assertEquals(10, productInventory.getQuantity());
    }

    @Test
    void testUpdateOrder_Success() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        when(orderRepository.save(order)).thenReturn(order);
        when(productInventoryRepository.findById(order.getProduct().getProductId())).thenReturn(Optional.of(order.getProduct()));
        when(customerRepository.findById(order.getCustomer().getCustomerId())).thenReturn(Optional.of(order.getCustomer()));

        // Act
        orderService.updateOrder(order);

        // Assert
        verify(productInventoryRepository, times(1)).findById(order.getProduct().getProductId());
        verify(customerRepository, times(1)).findById(order.getCustomer().getCustomerId());
    }


    private Order createOrder(Long orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setQuantity(2);
        order.setDiscount(10.0);

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFullName("John Doe");
        order.setCustomer(customer);

        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductId(1L);
        productInventory.setProductName("Product 1");
        productInventory.setQuantity(5);
        productInventory.setPrice(100.0);
        order.setProduct(productInventory);
        return order;
    }

}