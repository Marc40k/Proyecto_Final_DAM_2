package com.example.proyectofinaldam2final;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PeliculaDao {

    @Insert
    void insertarPelicula(Pelicula pelicula);

    @Query("SELECT * FROM Pelicula WHERE vista = 0")
    List<Pelicula> obtenerPendientes();

    @Query("SELECT * FROM Pelicula WHERE vista = 1")
    List<Pelicula> obtenerVistas();

    @Query("SELECT * FROM Pelicula WHERE vista = 1 ORDER BY fechaVista DESC")
    List<Pelicula> getPeliculasVistas();



    @Query("SELECT * FROM Pelicula WHERE vista = 0 ORDER BY fechaAdicion DESC")
    List<Pelicula> getPeliculasPendientes();

    @Update
    void actualizarPelicula(Pelicula pelicula);

    @Query("SELECT * FROM Pelicula WHERE vista = :b")
    List<Pelicula> obtenerPeliculasPorEstado(boolean b);


    @Delete
    void delete(Pelicula pelicula);

}