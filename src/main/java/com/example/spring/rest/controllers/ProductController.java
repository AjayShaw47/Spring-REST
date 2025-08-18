package com.example.spring.rest.controllers;

import com.example.spring.rest.dtos.ProductDTO;
import com.example.spring.rest.entities.Product;
import com.example.spring.rest.mappers.ProductMapper;
import com.example.spring.rest.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private  ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId){
        List<ProductDTO> productDTOS = productService.getALlProducts(categoryId);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id){
        ProductDTO productDTO = productService.getProduct(id);
        return ResponseEntity.ok(productDTO);

    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        ProductDTO savedProduct = productService.registerProduct(productDTO);
        URI location = URI.create("/products/" + savedProduct.getId());
        return ResponseEntity.created(location).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductDTO productDTO){
        ProductDTO updatedProduct = productService.updateProduct(id,productDTO);
        return ResponseEntity.ok(updatedProduct);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
