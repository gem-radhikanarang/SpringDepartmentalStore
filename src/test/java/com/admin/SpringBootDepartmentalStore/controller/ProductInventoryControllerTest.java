package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.service.ProductInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(ProductInventoryController.class)
public class ProductInventoryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductInventoryService productInventoryService;

    @InjectMocks
    private ProductInventoryController productInventoryController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productInventoryController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductInventory> productInventories = new ArrayList<>();
        productInventories.add(createProductInventory(1L));
        productInventories.add(createProductInventory(2L));

        when(productInventoryService.getAllProducts()).thenReturn(productInventories);

        mockMvc.perform(get("/productInventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[1].productId", is(2)));

        verify(productInventoryService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        Long productId = 1L;
        ProductInventory productInventory = createProductInventory(productId);

        when(productInventoryService.getProductById(productId)).thenReturn(productInventory);

        mockMvc.perform(get("/productInventory/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId", is(productId.intValue())));

        verify(productInventoryService, times(1)).getProductById(productId);
    }

    @Test
    void testUpdateProduct() throws Exception {
        Long productId = 1L;
        ProductInventory productToUpdate = createProductInventory(productId);

        mockMvc.perform(put("/productInventory/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product has been updated successfully"));

        verify(productInventoryService, times(1)).updateProduct(productId, productToUpdate);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/productInventory/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product has been deleted successfully"));

        verify(productInventoryService, times(1)).deleteProduct(productId);
    }

    private ProductInventory createProductInventory(Long productId) {
        ProductInventory product = new ProductInventory();
        product.setProductId(productId);
        product.setProductDesc("Product description");
        product.setProductName("Product name");
        product.setPrice(9.99); // Set the desired price
        product.setQuantity(10); // Set the desired count

        return product;
    }

}