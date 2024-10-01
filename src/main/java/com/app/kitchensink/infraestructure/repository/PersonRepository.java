package com.app.kitchensink.infraestructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.kitchensink.domain.model.Person;

public interface PersonRepository extends MongoRepository<Person, String> {

    Person findByEmail(String email); // Method to find a person by email

}
