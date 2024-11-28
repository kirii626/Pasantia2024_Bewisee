package com.example.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Auto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "marca")
        private String marca;

        @Column(name = "modelo")
        private String modelo;

        @Column(name = "anio")
        private int anio;

        @Column(name = "precio")
        private float precio;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "concesionaria_id")
        @JsonBackReference
        private Concesionaria concesionaria;

    public Auto() {
    }

    public Auto(Long id, String marca, String modelo, int anio, float precio, Concesionaria concesionaria) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
        this.concesionaria = concesionaria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Concesionaria getConcesionaria() {
        return concesionaria;
    }

    public void setConcesionaria(Concesionaria concesionaria) {
        this.concesionaria = concesionaria;
    }
}
