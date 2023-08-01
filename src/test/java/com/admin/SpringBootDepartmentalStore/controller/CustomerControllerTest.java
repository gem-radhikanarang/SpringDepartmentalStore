package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.service.CustomerService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomer(1L, "John Doe", "john@example.com", "1234567890", "City"));
        customers.add(createCustomer(2L, "Jane Smith", "jane@example.com", "9876543210", "City2"));

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(customers.size())))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john@example.com")))
                .andExpect(jsonPath("$[1].customerId", is(2)))
                .andExpect(jsonPath("$[1].fullName", is("Jane Smith")))
                .andExpect(jsonPath("$[1].email", is("jane@example.com")));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void testGetCustomerById() throws Exception {
        Long customerId = 1L;
        Customer customer = createCustomer(customerId, "John Doe", "john@example.com", "1234567890", "City");

        when(customerService.getCustomerById(customerId)).thenReturn(customer);

        mockMvc.perform(get("/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.fullName", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(customerService, times(1)).getCustomerById(customerId);
    }

    @Test
    void testAddCustomer() throws Exception {
        Customer newCustomer = createCustomer(null, "New Customer", "new@example.com", "9876543210", "City1");

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Customer has been added successfully."));

        verify(customerService, times(1)).addCustomer(newCustomer);
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
        Customer updatedCustomer = createCustomer(customerId, "Updated Customer", "updated@example.com", "1234567890", "City");

        mockMvc.perform(put("/customer/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer has been updated successfully."));

        verify(customerService, times(1)).updateCustomer(customerId, updatedCustomer);
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(delete("/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer has been deleted successfully."));

        verify(customerService, times(1)).deleteCustomer(customerId);
    }

    private Customer createCustomer(Long customerId, String name, String email, String contactNumber, String address) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setContactNumber(contactNumber);
        customer.setAddress(address);
        return customer;
    }


}