package com.example.spring.rest.repositories;

import com.example.spring.rest.entities.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile,Long> {

}
