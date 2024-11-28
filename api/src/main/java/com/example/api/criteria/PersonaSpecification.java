package com.example.api.criteria;

import com.example.api.dto.PersonaCriteriaDto;
import com.example.api.model.Persona;
import org.springframework.data.jpa.domain.Specification;

public class PersonaSpecification {

    public static Specification<Persona> personaSpecification(PersonaCriteriaDto criteria) {
        return (root, query, criteriaBuilder) -> {
            // Inicializamos la especificación como nula para empezar.
            Specification<Persona> specification = Specification.where(null);

            // Validamos si el campo 'nombre' no es nulo en los criterios de búsqueda
            if (criteria.getNombre() != null) {
                specification = specification.and((root1, query1, cb) ->
                        cb.equal(root1.get("nombre"), criteria.getNombre())
                );
            }
            if (criteria.getApellido() != null) {
                specification = specification.and((root1, query1, cb) ->
                        cb.equal(root1.get("apellido"), criteria.getApellido())
                );
            }
            if (criteria.getEdad() != 0) {
                specification = specification.and((root1, query1, cb) ->
                        cb.equal(root1.get("edad"), criteria.getEdad())
                );
            }
            // Retornamos el Predicate generado por la especificación
            return specification.toPredicate(root, query, criteriaBuilder);
        };
    }
}
