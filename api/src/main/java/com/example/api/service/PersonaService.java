package com.example.api.service;

import com.example.api.criteria.PersonaSpecification;
import com.example.api.dto.PersonaCriteriaDto;
import com.example.api.dto.PersonaDto;
import com.example.api.model.Persona;
import com.example.api.repository.PersonasRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PersonaService {
    private final PersonasRepository personasRepository;
    private static final String[] COLORS = {
            "Rojo", "Verde", "Azul", "Amarillo", "Naranja",
            "Púrpura", "Cyan", "Magenta", "Negro", "Blanco",
            "Gris", "Marrón", "Lima", "Turquesa", "Violeta"
    };

    public PersonaService(PersonasRepository personasRepository) {
        this.personasRepository = personasRepository;
    }

    public List<Persona> findAll() {
        return personasRepository.findAll();
    }

    public Persona save(Persona persona) {
        return personasRepository.save(persona);
    }

    public void deleteById(Long id) {
        personasRepository.deleteById(id);
    }

    public Optional<Persona> findById(Long id) {
        return personasRepository.findById(id);
    }

    public Persona editPerson(Optional<Persona> data, Persona personaActualizada) {
        data.get().setNombre(personaActualizada.getNombre());
        data.get().setApellido(personaActualizada.getApellido());
        data.get().setEdad(personaActualizada.getEdad());
        return personasRepository.save(data.get());
    }

    public Persona editPersona(Persona data, Persona personaActualizada) {
        data.setNombre(personaActualizada.getNombre());
        data.setApellido(personaActualizada.getApellido());
        data.setEdad(personaActualizada.getEdad());
        return personasRepository.save(data);
    }

    public Optional<Persona> findPersonaMayorEdad() {
        return personasRepository.findAll()
                .stream()
                .max((p1, p2) -> Integer.compare(p1.getEdad(), p2.getEdad()));
    }

    // Metodo para encontrar a la persona con menor edad
    public Optional<Persona> findPersonaMenorEdad() {
        return personasRepository.findAll()
                .stream()
                .min((p1, p2) -> Integer.compare(p1.getEdad(), p2.getEdad()));
    }

    // Metodo  para ordenar la lista de personas por edad (ascendente o descendente)
    public List<Persona> findPersonasOrdenadasPorEdad(String orden) {
        List<Persona> personas = personasRepository.findAll();
        if (orden.equalsIgnoreCase("asc")) {
            personas.sort((p1, p2) -> Integer.compare(p1.getEdad(), p2.getEdad())); // Ascendente
        } else if (orden.equalsIgnoreCase("desc")) {
            personas.sort((p1, p2) -> Integer.compare(p2.getEdad(), p1.getEdad())); // Descendente
        }
        return personas;
    }

    private PersonaDto convertToDto(Persona persona) {
        PersonaDto dto = new PersonaDto();
        dto.setId(persona.getId()); //el nuevo objeto dto obtiene los mismos datos que el de persona mediante el get
        dto.setNombre(persona.getNombre());
        dto.setApellido(persona.getApellido());
        dto.setEdad(persona.getEdad());
        dto.setFavColor(getRandomColor());
        return dto;
    }

    public List<PersonaDto> findAllDto() {
        List<Persona> personaList = personasRepository.findAll();
        List<PersonaDto> personaListDto = new ArrayList<>();
        for (Persona persona : personaList) {
            personaListDto.add(convertToDto(persona));
        }
        return personaListDto;
    }

    private String getRandomColor() {
        Random random = new Random();
        int index = random.nextInt(COLORS.length);
        return COLORS[index];
    }

    // Metodo que devuelve un PersonaDto basado en el ID
    public PersonaDto findByIdDto(Long id) {
        Persona persona = personasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
        return convertToDto(persona);  // Convierte la entidad Persona en un DTO
    }

    // Usar los métodos del repositorio para encontrar la persona con mayor edad
    public Optional<Persona> findPersonaMayorEdadSegundo() {
        return personasRepository.findFirstByOrderByEdadDesc();
    }

    // Usar los métodos del repositorio para encontrar la persona con menor edad
    public Optional<Persona> findPersonaMenorEdadSegundo() {
        return personasRepository.findFirstByOrderByEdadAsc();
    }


    // Metodo para ordenar la lista de personas por edad ascendente o descendente
    public List<Persona> findPersonasOrdenadasPorEdadSegundo(String orden) {
        if (orden.equalsIgnoreCase("asc")) {
            return personasRepository.findAllByOrderByEdadAsc();
        } else if (orden.equalsIgnoreCase("desc")) {
            return personasRepository.findAllByOrderByEdadDesc();
        } else {
            throw new IllegalArgumentException("El orden debe ser 'asc' o 'desc'.");
        }
    }

    public Page<Persona> findPersonaByFilter(PersonaCriteriaDto criteriaDto, Pageable pageable) {
        Specification<Persona> specification = PersonaSpecification.personaSpecification(criteriaDto);
        return personasRepository.findAll(specification, pageable);  // Cambia el orden de los parámetros
    }
}
