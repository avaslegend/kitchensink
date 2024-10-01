package com.app.kitchensink.infraestructure.controller;

import com.app.kitchensink.application.dto.PersonDTO;
import com.app.kitchensink.application.service.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/people")
@Validated
public class KitchenSinkController {
    
    private final PersonService personService;
   
    @Autowired
    public KitchenSinkController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Get all people", description = "Fetch a paginated list of people")
    @GetMapping
    public ResponseEntity<Page<PersonDTO>> getAllPersons(@RequestParam(defaultValue = "0") int page, 
    @RequestParam(defaultValue = "10") int size) {
        Page<PersonDTO> persons = personService.getAllPersons(page, size);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @Operation(summary = "Create a new person", description = "Add a new person to the database")
    @ApiResponse(responseCode = "201", description = "Person created successfully")
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        PersonDTO savedPerson = personService.savePerson(personDTO);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a person", description = "Delete a person by ID")
    @ApiResponse(responseCode = "204", description = "Person deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}