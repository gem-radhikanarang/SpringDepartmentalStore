package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.helper.ExcelHelper;
import com.admin.SpringBootDepartmentalStore.service.ProductInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productInventory")
public class ProductInventoryController {

    @Autowired
    private ProductInventoryService productInventoryService;

    /**
     * Retrieves all products in the inventory.
     *
     * @return A list of all products in the inventory.
     */

    @Operation(operationId = "getProduct", summary = "get Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully retrieved the Product"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping
    public List<ProductInventory> getAllProducts() {
        return productInventoryService.getAllProducts();
    }

    /**
     * Retrieves a specific product from the inventory by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return A response entity containing the retrieved product if found.
     */

    @Operation(operationId = "getProduct", summary = "get Product with Product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Product"),
            @ApiResponse(responseCode = "400", description = "Product with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInventory> getProductById(final @PathVariable Long productId) {
        ProductInventory productInventory = productInventoryService.getProductById(productId);
        return ResponseEntity.ok(productInventory);
    }

    /**
     * Retrieves a specific product from the inventory by its ID.
     *
     * @param productName The name of the product to retrieve.
     * @return A response entity containing the retrieved product if found.
     */

    @Operation(operationId = "searchProducts", summary = "Search Product by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Product"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(final @RequestParam(value = "productName") String productName) {

        List<ProductInventory> productList = productInventoryService.searchProductsStream(productName)
                .collect(Collectors.toList());

        if (productList.isEmpty()) {
            // If the search result is empty, return a custom message or any desired response
            String message = "Product with name '" + productName + "' not found.";
            return ResponseEntity.ok(message);
        } else {
            // If the product is found, return the details
            return ResponseEntity.ok(productList);
        }
    }

    /**
     * Retrieves a specific product from the inventory by its ID.
     *
     * @param page The page number to retrieve
     * @param size The size limit of the page
     * @return A response entity containing the retrieved product details on the requested page number.
     */

    @Operation(operationId = "searchProducts", summary = "Search Product by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Product"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})


    @GetMapping("/searchWithPagination")
    public ResponseEntity<?> getAllProducts(final @RequestParam(defaultValue = "1") int page,
                                            final @RequestParam(defaultValue = "10") int size) {
        Page<ProductInventory> productPage = productInventoryService.getAllProductsWithPagination(page, size);

        if (productPage.isEmpty()) {
            String message = "No products found.";
            return ResponseEntity.ok(message);
        } else {
            // Return the page number and list of products
            Map<String, Object> response = new HashMap<>();
            response.put("pageNumber", productPage.getNumber() + 1); // Add 1 to the page number to make it 1-indexed
            response.put("products", productPage.getContent());
            return ResponseEntity.ok(response);
        }
    }

    /**
     Adds a new product to the inventory.
     @param file The product inventory object to add.
     @throws IllegalArgumentException if the product inventory object is not valid.
     */

    @Operation(operationId = "addProduct", summary = "add Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added the Product"),
            @ApiResponse(responseCode = "400", description = "Product with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @PostMapping
    public ResponseEntity<?> upload(final @RequestParam("file") MultipartFile file) {
        if (ExcelHelper.checkFormat(file)) {
            //true

            this.productInventoryService.save(file);

            return ResponseEntity.ok(Map.of("message", "File is uploaded"));


        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file ");
    }

    /**
     Updates a specific product in the inventory by its ID.
     @param productInventory The updated product inventory object.
     @throws IllegalArgumentException if the product inventory object is not valid.
     */

    @Operation(operationId = "updateProduct", summary = "update Product with Product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the product"),
            @ApiResponse(responseCode = "400", description = "Product with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(final @PathVariable Long productId, final @RequestBody ProductInventory productInventory) {
        productInventoryService.updateProduct(productId, productInventory);
        return ResponseEntity.ok("Product has been updated successfully");
    }

    /**

     Deletes a specific product from the inventory by its ID.
     @param productId The ID of the product to delete.
     @throws IllegalArgumentException if the product ID is not valid.
     */

    @Operation(operationId = "deleteOrder", summary = "delete Product with Product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Product"),
            @ApiResponse(responseCode = "400", description = "Product with given id not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable final Long productId) {
        productInventoryService.deleteProduct(productId);
        return ResponseEntity.ok("Product has been deleted successfully");
    }
}
