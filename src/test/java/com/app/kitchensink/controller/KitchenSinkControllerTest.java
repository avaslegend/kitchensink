package com.app.kitchensink.controller;

import com.app.kitchensink.application.dto.PersonDTO;
import com.app.kitchensink.application.service.PersonService;
import com.app.kitchensink.infraestructure.controller.KitchenSinkController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class KitchenSinkControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private KitchenSinkController kitchenSinkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
    void testGetAllPersons() {
        // Arrange
        PersonDTO person1 = new PersonDTO();
        person1.setId("1");
        person1.setFirstName("Alice");
        person1.setLastName("Smith");
        person1.setEmail("alice@example.com");

        PersonDTO person2 = new PersonDTO();
        person2.setId("2");
        person2.setFirstName("John");
        person2.setLastName("Doe");
        person2.setEmail("john@example.com");

        List<PersonDTO> mockPersons = Arrays.asList(person1, person2);
        Page<PersonDTO> personPage = new PageImpl<>(mockPersons, PageRequest.of(0, 10), mockPersons.size());

        when(personService.getAllPersons(0, 10)).thenReturn(personPage);

        // Act
        ResponseEntity<Page<PersonDTO>> response = kitchenSinkController.getAllPersons(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Alice", response.getBody().getContent().get(0).getFirstName());
        assertEquals("John", response.getBody().getContent().get(1).getFirstName());
    }

    @Test
    void testCreatePerson() {
        // Arrange
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId("3");
        personDTO.setFirstName("Jane");
        personDTO.setLastName("Doe");
        personDTO.setEmail("jane.doe@example.com");

        when(personService.savePerson(personDTO)).thenReturn(personDTO);

        // Act
        ResponseEntity<PersonDTO> response = kitchenSinkController.createPerson(personDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getFirstName());
    }

    @Test
    void testDeletePerson() {
        // Arrange
        String personId = "1";
        doNothing().when(personService).deletePerson(personId); // Mock delete behavior

        // Act
        ResponseEntity<Void> response = kitchenSinkController.deletePerson(personId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personService, times(1)).deletePerson(personId); // Verify that delete was called once
    }
}