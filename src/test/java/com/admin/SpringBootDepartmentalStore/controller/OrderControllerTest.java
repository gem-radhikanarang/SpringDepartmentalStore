package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.bean.Order;
import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(OrderController.class)

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(1L));
        orders.add(createOrder(2L));

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(orders.size())))
                .andExpect(jsonPath("$[0].orderId", is(1)))
                .andExpect(jsonPath("$[1].orderId", is(2)));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById() throws Exception {
        Long orderId = 1L;
        Order order = createOrder(orderId);

        when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId", is(orderId.intValue())))
                .andExpect(jsonPath("$.quantity", is(2))) // Assuming quantity is 2 as set in createOrder()
                .andExpect(jsonPath("$.discount", is(10.0))) // Assuming discount is 10.0 as set in createOrder()
                .andExpect(jsonPath("$.customer.customerId", is(1))) // Assuming customerId is 1 as set in createOrder()
                .andExpect(jsonPath("$.customer.fullName", is("John Doe"))) // Assuming fullName is "John Doe" as set in createOrder()
                .andExpect(jsonPath("$.product.productId", is(1))) // Assuming productId is 1 as set in createOrder()
                .andExpect(jsonPath("$.product.productName", is("Product 1"))); // Assuming productName is "Product 1" as set in createOrder()

        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void testAddOrder() throws Exception {
        Order newOrder = createOrder(null);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order has been added successfully."));

        verify(orderService, times(1)).addOrder(newOrder);
    }

    @Test
    void testUpdateOrder() throws Exception {
        Long orderId = 1L;
        Order updatedOrder = createOrder(orderId);

        mockMvc.perform(put("/order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order has been updated successfully."));

        verify(orderService, times(1)).updateOrder(updatedOrder);
    }

    @Test
    void testDeleteOrder() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(delete("/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Order has been deleted successfully."));

        verify(orderService, times(1)).deleteOrder(eq(orderId), any(Order.class));
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