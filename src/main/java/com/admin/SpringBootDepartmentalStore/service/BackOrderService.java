package com.admin.SpringBootDepartmentalStore.service;

import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.repository.BackOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BackOrderService {

    private BackOrderRepository backOrderRepository;

    public List<BackOrder> getAllBackOrders() {
        return backOrderRepository.findAll();
    }

    public BackOrder getBackOrderById(final Long backOrderId) {
        return backOrderRepository.findById(backOrderId)
                .orElseThrow(NoSuchElementException::new);
    }

    public void createBackOrder(final BackOrder backorder) {
        backOrderRepository.save(backorder);
    }

    public void deleteBackOrder(final Long backOrderId) {
        backOrderRepository.deleteById(backOrderId);
    }




}
