package com.example.proyectofinaldam2final;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pelicula {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public int anyo;
    public String genero;
    public String director;
    public String nota;
    public int calificacion;
    public String fechaAdicion;
    public String fechaVista;
    public boolean vista;


        // Getters
    public String getTitulo() {
        return titulo;
    }
    public String getFechaAdicion() {
        return fechaAdicion;
    }
    public String getFechaVista() {
        return fechaVista;
    }
    public boolean isVista() {
        return vista;
        }
    public int getCalificacion() {
        return calificacion;
    }
    public String getNota() {
        return nota;
    }
    public String getDirector() {
        return director;
    }
    public String getGenero() {
        return genero;
    }
    public int getAnyo() {
        return anyo;
    }

    public void setVista(boolean b) {
        this.vista = b;
    }
    public void setFechaVista(String fecha) {
        this.fechaVista = fecha;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
