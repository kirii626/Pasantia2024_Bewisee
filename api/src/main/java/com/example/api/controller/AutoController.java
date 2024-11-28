package com.example.api.controller;

import com.example.api.dto.AutoDto;
import com.example.api.model.Auto;
import com.example.api.service.AutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/autos")
public class AutoController {
    private final AutoService autoService;

    public AutoController(AutoService autoService) {
        this.autoService = autoService;
    }

    @PostMapping("/{concesionariaId}")
    public ResponseEntity<?> crearAuto(@PathVariable Long concesionariaId, @RequestBody Auto auto) {
        try {
            Auto nuevoAuto = autoService.saveAuto(concesionariaId, auto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAuto);
        } catch (RuntimeException e) {
            // Devolver un mensaje detallado de error
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Manejo de excepciones generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el auto: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Auto>> obtenerTodosLosAutos() {
        List<Auto> autos = autoService.obtenerTodosLosAutos();
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auto> obtenerAutoPorId(@PathVariable Long id) {
        Optional<Auto> auto = autoService.obtenerAutoPorId(id);
        if (auto.isPresent()) {
            return ResponseEntity.ok(auto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Auto> actualizarAuto(@PathVariable Long id, @RequestBody Auto autoActualizado) {
        try {
            Auto auto = autoService.actualizarAuto(id, autoActualizado);
            return ResponseEntity.ok(auto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAuto(@PathVariable Long id) {
        try {
            autoService.eliminarAuto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter")
    public Page<AutoDto> findAutoByFilter(Pageable pageable) {
        return autoService.findAutoByFilter(pageable);
    }
}
