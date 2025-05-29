package com.example.proyectofinaldam2final;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

@Entity
public class Pelicula implements Serializable {
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



    public boolean isVista() {
        return vista;
    }




    public int diasEnLaLista() {    // Función para calcular los días que ha estado la película en la lista
                                    // En verdad es una tontería pero me gustaría poder calcularlo
        if (this.fechaAdicion == null || this.fechaVista == null) {
            return -1; // En el xml lo traduciremos a "Aún no se ha visto" o algo por el estilo
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date fechaAd = sdf.parse(this.fechaAdicion);
            Date fechaVis = sdf.parse(this.fechaVista);

            long diffInMillis = fechaVis.getTime() - fechaAd.getTime();
            return (int) (diffInMillis / (1000 * 60 * 60 * 24)); // Milis a días

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
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
    public String getDirector() {
        return director;
    }

    public String getGenero() {
        return genero;
    }

    public int getAnyo() {
        return anyo;
    }
    public int getCalificacion() {
        return calificacion;
    }

    public String getNota() {
        return nota;
    }

    // Setters

    public void setVista(boolean b) {
        this.vista = b;
    }

    public void setFechaAdicion(String fecha) {
        this.fechaAdicion = fecha;
    }

    public void setFechaVista(String fecha) {
        this.fechaVista = fecha;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAnyo(int i) {
        this.anyo = i;
    }

    public void setGenero(String genero) {
        this.genero = genero;

    }


    public void setDirector(String string) {
        this.director = string;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

}
