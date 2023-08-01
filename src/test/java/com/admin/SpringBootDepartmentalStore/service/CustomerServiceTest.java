package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.repository.CustomerRepository;
import com.admin.SpringBootDepartmentalStore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Autowired
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        // Create a list of customers for mocking repository behavior
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        // Mock the behavior of the customerRepository.findAll() method
        when(customerRepository.findAll()).thenReturn(customers);

        // Call the method under test
        List<Customer> result = customerService.getAllCustomers();

        // Verify the returned list matches the mocked list
        assertEquals(customers, result);
    }

    @Test
    void testGetCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer();

        // Mock the behavior of the customerRepository.findById() method
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Call the method under test
        Customer result = customerService.getCustomerById(customerId);

        // Verify the returned customer is the same as the mocked customer
        assertEquals(customer, result);
    }

    @Test
    void testGetCustomerById_CustomerNotFound() {
        Long customerId = 1L;

        // Mock the behavior of the customerRepository.findById() method to return an empty Optional
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Call the method under test and verify the NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    void testAddCustomer_ValidCustomer_Success() {
        // Prepare the customer data
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setContactNumber("1234567890");

        // Mock the behavior of the customerRepository.save() method to return the saved customer with the assigned customerId
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setCustomerId(1L); // Assign a fixed customerId for simplicity in the test
            return savedCustomer;
        });

        // Call the method under test
        assertDoesNotThrow(() -> customerService.addCustomer(customer));

        // Verify that the customerRepository.save() method is invoked with the correct customer object
        verify(customerRepository, times(1)).save(customer);

        // Ensure that the customer has been assigned a non-null customerId after saving
        assertNotNull(customer.getCustomerId());
    }

    @Test
    void testAddCustomer_InvalidEmail() {
        // Prepare the customer data with an invalid email address
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setEmail("invalid_email"); // Set an invalid email address
        customer.setContactNumber("1234567890"); // Set a valid contact number

        // Call the method under test and verify that IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(customer));

        // Verify that the customerRepository.save() method is not invoked
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void testAddCustomer_InvalidContactNumber() {
        // Prepare the customer data with an invalid contact number
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com"); // Set a valid email address
        customer.setContactNumber("invalid_contact_number"); // Set an invalid contact number

        // Call the method under test and verify that IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(customer));

        // Verify that the customerRepository.save() method is not invoked
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void testAddCustomer_NullCustomer() {
        // Prepare the null customer
        Customer customer = null;

        // Call the method under test and expect it to throw a NullPointerException
        assertThrows(NullPointerException.class, () -> customerService.addCustomer(customer));
    }

    @Test
    void testUpdateCustomer_ValidData() {
        Long customerId = 1L;

        // Prepare the customer data
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setContactNumber("1234567890");

        // Mock the behavior of customerRepository.findById() to return a customer
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Call the method under test
        customerService.updateCustomer(customerId, customer);

        // Verify that the customerRepository.save() method is invoked
        verify(customerRepository, times(1)).save(customer);
    }


    @Test
    void testUpdateCustomer_CustomerNotFound() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setEmail("johndoe@example.com"); // Set a valid email address
        customer.setContactNumber("1234567890"); // Set a valid contact number


        // Mock the behavior of the customerRepository.findById() method to return an empty Optional
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Call the method under test and verify the NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> customerService.updateCustomer(customerId, customer));
    }

    @Test
    void testDeleteCustomer_ExistingCustomer() {
        Long customerId = 1L;

        // Call the method under test and verify that no exception is thrown
        assertDoesNotThrow(() -> customerService.deleteCustomer(customerId));

        // Verify the customerRepository.deleteById() method is invoked
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void testDeleteCustomer_NonexistentCustomer() {
        Long customerId = 1L;

        // Mock the behavior of the customerRepository.deleteById() method to throw an exception
        doThrow(NoSuchElementException.class).when(customerRepository).deleteById(customerId);

        // Call the method under test and verify that NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> customerService.deleteCustomer(customerId));

        // Verify the customerRepository.deleteById() method is invoked
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    private Customer createCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFullName("John Doe");
        customer.setAddress("123 Main Street");
        customer.setContactNumber("+919417665710");
        customer.setEmail("johndoe@gmail.com");
        return customer;
    }
}