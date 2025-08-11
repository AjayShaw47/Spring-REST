package com.example.spring.rest.repositories;

import com.example.spring.rest.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address,Long> {
}
