package com.example.proyectofinaldam2final;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Pelicula.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract PeliculaDao peliculaDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "peliculas-db")
                    .allowMainThreadQueries() // ¡Sólo para pruebas! Cambiar en producción.
                    .build();
        }
        return INSTANCE;
    }
}
