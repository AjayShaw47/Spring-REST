package com.example.spring.rest.products;

import com.example.spring.rest.auth.JwtService;
import com.example.spring.rest.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class) // Only load beans related to ProductController
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for simplicity in this test
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; //  Use to send fake HTTP requests (GET, POST) to your controller

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService; //  mock the ProductService. We don't want to test the Service logic again,  we only want to test the HTTP endpoint

    // These mocks are needed because they are usually loaded by SecurityConfig, even if we disable filters, sometimes context requires them.
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private UserRepository userRepository;

    @Test
    void getProducts_ReturnsProductResponse() throws Exception {
        // Arrange
        ProductDTO productDTO = new ProductDTO(1L, "Test Product", BigDecimal.valueOf(100.0), 5, BigDecimal.valueOf(4.5));
        ProductResponse response = new ProductResponse(
                List.of(productDTO),
                0,
                1L,
                1
        );

        given(productService.getALlProducts(eq(null), anyInt(), anyInt())).willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.products[0].name").value("Test Product"))
                .andExpect(jsonPath("$.products[0].id").value(1));
    }

    @Test
    void getProduct_ExistingId_ReturnsProduct() throws Exception {
        // Arrange
        Long id = 1L;
        ProductDTO productDTO = new ProductDTO(id, "Test Product", BigDecimal.valueOf(100.0), 5, BigDecimal.valueOf(4.5));
        
        given(productService.getProduct(id)).willReturn(productDTO);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void createProduct_ValidProduct_ReturnsCreated() throws Exception {
        // Arrange
        ProductDTO inputDto = new ProductDTO(null, "New Product", BigDecimal.valueOf(50.0), 0, BigDecimal.ZERO);
        ProductDTO savedDto = new ProductDTO(1L, "New Product", BigDecimal.valueOf(50.0), 0, BigDecimal.ZERO);
        
        given(productService.registerProduct(any(ProductDTO.class))).willReturn(savedDto);

        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string("Location", "/api/products/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void updateProduct_ExistingId_ReturnsUpdatedProduct() throws Exception {
        // Arrange
        Long id = 1L;
        ProductDTO inputDto = new ProductDTO(id, "Updated Product", BigDecimal.valueOf(150.0), 5, BigDecimal.valueOf(4.5));
        ProductDTO updatedDto = new ProductDTO(id, "Updated Product", BigDecimal.valueOf(150.0), 5, BigDecimal.valueOf(4.5));
        
        given(productService.updateProduct(eq(id), any(ProductDTO.class))).willReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void deleteProduct_ExistingId_ReturnsNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        // deleteProduct returns void, so strictly we don't need to stub it unless it throws
        
        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());
    }
}
