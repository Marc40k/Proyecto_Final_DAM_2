package com.example.proyectofinaldam2final;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentoNuevaPelicula extends Fragment {
    public FragmentoNuevaPelicula() {
        super(R.layout.fragment_nueva_pelicula);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Obtener referencias a los campos
        EditText inputTitulo = view.findViewById(R.id.inputTitulo);
        EditText inputAnyo = view.findViewById(R.id.inputAnyo);
        EditText inputGenero = view.findViewById(R.id.inputGenero);
        EditText inputDirector = view.findViewById(R.id.inputDirector);
        EditText inputNota = view.findViewById(R.id.inputNota);
        EditText inputCalificacion = view.findViewById(R.id.inputCalificacion);
        EditText inputFechaAdicion = view.findViewById(R.id.inputFechaAdicion);
        com.google.android.material.materialswitch.MaterialSwitch switchEstado = view.findViewById(R.id.switchEstado);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);

        // Obtener y mostrar fecha de hoy
        if (inputFechaAdicion.getText().toString().isEmpty()) {
            String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            inputFechaAdicion.setText(fechaHoy);
        }

        // Lógica al pulsar Guardar
        btnGuardar.setOnClickListener(v -> {
            String titulo = inputTitulo.getText().toString().trim();
            String anyoStr = inputAnyo.getText().toString().trim();
            String genero = inputGenero.getText().toString().trim();
            String director = inputDirector.getText().toString().trim();
            String nota = inputNota.getText().toString().trim();
            String calificacionStr = inputCalificacion.getText().toString().trim();
            String fechaAdicion = inputFechaAdicion.getText().toString().trim();
            boolean vista = switchEstado.isChecked();

            // Validación básica
            if (titulo.isEmpty()) {
                Toast.makeText(getContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            int anyo = anyoStr.isEmpty() ? 0 : Integer.parseInt(anyoStr);
            int calificacion = calificacionStr.isEmpty() ? 0 : Integer.parseInt(calificacionStr);


            // Crear objeto Pelicula
            Pelicula pelicula = new Pelicula();
            pelicula.titulo = titulo;
            pelicula.anyo = anyo;
            pelicula.genero = genero;
            pelicula.director = director;
            pelicula.nota = nota;
            pelicula.calificacion = calificacion;
            pelicula.fechaAdicion = fechaAdicion;
            pelicula.vista = vista;

            // Insertar en la base de datos
            AppDatabase db = AppDatabase.getInstance(getContext());
            db.peliculaDao().insertarPelicula(pelicula);

            Toast.makeText(getContext(), "Película guardada correctamente", Toast.LENGTH_SHORT).show();

            // (Opcional) limpiar los campos
            inputTitulo.setText("");
            inputAnyo.setText("");
            inputGenero.setText("");
            inputDirector.setText("");
            inputNota.setText("");
            inputCalificacion.setText("");
            inputFechaAdicion.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            switchEstado.setChecked(false);
        });
    }
}