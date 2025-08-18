package com.example.spring.rest.services;

import com.example.spring.rest.dtos.ProductDTO;
import com.example.spring.rest.entities.Category;
import com.example.spring.rest.entities.Product;
import com.example.spring.rest.mappers.ProductMapper;
import com.example.spring.rest.repositories.CategoryRepository;
import com.example.spring.rest.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;

    public List<ProductDTO> getALlProducts(Byte categoryId) {
        List<Product> products;
        if(categoryId != null)
            products =  productRepository.findByCategoryId(categoryId);
        else{
            products =  productRepository.findAll();

        }
        return products.stream().map(product -> productMapper.toDto(product)).toList();

    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }

    public ProductDTO registerProduct(ProductDTO productDTO) {

       Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        productRepository.save(product);
        productDTO.setId(product.getId());
         return  productDTO;
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
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

