package com.admin.SpringBootDepartmentalStore.controller;

import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.service.BackOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
@WebMvcTest(BackOrderController.class)
public class BackOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BackOrderService backOrderService;

    @InjectMocks
    private BackOrderController backOrderController;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(backOrderController).build();
    }

    @Test
    void testGetAllBackorders() throws Exception {
        List<BackOrder> backOrder = new ArrayList<>();
        backOrder.add(createBackOrder());

        when(backOrderService.getAllBackOrders()).thenReturn(backOrder);

        mockMvc.perform(get("/backorder"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(backOrder.size())));

        verify(backOrderService, times(1)).getAllBackOrders();
    }

    @Test
    public void testGetBackOrderById() throws Exception {
        Long backOrderId = 1L;
        BackOrder backOrder = createBackOrder();

        // Mock the service response
        when(backOrderService.getBackOrderById(backOrderId)).thenReturn(backOrder);

        // Perform the GET request
        mockMvc.perform(get("/backorder/{backOrderId}", backOrderId))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(backOrder)));

        // Verify that the getBackOrderById method in the service is called exactly once with the backOrderId
        verify(backOrderService, times(1)).getBackOrderById(backOrderId);
    }

    @Test
    public void testDeleteBackOrder() throws Exception {
        Long backOrderId = 1L;

        // Perform the DELETE request
        mockMvc.perform(delete("/backorder/{backOrderId}", backOrderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Backorder deleted successfully."));

        // Verify that the deleteBackOrder method in the service is called exactly once with the backOrderId
        verify(backOrderService, times(1)).deleteBackOrder(backOrderId);
    }

    private String asJsonString(Object obj) throws Exception {
        try {
            // Create an instance of ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the object to JSON string using the writeValueAsString method
            String jsonString = objectMapper.writeValueAsString(obj);

            // Return the JSON string representation of the object
            return jsonString;
        } catch (Exception e) {
            // Handle any exception that might occur during JSON serialization
            throw new Exception("Error serializing object to JSON: " + e.getMessage(), e);
        }
    }

    private BackOrder createBackOrder() {
        BackOrder backOrder = new BackOrder();
        backOrder.setBackOrderId(1L);
        backOrder.setOrder(null);
        return backOrder;
    }

}