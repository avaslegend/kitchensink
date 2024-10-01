package com.app.kitchensink.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Data
@Document(collection = "people")
@AllArgsConstructor
public class Person {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    public Person() {
        this.id = UUID.randomUUID().toString(); // Generate a new UUID
    }
}
