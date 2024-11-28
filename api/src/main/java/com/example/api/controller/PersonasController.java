    package com.example.api.controller;


    import com.example.api.dto.PersonaCriteriaDto;
    import com.example.api.dto.PersonaDto;
    import com.example.api.model.Persona;
    import com.example.api.repository.PersonasRepository;
    import com.example.api.service.PersonaService;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping(path = "api/personas")
    public class PersonasController {
        private List<Persona> personas = new ArrayList<>();
        private final PersonasRepository personasRepository;
        private final PersonaService personaService;

        public PersonasController(PersonasRepository personasRepository, PersonaService personaService) {
            this.personasRepository = personasRepository;
            this.personaService = personaService;
        }


        @GetMapping("/")
        public List<PersonaDto> getPersona() {
            return personaService.findAllDto();
        }

        @GetMapping("/{id}")
        public PersonaDto getPersonaById(@PathVariable Long id){
            return personaService.findByIdDto(id);
        }

        @PostMapping("/añadir-persona")
        public ResponseEntity<Persona> addPersona(@RequestBody Persona persona) {  //La anotación @PathVariable le dice a Spring que el valor de id debe extraerse de la parte correspondiente de la URL.
            try {
                Persona nuevaPersona = personaService.save(persona);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deletePersonaById(@PathVariable Long id) {  //La anotación @PathVariable le dice a Spring que el valor de id debe extraerse de la parte correspondiente de la URL.
            try {
                personaService.deleteById(id);
                return ResponseEntity.ok("Persona eliminada con éxito.");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró persona con este ID: " + id);
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<Persona> updatePersona(@PathVariable Long id, @RequestBody Persona personaActualizada) {
            // Verificar si la persona con el ID proporcionado existe
            Optional<Persona> dataOptional = personaService.findById(id); //el optional valida que el elemento exista o no, ventaja en excepcion
            if (dataOptional.isPresent()){
                Persona data = personaService.editPerson(dataOptional,personaActualizada);
                return ResponseEntity.ok(data);
            } else {
                // Si la persona no fue encontrada, retornar un 404
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }


        @PutMapping("/edit-person/{id}")
        public ResponseEntity<Persona> updatePersonaReal(@PathVariable Long id, @RequestBody Persona personaActualizada) {
            // Verificar si la persona con el ID proporcionado existe
            Persona personaExistente = personaService.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
            // Guardar la persona actualizada
            Persona personaGuardada = personaService.editPersona(personaExistente,personaActualizada);

            // Retornar la respuesta con la persona actualizada
            return ResponseEntity.ok(personaGuardada);
        }

        @GetMapping("/edad/mayor")
        public ResponseEntity<Persona> getPersonaMayorEdad(){
            return personaService.findPersonaMayorEdad()
                    .map(ResponseEntity::ok)
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @GetMapping("/edad/menor")
        public ResponseEntity<Persona> getPersonaMenorEdad() {
            return personaService.findPersonaMenorEdad()
                    .map(ResponseEntity::ok)
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @GetMapping("/edad/{orden}")
        public ResponseEntity<List<Persona>> getPersonasOrdenadasPorEdad(@PathVariable String orden) {
            List<Persona> personas = personaService.findPersonasOrdenadasPorEdad(orden);
            return ResponseEntity.ok(personas);
        }

        @GetMapping("/edad/max")
        public ResponseEntity<Persona> getPersonaMayorEdadSecond() {
            return personaService.findPersonaMayorEdadSegundo()
                    .map(ResponseEntity::ok)
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @GetMapping("/edad/min")
        public ResponseEntity<Persona> getPersonaMenorEdadSecond() {
            return personaService.findPersonaMenorEdadSegundo()
                    .map(ResponseEntity::ok)
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @GetMapping("/edad/orden/{orden}")
        public ResponseEntity<List<Persona>> getPersonasOrdenadasPorEdadSegundo(@PathVariable String orden) {
            try {
                List<Persona> personasOrdenadas = personaService.findPersonasOrdenadasPorEdadSegundo(orden);
                return ResponseEntity.ok(personasOrdenadas);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @GetMapping("/filter")
        public Page<Persona> personaByFilter(@RequestBody PersonaCriteriaDto criteriaDto, Pageable page) {
            return personaService.findPersonaByFilter(criteriaDto, page);
        }

    }
