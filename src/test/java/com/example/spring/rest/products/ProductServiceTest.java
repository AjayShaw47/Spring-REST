package com.example.spring.rest.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    // isolated the ProductService by creating Mocks for its dependencies:
    // ProductRepository (To avoid hitting the real database)
    // ProductMapper (To simulate entity-to-DTO conversion)
    // This ensures that if the test fails, it's because of logic inside the Service, not a database issue.

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    // Creates a real ProductService, Injects the mocks above into its constructor/fields
    // productService = new ProductService(productRepository,productMapper,categoryRepository);
    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_NoCategory_ReturnsAllProducts() {
        // Arrange
        int page = 0;
        int size = 10;

        //  Created a dummy Product entity and a dummy ProductDTO.
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        
        ProductDTO productDTO = new ProductDTO(1L, "Test Product", BigDecimal.TEN, 5, BigDecimal.valueOf(4.5));

        // Created a Page<Product> object containing that dummy product.
        Page<Product> productPage = new PageImpl<>(List.of(product)); // new Page<Product>(); will not work as Page is an interface, PageImpl<T> is default implementation of Page<T>.

        //  Tells Mockito: "When someone calls `productRepository.findAll`, don't actually go to the DB—just return this dummy Page.
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        // Act
        // called the actual method:
        ProductResponse result = productService.getALlProducts(null, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.totalItems());
        assertEquals(1, result.totalPages());
        assertEquals(0, result.currentPage());
        
        List<ProductDTO> products = result.products();
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).name());

        // Check that my code actually called the productRepository.findAll() method exactly once, and make sure it passed in a Pageable object as the argument.
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllProducts_WithCategory_ReturnsCategoryProducts() {
        // Arrange
        Byte categoryId = 1;
        int page = 0;
        int size = 10;
        Product product = new Product();
        product.setId(1L);
        product.setName("Category Product");
        
        ProductDTO productDTO = new ProductDTO(1L, "Category Product", BigDecimal.TEN, 5, BigDecimal.valueOf(4.5));
        
        Page<Product> productPage = new PageImpl<>(List.of(product));
        
        when(productRepository.findByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        // Act
        ProductResponse result = productService.getALlProducts(categoryId, page, size);

        // Assert
        assertNotNull(result);
        List<ProductDTO> products = result.products();
        assertEquals(1, products.size());
        assertEquals("Category Product", products.get(0).name());
        
        verify(productRepository).findByCategoryId(eq(categoryId), any(Pageable.class));
    }

    @Test
    void getProduct_ExistingId_ReturnsProduct() {
        // Arrange
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        ProductDTO productDTO = new ProductDTO(id, "Test", BigDecimal.TEN, 5, BigDecimal.valueOf(4.5));

        when(productRepository.findById(id)).thenReturn(java.util.Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.getProduct(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        verify(productRepository).findById(id);
    }

    @Test
    void getProduct_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        // assertThrows checks if the code inside the lambda () -> ... throws the expected exception type
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> productService.getProduct(id));
        verify(productRepository).findById(id);
    }

    @Test
    void registerProduct_ValidProduct_ReturnsSavedProduct() {
        // Arrange
        ProductDTO inputDto = new ProductDTO(null, "New Product", BigDecimal.TEN, 0, BigDecimal.ZERO);
        Product product = new Product();
        product.setName("New Product");
        
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");
        savedProduct.setPrice(BigDecimal.TEN);

        when(productMapper.toEntity(inputDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct); // mocking save to return saved entity (though service uses 'product' var in return)

        // Act
        ProductDTO result = productService.registerProduct(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("New Product", result.name());
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_ExistingId_UpdatesAndReturnsProduct() {
        // Arrange
        Long id = 1L;
        ProductDTO updateDto = new ProductDTO(id, "Updated Name", BigDecimal.valueOf(20), 5, BigDecimal.valueOf(4.5));
        
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Old Name");

        when(productRepository.findById(id)).thenReturn(java.util.Optional.of(existingProduct));
        // productMapper.update(dto, entity) is void, so we usually don't need to stub it unless we want to verify calls or if strict stubbing is on. 
        // Ideally we should assume the mapper updates the entity state.
        
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        when(productMapper.toDto(existingProduct)).thenReturn(updateDto);

        // Act
        ProductDTO result = productService.updateProduct(id, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.name());
        verify(productRepository).findById(id);
        verify(productMapper).update(updateDto, existingProduct);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProduct_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 1L;
        ProductDTO updateDto = new ProductDTO(id, "Updated Name", BigDecimal.valueOf(20), 5, BigDecimal.valueOf(4.5));
        when(productRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> productService.updateProduct(id, updateDto));
        verify(productRepository).findById(id);
        // Verify save is NEVER called
        verify(productRepository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    void deleteProduct_CallsRepositoryDelete() {
        // Arrange
        Long id = 1L;

        // Act
        productService.deleteProduct(id);

        // Assert
        verify(productRepository).deleteById(id);
    }
}
