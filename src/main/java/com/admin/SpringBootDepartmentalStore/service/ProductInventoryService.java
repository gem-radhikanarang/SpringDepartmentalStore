package com.admin.SpringBootDepartmentalStore.service;
import com.admin.SpringBootDepartmentalStore.bean.BackOrder;
import com.admin.SpringBootDepartmentalStore.bean.Order;
import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.helper.ExcelHelper;
import com.admin.SpringBootDepartmentalStore.repository.BackOrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.OrderRepository;
import com.admin.SpringBootDepartmentalStore.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Service
public class ProductInventoryService {

    @Autowired
    private ProductInventoryRepository productRepository;

    @Autowired
    private BackOrderService backOrderService;

    @Autowired
    private BackOrderRepository backOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void save(final MultipartFile file) {

        try {
            List<ProductInventory> products = ExcelHelper.convertExcelToList(file.getInputStream());
            this.productRepository.saveAll(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ProductInventory> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductInventory getProductById(final Long productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
    }

    public void addProduct(final ProductInventory productInventory) {

        productRepository.save(productInventory);
    }

    public void removeBackorder(final Long productId, final ProductInventory productInventory) {
        ProductInventory productObj = getProductById(productId);
        List<BackOrder> backOrders = backOrderRepository.findByOrderProductProductId(productId);

        if (backOrders.isEmpty()) {
            return;
        }

        boolean isQuantityFulfilled = false;
        int remainingQuantity = productObj.getQuantity();

        for (BackOrder backOrder : backOrders) {
            Order order = backOrder.getOrder();
            if (remainingQuantity >= order.getQuantity()) {
                // Sufficient quantity available, delete the backorder
                backOrderService.deleteBackOrder(backOrder.getBackOrderId());
                remainingQuantity -= order.getQuantity();
                isQuantityFulfilled = true;
            }
        }

        if (isQuantityFulfilled) {
            // Update the product quantity
            productInventory.setQuantity(remainingQuantity);
            productRepository.save(productInventory);
        }
    }

    public void updateProduct(final Long productId, final ProductInventory productInventory) {
        ProductInventory productObj = getProductById(productId);
        if (productObj != null) {
            productObj.setProductId(productId);
            productObj.setProductDesc(productInventory.getProductDesc());
            productObj.setProductName(productInventory.getProductName());
            productObj.setPrice(productInventory.getPrice());
            productObj.setQuantity(productInventory.getQuantity());
            productRepository.save(productObj);
        }
    }

    public void deleteProduct(final Long orderId) {
        productRepository.deleteById(orderId);
    }

    @Scheduled(cron = "0 * * * * *")
    public void removeBackordersScheduled() {
        // Get the list of products with backorders
        List<ProductInventory> productsWithBackorders = productRepository.findAll();

        for (ProductInventory productInventory : productsWithBackorders) {
            removeBackorder(productInventory.getProductId(), productInventory);
        }
    }

    public void setProductInventoryRepository(final ProductInventoryRepository productInventoryRepository) {
        this.productRepository = productInventoryRepository;
    }

    public Stream<ProductInventory> searchProductsStream(final String productName) {
        return productRepository.findAll().stream()
                .filter(product -> product.getProductName().equalsIgnoreCase(productName));
    }

    public Page<ProductInventory> getAllProductsWithPagination(final int page, final int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(pageable);
    }

}
