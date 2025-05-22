package com.example.proyectofinaldam2final;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentoListaPendiente extends Fragment {

    private RecyclerView recyclerPendientes;
    private PeliculaAdapter adapter;
    private AppDatabase db;


    // Esto ya no hace falta porque ahora se carga con OnCreateView, con el inflater
//    public FragmentoListaPendiente() {
//        super(R.layout.fragment_lista_pendiente);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_pendiente, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerPendientes = view.findViewById(R.id.recyclerPendientes);
        recyclerPendientes.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(requireContext());


        // Cargar películas pendientes desde la base de datos en un hilo de fondo
        new Thread(() -> {
            List<Pelicula> peliculasPendientes = db.peliculaDao().getPeliculasPendientes();

            requireActivity().runOnUiThread(() -> {
                adapter = new PeliculaAdapter(peliculasPendientes, new PeliculaAdapter.OnPeliculaClickListener() {
                    @Override
                    public void onPeliculaClick(Pelicula pelicula) {
                        // expansión visual, ya manejada en el adapter
                        // Creo que esto se va a quedar vacío (?)
                    }

                    @Override
                    public void onPeliculaLongClick(Pelicula pelicula) {
                        // El usuario ha hecho un click largo sobre la película: edición de la película
                        // Aquí implementaremos la lógica para editar la película
                    }

                    @Override
                    public void onVerClick(Pelicula pelicula) { // El usuario ha clickado en el botón de VER:
                        mostrarDialogoConfirmacion(pelicula);


                        // Antiguo comportamiento, sin un PopUP y sin modificación de la calificación.
//                        pelicula.setVista(true);
//                        pelicula.setFechaVista(obtenerFechaActual());
//
//                        new Thread(() -> {
//                            db.peliculaDao().actualizarPelicula(pelicula);
//
//                            List<Pelicula> actualizadas = db.peliculaDao().getPeliculasPendientes();
//                            requireActivity().runOnUiThread(() -> adapter.actualizarLista(actualizadas));
//                        }).start();
                    }
                }, true); // Es el último argumento de la clase ^ PeliculaAdapter. true = es lista de pendientes

                recyclerPendientes.setAdapter(adapter);
            });
        }).start();
    }

    private String obtenerFechaActual() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
    private void mostrarDialogoConfirmacion(Pelicula pelicula) {
        // Inflar la vista del diálogo
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_confirmar_vista, null);
        EditText etCalificacion = dialogView.findViewById(R.id.editTextCalificacion);

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Validar y procesar la calificación
                    String calificacionTexto = etCalificacion.getText().toString().trim();
                    int calificacion = 0;

                    if (!calificacionTexto.isEmpty()) {
                        try {
                            calificacion = Integer.parseInt(calificacionTexto);
                            if (calificacion < 0 || calificacion > 10) {
                                mostrarError("La calificación debe estar entre 0 y 10");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            mostrarError("Formato de calificación inválido, la calificación és un entero del 0 al 10");
                            return;
                        }
                    }

                    // Actualizar el modelo de datos
                    actualizarPeliculaComoVista(pelicula, calificacion);
                })
                .setNegativeButton("Cancelar", null) // Comportamiento por defecto
                .create()
                .show();
    }

    private void actualizarPeliculaComoVista(Pelicula pelicula, int calificacion) {
        pelicula.setVista(true);
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        pelicula.setFechaVista(fechaHoy);
        pelicula.setCalificacion(calificacion);

        // Ejecutar en segundo plano
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.peliculaDao().actualizarPelicula(pelicula);

            // Actualizar UI en el hilo principal
            requireActivity().runOnUiThread(() -> {
                cargarPeliculasPendientes();
                Toast.makeText(requireContext(),
                        "Película marcada como vista",
                        Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void mostrarError(String mensaje) {
        requireActivity().runOnUiThread(() ->
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        );
    }

    private void cargarPeliculasPendientes() {
        new Thread(() -> {
            List<Pelicula> peliculasPendientes = db.peliculaDao().obtenerPeliculasPorEstado(false);

            requireActivity().runOnUiThread(() -> {
                adapter.actualizarLista(peliculasPendientes);
            });
        }).start();
    }

}
