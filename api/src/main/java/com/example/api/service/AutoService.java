package com.example.api.service;

import com.example.api.dto.AutoDto;
import com.example.api.model.Auto;
import com.example.api.model.Concesionaria;
import com.example.api.repository.AutoRepository;
import com.example.api.repository.ConcesionariaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AutoService {
    private final AutoRepository autoRepository;
    private final ConcesionariaRepository concesionariaRepository;

    // Inyecta ambos repositorios en el constructor
    public AutoService(AutoRepository autoRepository, ConcesionariaRepository concesionariaRepository) {
        this.autoRepository = autoRepository;
        this.concesionariaRepository = concesionariaRepository;
    }


    public AutoDto autoConvertDto(Auto auto) {
        AutoDto autoDto = new AutoDto();
        autoDto.setId(auto.getId());
        autoDto.setMarca(auto.getMarca());
        autoDto.setModelo(auto.getModelo());
        autoDto.setAnio(auto.getAnio());
        autoDto.setPrecio(auto.getPrecio());
        return autoDto;
    }

    public List<AutoDto> findAllAutoDto() {
        List<Auto> autoList = autoRepository.findAll();
        List<AutoDto> autoDtoList = new ArrayList<>();
        for (Auto auto : autoList) {
            autoDtoList.add(autoConvertDto(auto));
        }
        return autoDtoList;
    }

    private Auto autoDtoToEntity(AutoDto autoDto) {
        Auto auto = new Auto();
        auto.setId(autoDto.getId());
        auto.setModelo(autoDto.getModelo());
        auto.setMarca(autoDto.getMarca());
        auto.setAnio(autoDto.getAnio());
        auto.setPrecio(autoDto.getPrecio());
        return auto;
    }

    public List<Auto> autoDtoListToEntityList(List<AutoDto> autoDtoList) {
        List<Auto> autoList = new ArrayList<>();
        for (AutoDto autoDto : autoDtoList) {
            autoList.add(autoDtoToEntity(autoDto));
        }
        return autoList;
    }

    public Auto saveAuto(Long concesionariaId, Auto auto) {
        // Busca la concesionaria por ID
        Optional<Concesionaria> concesionaria = concesionariaRepository.findById(concesionariaId);
        if (concesionaria.isPresent()) {
            auto.setConcesionaria(concesionaria.get());  // Asocia el auto a la concesionaria
            return autoRepository.save(auto);            // Guarda el auto con la referencia a la concesionaria
        } else {
            throw new RuntimeException("Concesionaria no encontrada con ID: " + concesionariaId);
        }
    }

    public List<Auto> obtenerTodosLosAutos() {
        return autoRepository.findAll();
    }

    public Optional<Auto> obtenerAutoPorId(Long id) {
        return autoRepository.findById(id);
    }

    public Auto actualizarAuto(Long id, Auto autoActualizado) {
        Optional<Auto> autoOptional = autoRepository.findById(id);
        if (autoOptional.isPresent()) {
            Auto auto = autoOptional.get();
            auto.setMarca(autoActualizado.getMarca());
            auto.setModelo(autoActualizado.getModelo());
            auto.setAnio(autoActualizado.getAnio());
            auto.setPrecio(autoActualizado.getPrecio());
            return autoRepository.save(auto);
        } else {
            throw new RuntimeException("Auto no encontrado con ID: " + id);
        }
    }

    public void eliminarAuto(Long id) {
        Optional<Auto> auto = autoRepository.findById(id);
        if (auto.isPresent()) {
            autoRepository.delete(auto.get());
        } else {
            throw new RuntimeException("Auto no encontrado con ID: " + id);
        }
    }

    public Page<AutoDto> findAutoByFilter(Pageable pageable) {
        Page<Auto> autoPage = autoRepository.findAll(pageable);
        return autoPage.map(this::autoConvertDto);
    }
}