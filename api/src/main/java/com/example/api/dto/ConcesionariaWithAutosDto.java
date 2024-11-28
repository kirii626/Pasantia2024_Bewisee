package com.example.api.dto;

import java.util.List;

public class ConcesionariaWithAutosDto {

    private Long concesionariaId;

    private String nombre;

    private String direccion;

    private String telefono;

    private List<AutoDto> autos;  // Autos asociados

    public Long getConcesionariaId() {
        return concesionariaId;
    }

    public void setConcesionariaId(Long concesionariaId) {
        this.concesionariaId = concesionariaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<AutoDto> getAutos() {
        return autos;
    }

    public void setAutos(List<AutoDto> autos) {
        this.autos = autos;
    }
}
