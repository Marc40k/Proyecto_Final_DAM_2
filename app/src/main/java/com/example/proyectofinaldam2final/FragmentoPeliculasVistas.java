package com.example.proyectofinaldam2final;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentoPeliculasVistas extends Fragment {

    private RecyclerView recyclerView;
    private PeliculaAdapter adapter;
    private AppDatabase db;

    private EditText etBuscar;
    private List<Pelicula> peliculasOriginales;




    public FragmentoPeliculasVistas() {
        // Constructor vacío
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peliculas_vistas, container, false);
    }

    private void filtrarPeliculas(String texto) {
        if (peliculasOriginales == null) return;

        String textoLower = texto.toLowerCase();
        List<Pelicula> filtradas = new java.util.ArrayList<>();

        for (Pelicula p : peliculasOriginales) {
            if (p.getTitulo().toLowerCase().contains(textoLower) ||
                    (p.getGenero() != null && p.getGenero().toLowerCase().contains(textoLower)) ||
                    (p.getDirector() != null && p.getDirector().toLowerCase().contains(textoLower))) {
                filtradas.add(p);
            }
        }

        adapter.actualizarLista(filtradas);
    }
    private int compararFechas(String f1, String f2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date d1 = sdf.parse(f1);
            Date d2 = sdf.parse(f2);
            if (d1 == null || d2 == null) return 0;
            return d1.compareTo(d2);
        } catch (Exception e) {
            return 0;
        }
    }
    private void mostrarPopupFiltros() { //Lógica para el diálogo popUp de los filtros
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_filtros, null);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(popupView).create();

        EditText etAnyo = popupView.findViewById(R.id.etAnyoFiltro);
        EditText etCalificacionMin = popupView.findViewById(R.id.etCalificacionMin);
        CheckBox cbConNotas = popupView.findViewById(R.id.cbConNotas);

        MaterialButtonToggleGroup tgTipoOrden = popupView.findViewById(R.id.tgTipoOrden);
        MaterialButtonToggleGroup tgDireccionOrden = popupView.findViewById(R.id.tgDireccionOrden);

        Button btnAplicar = popupView.findViewById(R.id.btnAplicarFiltros);
        btnAplicar.setOnClickListener(v -> {
            String anyoStr = etAnyo.getText().toString().trim();
            String califStr = etCalificacionMin.getText().toString().trim();
            boolean soloConNotas = cbConNotas.isChecked();

            Integer filtroAnyo = null;
            Integer filtroCalif = null;

            try {
                if (!anyoStr.isEmpty()) filtroAnyo = Integer.parseInt(anyoStr);
            } catch (NumberFormatException ignored) {}

            try {
                if (!califStr.isEmpty()) filtroCalif = Integer.parseInt(califStr);
            } catch (NumberFormatException ignored) {}

            int idOrden = tgTipoOrden.getCheckedButtonId();
            int idDir = tgDireccionOrden.getCheckedButtonId();

            Comparator<Pelicula> comparator = null;

            if (idOrden == R.id.rbOrdenCalificacion) {
                comparator = Comparator.comparingInt(Pelicula::getCalificacion);
            } else if (idOrden == R.id.rbOrdenDiaVisto) {
                comparator = (p1, p2) -> compararFechas(p1.getFechaVista(), p2.getFechaVista());
            } else if (idOrden == R.id.rbOrdenDiaPropuesto) {
                comparator = (p1, p2) -> compararFechas(p1.getFechaAdicion(), p2.getFechaAdicion());
            }

            if (comparator != null && idDir == R.id.rbOrdenDescendente) {
                comparator = comparator.reversed();
            }

            List<Pelicula> filtradas = new java.util.ArrayList<>();

            for (Pelicula p : peliculasOriginales) {
                boolean pasa = true;

                if (filtroAnyo != null) {
                    try {
                        String[] partes = p.getFechaVista().split("/");
                        int anyo = Integer.parseInt(partes[2]);
                        if (anyo != filtroAnyo) pasa = false;
                    } catch (Exception e) {
                        pasa = false;
                    }
                }

                if (pasa && filtroCalif != null && p.getCalificacion() < filtroCalif) {
                    pasa = false;
                }

                if (pasa && soloConNotas && (p.getNota() == null || p.getNota().trim().isEmpty())) {
                    pasa = false;
                }

                if (pasa) filtradas.add(p);
            }

            if (comparator != null) {
                filtradas.sort(comparator);
            }

            adapter.actualizarLista(filtradas);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(v -> mostrarPopupFiltros());
        recyclerView = view.findViewById(R.id.recyclerViewVistas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(requireContext());


        // Cargar películas vistas desde la base de datos en un hilo de fondo
        new Thread(() -> {
            List<Pelicula> peliculasVistas = db.peliculaDao().getPeliculasVistas();

            requireActivity().runOnUiThread(() -> {
                peliculasOriginales = peliculasVistas; // Almacenar una copia de las películas originales
                adapter = new PeliculaAdapter(peliculasVistas, new PeliculaAdapter.OnPeliculaClickListener() {
                    @Override
                    public void onPeliculaClick(Pelicula pelicula) {
                        // expansión ya manejada en el adapter
                    }

                    @Override
                    public void onPeliculaLongClick(Pelicula pelicula) {
                        FragmentoEditarPelicula fragmento = FragmentoEditarPelicula.newInstance(pelicula);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragmento) // Usa tu contenedor real
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onVerClick(Pelicula pelicula) {
                        // No aplica en películas vistas
                    }
                }, false); // false = porque no es lista de pendientes!

                recyclerView.setAdapter(adapter);
                // Setup búsqueda
                etBuscar = view.findViewById(R.id.etBuscar);
                etBuscar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filtrarPeliculas(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                    }); // Fin setup búsqueda
            });
        }).start();
    }
}