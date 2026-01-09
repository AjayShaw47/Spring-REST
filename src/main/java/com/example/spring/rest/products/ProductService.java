package com.example.spring.rest.products;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;

    public Map<String, Object> getALlProducts(Byte categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        if(categoryId != null)
            productPage =  productRepository.findByCategoryId(categoryId,pageable);
        else
            productPage =  productRepository.findAll(pageable);

        List<ProductDTO> productDTO = productPage.getContent().stream().map(product -> productMapper.toDto(product)).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("products", productDTO);
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());

        return response;

    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }

    public ProductDTO registerProduct(ProductDTO productDTO) {

       Category category = categoryRepository.findById(productDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        productRepository.save(product);
        return new ProductDTO(
                product.getId(),
                productDTO.name(),
                productDTO.price(),
                productDTO.categoryId(),
                productDTO.ratingCount(),
                productDTO.ratingStar()
        );
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        productMapper.update(productDTO,product);
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.toDto(product);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

