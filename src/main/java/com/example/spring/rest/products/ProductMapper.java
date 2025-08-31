package com.example.spring.rest.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id",target = "categoryId")
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO productDTO);
    @Mapping(target = "id",ignore = true)
    void update(ProductDTO productDTO,@MappingTarget Product product);
}
