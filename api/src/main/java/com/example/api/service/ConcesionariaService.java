package com.example.api.service;

import com.example.api.dto.AutoDto;
import com.example.api.dto.ConcesionariaDto;
import com.example.api.dto.ConcesionariaWithAutosDto;
import com.example.api.model.Concesionaria;
import com.example.api.repository.ConcesionariaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConcesionariaService {
    private final ConcesionariaRepository concesionariaRepository;
    private final AutoService autoService;

    public ConcesionariaService(ConcesionariaRepository concesionariaRepository, AutoService autoService) {
        this.concesionariaRepository = concesionariaRepository;
        this.autoService = autoService;
    }

    private ConcesionariaDto concesionariaToDto(Concesionaria concesionaria) {
        ConcesionariaDto concesionariaDto = new ConcesionariaDto();
        concesionariaDto.setId(concesionaria.getId());
        concesionariaDto.setNombre(concesionaria.getNombre());
        concesionariaDto.setDireccion(concesionaria.getDireccion());
        concesionariaDto.setTelefono(concesionaria.getTelefono());
        return concesionariaDto;
    }

    public List<ConcesionariaDto> findAllConcesionariaDto() {
        List<Concesionaria> concesionariaList = concesionariaRepository.findAll();
        List<ConcesionariaDto> concesionariaDtoList = new ArrayList<>();
        for (Concesionaria concesionaria : concesionariaList) {
            concesionariaDtoList.add(concesionariaToDto(concesionaria));
        }
        return concesionariaDtoList;
    }

    private Concesionaria concesionariaDtoToEntity(ConcesionariaDto concesionariaDto) {
        Concesionaria concesionaria = new Concesionaria();
        concesionaria.setId(concesionariaDto.getId());
        concesionaria.setNombre(concesionariaDto.getNombre());
        concesionaria.setDireccion(concesionariaDto.getDireccion());
        concesionaria.setTelefono(concesionariaDto.getTelefono());
        return concesionaria;
    }

    public List<Concesionaria> concesionariaDtoListToEntityList(List<ConcesionariaDto> concesionariaDtoList) {
        List<Concesionaria> concesionariaList = new ArrayList<>();
        for (ConcesionariaDto concesionariaDto : concesionariaDtoList) {
            concesionariaList.add(concesionariaDtoToEntity(concesionariaDto));
        }
        return concesionariaList;
    }

    public ConcesionariaDto save(ConcesionariaDto concesionariaDto) {
        //Convierte el DTO a entidad
        Concesionaria concesionaria = concesionariaDtoToEntity(concesionariaDto);
        //Guardar la entidad en la BD
        Concesionaria savedConcesionaria = concesionariaRepository.save(concesionaria);
        // Convertir la entidad guardada de nuevo a DTO (si necesitas retornar un DTO)
        return concesionariaToDto(savedConcesionaria);

    }

    public List<Concesionaria> findAllConcesionaria() {
        return concesionariaRepository.findAll();
    }

    public Optional<Concesionaria> findConcesionariaById(Long id) {
        return concesionariaRepository.findById(id);
    }

    public Concesionaria editConcesionaria(Optional<Concesionaria> dataOptional, Concesionaria concesionariaActualizada) {
        // Verificamos que el Optional contenga un valor
        if (dataOptional.isPresent()) {
            Concesionaria concesionaria = dataOptional.get();
            concesionaria.setNombre(concesionariaActualizada.getNombre());
            concesionaria.setDireccion(concesionariaActualizada.getDireccion());
            concesionaria.setTelefono(concesionariaActualizada.getTelefono());
            return concesionariaRepository.save(concesionaria);  // Guardamos la entidad actualizada
        } else {
            throw new RuntimeException("Concesionaria no encontrada");
        }
    }

    public void deleteById(Long id) {
        concesionariaRepository.deleteById(id);
    }

    public Page<ConcesionariaDto> findConcesionariaByFilter(Pageable pageable) {
        Page<Concesionaria> concesionariaPage = concesionariaRepository.findAll(pageable);
        return concesionariaPage.map(this::concesionariaToDto);
    }

    // Metodo de conversión a ConcesionariaWithAutosDto
    private ConcesionariaWithAutosDto convertToConcesionariaWithAutosDto(Concesionaria concesionaria) {
        ConcesionariaWithAutosDto dto = new ConcesionariaWithAutosDto();
        dto.setConcesionariaId(concesionaria.getId());
        dto.setNombre(concesionaria.getNombre());
        dto.setDireccion(concesionaria.getDireccion());
        dto.setTelefono(concesionaria.getTelefono());

        // Usar AutoService para convertir los autos asociados
        List<AutoDto> autosDto = concesionaria.getAutos().stream()
                .map(autoService::autoConvertDto)
                .collect(Collectors.toList());

        dto.setAutos(autosDto);
        return dto;
    }

    public Page<ConcesionariaWithAutosDto> findConcesionariaWithAutosByFilter(Pageable pageable) {
        // Obtener una página de concesionarias desde el repositorio
        Page<Concesionaria> concesionariaPage = concesionariaRepository.findAll(pageable);

        // Mapear las concesionarias a ConcesionariaWithAutosDto, incluyendo los autos asociados
        return concesionariaPage.map(this::convertToConcesionariaWithAutosDto);
    }
}
