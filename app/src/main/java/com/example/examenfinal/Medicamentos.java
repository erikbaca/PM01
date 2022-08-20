package com.example.examenfinal;

import java.io.Serializable;

public class Medicamentos implements Serializable  {


    private String id;
    private String nombre;
    private String descripcion;
    private String cantidad;
    private  String tiempo;
    private  String Periocidad;
    private  String url;

    Medicamentos(){

    }

    public Medicamentos(String id, String nombre, String descripcion, String cantidad, String tiempo, String periocidad, String url) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.tiempo = tiempo;
        Periocidad = periocidad;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getPeriocidad() {
        return Periocidad;
    }

    public void setPeriocidad(String periocidad) {
        Periocidad = periocidad;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
