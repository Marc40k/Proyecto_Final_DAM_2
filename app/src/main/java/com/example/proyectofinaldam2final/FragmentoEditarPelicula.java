package com.example.proyectofinaldam2final;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentoEditarPelicula extends Fragment {

    private Pelicula pelicula;
    private AppDatabase db;

    // UI
    private TextView tvTituloEditar;
    private EditText etTitulo, etAnyo, etGenero, etDirector, etCalificacion, etNota, etFechaAdicion, etFechaVista;
    private com.google.android.material.materialswitch.MaterialSwitch switchEstado;
    private Button btnAceptar, btnCancelar, btnBorrar;

    public static FragmentoEditarPelicula nuevaInstancia(Pelicula pelicula) {
        FragmentoEditarPelicula fragmento = new FragmentoEditarPelicula();
        Bundle args = new Bundle();
        args.putSerializable("pelicula", pelicula);
        fragmento.setArguments(args);
        return fragmento;
    }

    public static FragmentoEditarPelicula newInstance(Pelicula pelicula) {
        FragmentoEditarPelicula fragment = new FragmentoEditarPelicula();
        Bundle args = new Bundle();
        args.putSerializable("pelicula", pelicula);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pelicula = (Pelicula) getArguments().getSerializable("pelicula");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_pelicula, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());
        pelicula = (Pelicula) getArguments().getSerializable("pelicula");

        // Referencias UI
        tvTituloEditar = view.findViewById(R.id.tvEditarTituloEditar);
        etTitulo = view.findViewById(R.id.etTitulo);
        etAnyo = view.findViewById(R.id.etAnyo);
        etGenero = view.findViewById(R.id.etGenero);
        etDirector = view.findViewById(R.id.etDirector);
        etCalificacion = view.findViewById(R.id.etCalificacion);
        etNota = view.findViewById(R.id.etNota);
        etFechaAdicion = view.findViewById(R.id.etFechaAdicion);
        etFechaVista = view.findViewById(R.id.etFechaVista);
        switchEstado = view.findViewById(R.id.switchEstado);

        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnBorrar = view.findViewById(R.id.btnBorrar);

        // Mostrar datos
        if (pelicula != null) {
            tvTituloEditar.setText("Editando \"" + pelicula.getTitulo() + "\"");
            etTitulo.setText(pelicula.getTitulo());
            etAnyo.setText(String.valueOf(pelicula.getAnyo()));
            etGenero.setText(pelicula.getGenero());
            etDirector.setText(pelicula.getDirector());
            etCalificacion.setText(String.valueOf(pelicula.getCalificacion()));
            etNota.setText(pelicula.getNota());
            etFechaAdicion.setText(pelicula.getFechaAdicion());
            etFechaVista.setText(pelicula.getFechaVista());
            switchEstado.setChecked(pelicula.isVista());
        }


        btnCancelar.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        btnAceptar.setOnClickListener(v -> {
            // Recoger datos actualizados del usuario
            pelicula.setTitulo(etTitulo.getText().toString());
            pelicula.setGenero(etGenero.getText().toString());
            pelicula.setDirector(etDirector.getText().toString());
            pelicula.setNota(etNota.getText().toString());
            pelicula.setAnyo(Integer.parseInt(etAnyo.getText().toString()));
            pelicula.setCalificacion(Integer.parseInt(etCalificacion.getText().toString()));

            String fechaAdicion = etFechaAdicion.getText().toString().trim();
            pelicula.setFechaAdicion(fechaAdicion.isEmpty() ? null : fechaAdicion);


            String fechaVista = etFechaVista.getText().toString().trim();
            pelicula.setFechaVista(fechaVista.isEmpty() ? null : fechaVista);


            // Actualizar en la base de datos en un hilo de fondo
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                db.peliculaDao().actualizarPelicula(pelicula);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Película actualizada", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            }).start();
        });

        btnBorrar.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("¿Borrar película?")
                    .setMessage("¿Estás seguro de que deseas eliminar esta película?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase db = AppDatabase.getInstance(requireContext());
                            db.peliculaDao().delete(pelicula);
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Película borrada", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack();
                            });
                        }).start();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void guardarCambios() {
        // Validación mínima
        String titulo = etTitulo.getText().toString().trim();
        if (TextUtils.isEmpty(titulo)) {
            Toast.makeText(getContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        pelicula.setTitulo(titulo);
        pelicula.setAnyo(parseInt(etAnyo.getText().toString()));
        pelicula.setGenero(etGenero.getText().toString());
        pelicula.setDirector(etDirector.getText().toString());
        pelicula.setCalificacion(parseInt(etCalificacion.getText().toString()));
        pelicula.setNota(etNota.getText().toString());
        pelicula.setFechaAdicion(etFechaAdicion.getText().toString());
        pelicula.setFechaVista(etFechaVista.getText().toString());
        pelicula.setVista(switchEstado.isChecked());

        new Thread(() -> {
            db.peliculaDao().actualizarPelicula(pelicula);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Película actualizada", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }).start();
    }

    private void confirmarBorrado() {
        new AlertDialog.Builder(getContext())
                .setTitle("¿Borrar película?")
                .setMessage("¿Estás seguro de que quieres eliminar esta película?")
                .setPositiveButton("Sí", (dialog, which) -> borrarPelicula())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void borrarPelicula() {
        new Thread(() -> {
            db.peliculaDao().delete(pelicula);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Película eliminada", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }).start();
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
