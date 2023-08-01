package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${valid}")
    private String valid;

    @Value("${pattern}")
    private String pattern;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(final Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(NoSuchElementException::new);
    }

    private void validateEmail(final String email) {

        if (!email.matches(valid)) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void validateContactNumber(final String contactNumber) {
        if (contactNumber == null) {
            throw new IllegalArgumentException("Contact number cannot be null");
        }

        if (!contactNumber.matches(pattern)) {
            throw new IllegalArgumentException("Invalid contact number");
        }
    }

    public void addCustomer(final Customer customer) {
        validateEmail(customer.getEmail());
        validateContactNumber(customer.getContactNumber());
        customerRepository.save(customer);
    }


    public void updateCustomer(final Long customerId, final Customer customer) {
        Customer customerObj = getCustomerById(customerId);
        if (customerObj != null) {
            String updatedEmail = customer.getEmail();
            if (updatedEmail != null && !updatedEmail.isEmpty()) {
                validateEmail(updatedEmail);
                validateContactNumber(customer.getContactNumber());
                customerObj.setCustomerId(customerId);
                customerObj.setFullName(customer.getFullName());
                customerObj.setAddress(customer.getAddress());
                customerObj.setContactNumber(customer.getContactNumber());
                customerObj.setEmail(updatedEmail);
                customerRepository.save(customerObj);
            } else {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
        } else {
            throw new IllegalArgumentException("Customer not found");
        }
    }

    public void deleteCustomer(final Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
