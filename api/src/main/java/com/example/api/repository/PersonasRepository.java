package com.example.api.repository;

import com.example.api.model.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PersonasRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    // Encontrar a la persona con mayor edad
    Optional<Persona> findFirstByOrderByEdadDesc();

    // Encontrar a la persona con menor edad
    Optional<Persona> findFirstByOrderByEdadAsc();

    // Ordenar la lista de personas por edad en forma ascendente
    List<Persona> findAllByOrderByEdadAsc();

    // Ordenar la lista de personas por edad en forma descendente
    List<Persona> findAllByOrderByEdadDesc();

    Page<Persona> findAll(Specification<Persona> specification, Pageable pageable);
}
