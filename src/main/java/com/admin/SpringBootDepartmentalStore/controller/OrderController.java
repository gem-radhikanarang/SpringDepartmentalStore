package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.Order;
import com.admin.SpringBootDepartmentalStore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     Retrieves all orders.
     @return A list of all orders.
     */
    @Operation(operationId = "getOrder", summary = "get Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Order"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
    Retrieves a specific order by its ID.
    @param orderId The ID of the order to retrieve.
    @return A response entity containing the retrieved order if found.
     */

    @Operation(operationId = "getOrder", summary = "get Order with Order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Order"),
            @ApiResponse(responseCode = "400", description = "Order with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(final @PathVariable Long orderId) {
       Order order = orderService.getOrderById(orderId);
       return ResponseEntity.ok(order);
    }

    /**

    Adds a new order.
    @param order The order object to add.
    @throws IllegalArgumentException if the order object is not valid.
    @return A response entity indicating the success of the operation.
     */

    @Operation(operationId = "addOrder", summary = "add Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added the Order"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})


    @PostMapping
    public ResponseEntity<String> addOrder(final @RequestBody Order order) {
        orderService.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order has been added successfully.");
    }

    /**

    Updates a specific order by its ID.
    @param order The updated order object.
    @throws IllegalArgumentException if the order object is not valid.
    @return A response entity indicating the success of the update operation.
     */

    @Operation(operationId = "updateOrder", summary = "update Order with Order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Order"),
            @ApiResponse(responseCode = "400", description = "Order with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(final @PathVariable Long orderId, final @RequestBody Order order) {
        orderService.updateOrder(order);
        return ResponseEntity.ok("Order has been updated successfully.");
    }

    /**
    Deletes a specific order by its ID.
    @param orderId The ID of the order to delete.
    @param order The order object.
    @throws IllegalArgumentException if the order ID or the order object is not valid.
    @return A response entity indicating the success of the deletion operation.
     */

    @Operation(operationId = "deleteOrder", summary = "delete Order with Order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Order"),
            @ApiResponse(responseCode = "400", description = "Order with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(final @PathVariable Long orderId, final Order order) {
        orderService.deleteOrder(orderId, order);
        return ResponseEntity.ok("Order has been deleted successfully.");
    }

}
