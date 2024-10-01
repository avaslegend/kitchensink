package com.app.kitchensink.service;

import com.app.kitchensink.application.dto.PersonDTO;
import com.app.kitchensink.application.exception.EmailAlreadyExistsException;
import com.app.kitchensink.application.exception.PersonNotFoundException;
import com.app.kitchensink.application.service.PersonService;
import com.app.kitchensink.domain.model.Person;
import com.app.kitchensink.infraestructure.repository.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPersonsWithPagination() {
        // Arrange
        Person person1 = new Person();
        person1.setId("1");
        person1.setFirstName("Alice");
        person1.setLastName("Smith");
        person1.setEmail("alice@example.com");

        Person person2 = new Person();
        person2.setId("2");
        person2.setFirstName("john");
        person2.setLastName("Doe");
        person2.setEmail("john@example.com");

        List<Person> mockPersons = Arrays.asList(person1, person2);
        when(personRepository.findAll()).thenReturn(mockPersons);

        // Act
        Page<PersonDTO> personDTOs = personService.getAllPersons(0, 10);

        // Assert
        assertEquals(2, personDTOs.getContent().size());
        assertEquals("Alice", personDTOs.getContent().get(0).getFirstName());
        assertEquals("john", personDTOs.getContent().get(1).getFirstName());
    }

    @Test
    void testSavePersonWithUniqueEmail() {
        // Arrange
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId("3");
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setEmail("john.doe@example.com");

        when(personRepository.findByEmail(personDTO.getEmail())).thenReturn(null); // No existing email
        when(personRepository.save(any(Person.class))).thenReturn(new Person()); // Mock save behavior

        // Act
        PersonDTO savedPersonDTO = personService.savePerson(personDTO);

        // Assert
        assertNotNull(savedPersonDTO);
        verify(personRepository, times(1)).findByEmail(personDTO.getEmail());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testSavePersonWithExistingEmail() {
        // Arrange
        PersonDTO personDTO = new PersonDTO();
        personDTO.setEmail("john.doe@example.com");

        Person existingPerson = new Person();
        existingPerson.setEmail("john.doe@example.com");

        when(personRepository.findByEmail(personDTO.getEmail())).thenReturn(existingPerson);

        // Act & Assert
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            personService.savePerson(personDTO);
        });

        assertEquals(personDTO.getEmail(), exception.getMessage());
        verify(personRepository, times(1)).findByEmail(personDTO.getEmail());
        verify(personRepository, times(0)).save(any(Person.class)); // Ensure save was not called
    }

    @Test
    void testDeletePersonWithExistingId() {
        // Arrange
        String personId = "1";
        when(personRepository.existsById(personId)).thenReturn(true);

        // Act
        personService.deletePerson(personId);

        // Assert
        verify(personRepository, times(1)).deleteById(personId);
    }

    @Test
    void testDeletePersonWithNonExistingId() {
        // Arrange
        String personId = "1";
        when(personRepository.existsById(personId)).thenReturn(false);

        // Act & Assert
        PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> {
            personService.deletePerson(personId);
        });

        assertEquals(personId, exception.getMessage());
        verify(personRepository, times(0)).deleteById(personId); // Ensure delete was not called
    }
}