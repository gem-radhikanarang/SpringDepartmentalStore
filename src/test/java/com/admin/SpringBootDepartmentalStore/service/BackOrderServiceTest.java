package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.repository.BackOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BackOrderServiceTest {

    @Mock
    private BackOrderRepository backorderRepository;

    @InjectMocks
    private BackOrderService backOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBackOrders() {
        // Create a list of dummy back orders
        List<BackOrder> backOrders = new ArrayList<>();
        backOrders.add(new BackOrder());
        backOrders.add(new BackOrder());

        // Mock the behavior of the backorderRepository.findAll() method
        when(backorderRepository.findAll()).thenReturn(backOrders);

        // Call the method under test
        List<BackOrder> result = backOrderService.getAllBackOrders();

        // Verify the result
        assertEquals(backOrders.size(), result.size());
        assertEquals(backOrders, result);
        verify(backorderRepository, times(1)).findAll();
    }

    @Test
    void testGetBackOrderById() {
        // Create a dummy back order
        BackOrder backOrder = new BackOrder();
        Long backOrderId = 1L;
        backOrder.setBackOrderId(backOrderId);

        // Mock the behavior of the backorderRepository.findById() method
        when(backorderRepository.findById(backOrderId)).thenReturn(Optional.of(backOrder));

        // Call the method under test
        BackOrder result = backOrderService.getBackOrderById(backOrderId);

        // Verify the result
        assertNotNull(result);
        assertEquals(backOrderId, result.getBackOrderId());
        verify(backorderRepository, times(1)).findById(backOrderId);
    }

    @Test
    void testGetBackOrderById_BackOrderNotFound() {
        Long backOrderId = 1L;

        // Mock the behavior of the backorderRepository.findById() method
        when(backorderRepository.findById(backOrderId)).thenReturn(Optional.empty());

        // Call the method under test and verify that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> backOrderService.getBackOrderById(backOrderId));
        verify(backorderRepository, times(1)).findById(backOrderId);
    }

    @Test
    void testCreateBackOrder() {
        // Create a dummy back order
        BackOrder backOrder = new BackOrder();

        // Call the method under test
        backOrderService.createBackOrder(backOrder);

        // Verify that the backorderRepository.save() method was called once
        verify(backorderRepository, times(1)).save(backOrder);
    }

    @Test
    void testDeleteBackOrder() {
        // Create a dummy back order
        Long backOrderId = 1L;

        // Call the method under test
        backOrderService.deleteBackOrder(backOrderId);

        // Verify that the backorderRepository.deleteById() method was called once with the correct parameter
        verify(backorderRepository, times(1)).deleteById(backOrderId);
    }

}
