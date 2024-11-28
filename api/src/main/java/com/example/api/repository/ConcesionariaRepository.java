package com.example.api.repository;

import com.example.api.model.Concesionaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcesionariaRepository extends JpaRepository<Concesionaria, Long>, JpaSpecificationExecutor<Concesionaria> {

}