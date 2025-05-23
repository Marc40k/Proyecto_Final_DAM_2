package com.example.proyectofinaldam2final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentoPeliculasVistas extends Fragment {

    private RecyclerView recyclerView;
    private PeliculaAdapter adapter;
    private AppDatabase db;

    public FragmentoPeliculasVistas() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peliculas_vistas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewVistas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(requireContext());

//        PeliculaAdapter.OnPeliculaClickListener listener = pelicula -> {
//            // Lógica de prueba - mostrar un Toast
//            Toast.makeText(requireContext(), "Has hecho clic en: " + pelicula.getTitulo(), Toast.LENGTH_SHORT).show();
//
//            // En el futuro: expandir el rectángulo o abrir un detalle
//        };


        // Cargar películas vistas desde la base de datos en un hilo de fondo
        new Thread(() -> {
            List<Pelicula> peliculasVistas = db.peliculaDao().getPeliculasVistas();

            requireActivity().runOnUiThread(() -> {
                adapter = new PeliculaAdapter(peliculasVistas, new PeliculaAdapter.OnPeliculaClickListener() {
                    @Override
                    public void onPeliculaClick(Pelicula pelicula) {
                        // expansión ya manejada en el adapter
                    }

                    @Override
                    public void onPeliculaLongClick(Pelicula pelicula) {
                        // abrir actividad/fragmento de edición
                        // ejemplo: abrirPeliculaDetalle(pelicula);
                    }

                    @Override
                    public void onVerClick(Pelicula pelicula) {
                        // No aplica en películas vistas
                    }
                }, false); // false = no es lista de pendientes

                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}