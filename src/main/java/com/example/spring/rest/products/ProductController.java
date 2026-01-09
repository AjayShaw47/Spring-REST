package com.example.spring.rest.products;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private  ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) Byte categoryId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size){

        Map<String, Object> response = productService.getALlProducts(categoryId,page,size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id){
        ProductDTO productDTO = productService.getProduct(id);
        return ResponseEntity.ok(productDTO);

    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        ProductDTO savedProduct = productService.registerProduct(productDTO);
        URI location = URI.create("/products/" + savedProduct.id());
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
