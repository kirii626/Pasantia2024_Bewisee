package com.example.api.controller;

import com.example.api.dto.ConcesionariaDto;
import com.example.api.dto.ConcesionariaWithAutosDto;
import com.example.api.model.Concesionaria;
import com.example.api.service.ConcesionariaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/concesionaria")
public class ConcesionariaController {
    private final ConcesionariaService concesionariaService;

    public ConcesionariaController(ConcesionariaService concesionariaService) {
        this.concesionariaService = concesionariaService;
    }

    @PostMapping("/agregar-concesionaria") // o "/add-concesionaria"
    public ResponseEntity<?> addConcesionaria(@RequestBody Concesionaria concesionaria) {
        try {
            // Convertir Concesionaria a ConcesionariaDto
            ConcesionariaDto concesionariaDto = new ConcesionariaDto();
            concesionariaDto.setId(concesionaria.getId());
            concesionariaDto.setNombre(concesionaria.getNombre());
            concesionariaDto.setDireccion(concesionaria.getDireccion());
            concesionariaDto.setTelefono(concesionaria.getTelefono());

            // Llamar al servicio con el DTO
            ConcesionariaDto nuevaConcesionariaDto = concesionariaService.save(concesionariaDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaConcesionariaDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al agregar la concesionaria: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Concesionaria>> getConcesionaria() {
        try {
            List<Concesionaria> concesionarias = concesionariaService.findAllConcesionaria();
            return ResponseEntity.ok(concesionarias); // Si la lista es encontrada, devolver 200 OK con los datos
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // En caso de error, devolver un código 500 con cuerpo null
        }
    }

    @GetMapping("/{id}")
    public Optional<Concesionaria> getConcesionariaById(@PathVariable Long id) {
        return  concesionariaService.findConcesionariaById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concesionaria> updateConcesionaria(@PathVariable Long id, @RequestBody Concesionaria concesionariaActualizada) {
        // Verificar si la concesionaria con el ID proporcionado existe
        Optional<Concesionaria> dataOptional = concesionariaService.findConcesionariaById(id); //el optional valida que el elemento exista o no, ventaja en excepcion
        if (dataOptional.isPresent()){
            Concesionaria data = concesionariaService.editConcesionaria(dataOptional, concesionariaActualizada);
            return ResponseEntity.ok(data);
        } else {
            // Si la persona no fue encontrada, retornar un 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConcesionaria(@PathVariable Long id) {
        try {
            concesionariaService.deleteById(id);
            return ResponseEntity.ok("Concesionaria eliminada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró concesionaria con este ID: " + id);
        }
    }

    @GetMapping("/filter")
    public Page<ConcesionariaDto> findConcesionariaByFilter(Pageable pageable) {
        return concesionariaService.findConcesionariaByFilter(pageable);
    }

    // Endpoint para devolver las concesionarias con sus autos, paginado
    @GetMapping("/with-autos")
    public ResponseEntity<Page<ConcesionariaWithAutosDto>> getConcesionariasWithAutos(Pageable pageable) {
        Page<ConcesionariaWithAutosDto> concesionariasPage = concesionariaService.findConcesionariaWithAutosByFilter(pageable);
        return ResponseEntity.ok(concesionariasPage);
    }

}
