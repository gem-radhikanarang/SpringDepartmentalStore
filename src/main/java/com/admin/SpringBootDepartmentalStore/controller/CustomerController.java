package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.Customer;
import com.admin.SpringBootDepartmentalStore.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return A list of all customers.
     */

    @Operation(operationId = "getCustomer", summary = "get Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Customer"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     Retrieves a specific customer by its ID.
     @param customerId The ID of the customer to retrieve.
     @return A response entity containing the retrieved customer if found.
     */

    @Operation(operationId = "getCustomer", summary = "get Customer with Customer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Customer"),
            @ApiResponse(responseCode = "400", description = "Customer with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(final @PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    /**

    Adds a new customer.
    @param customer The customer object to add.
    @throws IllegalArgumentException if the customer object is not valid.
    @return A response entity indicating the success of the operation.
     */

    @Operation(operationId = "addCustomer", summary = "add Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added the Customer"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @PostMapping
    public ResponseEntity<String> addCustomer(final @RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer has been added successfully.");
    }

    /**
    Updates a specific customer by its ID.
    @param customer The updated customer object.
    @throws IllegalArgumentException if the customer object is not valid.
     @return A response entity indicating the success of the update operation.
     */

    @Operation(operationId = "updateCustomer", summary = "update Customer with Customer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Customer"),
            @ApiResponse(responseCode = "400", description = "Customer with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @PutMapping("/{customerId}")
    public ResponseEntity<String> updateCustomer(final @PathVariable Long customerId, final @RequestBody Customer customer) {
        customerService.updateCustomer(customerId, customer);
        return ResponseEntity.ok("Customer has been updated successfully.");
    }

    /**

    Deletes a specific customer by its ID.
    @param customerId The ID of the customer to delete.
    @throws IllegalArgumentException if the customer ID is not valid.
    @return A response entity indicating the success of the deletion operation.
     */

    @Operation(operationId = "deleteCustomer", summary = "delete Customer with Customer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Customer"),
            @ApiResponse(responseCode = "400", description = "Customer with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(final @PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer has been deleted successfully.");
    }
}
