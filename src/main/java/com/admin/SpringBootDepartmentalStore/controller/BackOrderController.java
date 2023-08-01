package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.service.BackOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/backorder")
public class BackOrderController {
    @Autowired
    private BackOrderService backOrderService;

    /**
     * Retrieves all back orders.
     *
     * @return A list of all back orders.
     */

    @Operation(operationId = "getBackOrder", summary = "get BackOrder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Backorder"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping
    public List<BackOrder> getAllBackOrders() {
        return backOrderService.getAllBackOrders();
    }

    /**
     Retrieves a specific back order by its ID.
     @param backOrderId The ID of the back order to retrieve.
     @return A response entity containing the retrieved back order if found.
     */


    @Operation(operationId = "getBackOrder", summary = "get BackOrder with BackOrder id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the BackOrder"),
            @ApiResponse(responseCode = "400", description = "BackOrder with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping("/{backOrderId}")
    public ResponseEntity<BackOrder> getBackOrderById(final @PathVariable Long backOrderId) {
        BackOrder backOrder = backOrderService.getBackOrderById(backOrderId);
        return ResponseEntity.ok(backOrder);
    }

    /**
    Deletes a specific back order by its ID.
    @param backOrderId The ID of the back order to delete.
    @return A response entity indicating the success of the deletion operation.
    */

    @Operation(operationId = "deleteBackOrder", summary = "delete BackOrder with BackOrder id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Backorder"),
            @ApiResponse(responseCode = "400", description = "BackOrder with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @DeleteMapping("/{backOrderId}")
    public ResponseEntity<String> deleteBackOrder(final @PathVariable Long backOrderId) {
        backOrderService.deleteBackOrder(backOrderId);
        return ResponseEntity.ok("Backorder deleted successfully.");
    }

}
