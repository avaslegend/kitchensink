package com.app.kitchensink.application.service;

import com.app.kitchensink.application.dto.PersonDTO;
import com.app.kitchensink.application.exception.EmailAlreadyExistsException;
import com.app.kitchensink.application.exception.PersonNotFoundException;
import com.app.kitchensink.domain.model.Person;
import com.app.kitchensink.infraestructure.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Person entities.
 */
@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    /**
     * Constructs a PersonService with the specified PersonRepository.
     *
     * @param personRepository the repository to be used for Person entities
     */
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

     /**
     * Retrieves all persons with pagination.
     *
     * @param page of the pagination information
     * @param size of the pagination information
     * @return a Page containing Person entities
     */
    // @Cacheable(value = "people", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<PersonDTO> getAllPersons(int page, int size) {
        // Fetch all persons from the repository
        List<Person> allPersons = personRepository.findAll();
        
        // Sort persons by firstName in a case-insensitive manner
        allPersons.sort(Comparator.comparing(Person::getFirstName, String.CASE_INSENSITIVE_ORDER));
        
        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), allPersons.size());

        List<PersonDTO> personDTOs = allPersons.stream()
                                                    .map(this::convertToDTO) // Map each Person to PersonDTO
                                                    .collect(Collectors.toList());
        // Return a Page object
        return new PageImpl<>(personDTOs.subList(start, end), pageable, allPersons.size());
    }

    /**
     * Saves a new person to the database.
     *
     * @param person the Person entity to be saved
     * @return the saved Person entity
     */
    @CachePut(value = "people", key = "#personDTO.email")
    public PersonDTO savePerson(PersonDTO personDTO) {
        logger.info("Saving person: {}", personDTO);
        Person person = convertToEntity(personDTO);

        if (personRepository.findByEmail(person.getEmail()) != null) {
            throw new EmailAlreadyExistsException(person.getEmail());
        }
        Person savedPerson = personRepository.save(person);
        evictCache(); // Invalidate the cache after saving
        return convertToDTO(savedPerson);
    }

    @CacheEvict(value = "people", allEntries = true)
    public void evictCache() {
        // This will clear the cache for the paginated list
    }

    /**
     * Deletes a person by their ID.
     *
     * @param id the ID of the person to delete
     * @throws PersonNotFoundException if no person with the given ID is found
     */
    @CacheEvict(value = "people", key = "#id")
    public void deletePerson(String id) {
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        logger.info("Deleting person with ID: {}", id);
        personRepository.deleteById(id);
        evictCache();
    }

    /**
     * Converts a Person entity to a PersonDto.
     *
     * @param person the Person entity to convert
     * @return the converted PersonDto
     */
    private PersonDTO convertToDTO(Person person) {
        PersonDTO personDto = new PersonDTO();
        personDto.setId(person.getId());
        personDto.setFirstName(person.getFirstName());
        personDto.setLastName(person.getLastName());
        personDto.setEmail(person.getEmail());
        return personDto;
    }

    /**
     * Converts a PersonDto to a Person entity.
     *
     * @param personDto the PersonDto to convert
     * @return the converted Person entity
     */
    private Person convertToEntity(PersonDTO personDto) {
        Person person = new Person();
        person.setId(personDto.getId());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setEmail(personDto.getEmail());
        return person;
    }
}