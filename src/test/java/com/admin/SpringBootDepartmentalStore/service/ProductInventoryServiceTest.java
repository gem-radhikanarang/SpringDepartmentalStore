package com.admin.SpringBootDepartmentalStore.service;
import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import com.admin.SpringBootDepartmentalStore.repository.ProductInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductInventoryServiceTest {

    @Mock
    private ProductInventoryRepository productRepository;

    @InjectMocks
    private ProductInventoryService productInventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveProductsFromFile() throws IOException {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MockMultipartFile file = new MockMultipartFile(
                "data", "products.xlsx", "application/vnd.ms-excel", createExcelData());

        List<ProductInventory> expectedProducts = createExpectedProducts();

        doReturn(expectedProducts).when(productRepository).saveAll(anyIterable());

        // Act
        productInventoryService.save(file);

        // Assert
        verify(productRepository, times(1)).saveAll(expectedProducts);
    }

    private byte[] createExcelData() {
        // Create and return the binary data of the Excel file to be used in the test
        // You can use a library like Apache POI to create the Excel file data programmatically
        return new byte[0];
    }

    private List<ProductInventory> createExpectedProducts() {
        // Create and return a list of expected ProductInventory objects
        List<ProductInventory> products = new ArrayList<>();
        // Add some ProductInventory objects to the list
        return products;
    }

    @Test
    void testGetAllProducts() {
        // Create a list of dummy products
        List<ProductInventory> products = new ArrayList<>();
        products.add(new ProductInventory());
        products.add(new ProductInventory());

        // Mock the behavior of the productRepository.findAll() method
        when(productRepository.findAll()).thenReturn(products);

        // Call the method under test
        List<ProductInventory> result = productInventoryService.getAllProducts();

        // Verify the result
        assertEquals(products.size(), result.size());
        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Create a dummy product
        ProductInventory product = new ProductInventory();
        Long productId = 1L;
        product.setProductId(productId);

        // Mock the behavior of the productRepository.findById() method
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call the method under test
        ProductInventory result = productInventoryService.getProductById(productId);

        // Verify the result
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        Long productId = 1L;

        // Mock the behavior of the productRepository.findById() method
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the method under test and verify that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> productInventoryService.getProductById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testAddProduct() {
        // Create a new product
        ProductInventory product = new ProductInventory();
        product.setProductId(1L);
        product.setProductName("Product 1");
        product.setProductDesc("Description of Product 1");
        product.setPrice(9.99);
        product.setQuantity(100);

        // Mock the save method of the repository
        Mockito.when(productRepository.save(product)).thenReturn(product);

        // Call the addProduct method
        productInventoryService.addProduct(product);

        // Verify that the product is saved in the repository
        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    public void testAddProduct_NullProduct() {

        // Call the addProduct method with a null ProductInventory object
        productInventoryService.addProduct(null);

        // Verify that the save method of the repository was never invoked
        verify(productRepository, never()).save(any(ProductInventory.class));
    }


    @Test
    void testUpdateProduct_ExistingProduct() {
        // Create a dummy product
        Long productId = 1L;
        ProductInventory product = new ProductInventory();
        product.setProductId(productId);
        product.setProductName("Updated Product");
        product.setProductDesc("Updated Description");
        product.setPrice(19.99);
        product.setQuantity(200);

        // Mock the behavior of the productRepository.findById() method
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call the method under test
        productInventoryService.updateProduct(productId, product);

        // Verify that the productRepository.save() method was called once with the updated product
        verify(productRepository, times(1)).save(product);
    }


    @Test
    void testUpdateProduct_NonExistingProduct() {
        // Create a dummy product
        Long productId = 1L;
        ProductInventory product = new ProductInventory();
        product.setProductId(productId);
        product.setProductName("Updated Product");
        product.setProductDesc("Updated Description");
        product.setPrice(19.99);
        product.setQuantity(200);

        // Mock the behavior of the productRepository.findById() method
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the method under test and verify that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> productInventoryService.updateProduct(productId, product));

        // Verify that the productRepository.save() method was never called
        verify(productRepository, never()).save(any(ProductInventory.class));
    }


    @Test
    void testDeleteProduct_ExistingProduct() {
        // Create a dummy product
        Long productId = 1L;

        // Call the method under test
        productInventoryService.deleteProduct(productId);

        // Verify that the productRepository.deleteById() method was called once with the correct parameter
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_NonExistingProduct() {
        // Create a non-existing product ID
        Long productId = 999L;

        // Mock the behavior of the productRepository.findById() method
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the method under test
        assertDoesNotThrow(() -> productInventoryService.deleteProduct(productId));

        // Verify that the product still doesn't exist
        assertThrows(NoSuchElementException.class, () -> productInventoryService.getProductById(productId));
    }




}