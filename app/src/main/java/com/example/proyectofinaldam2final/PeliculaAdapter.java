package com.example.proyectofinaldam2final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {


    public interface OnPeliculaClickListener {
        void onPeliculaClick(Pelicula pelicula);
        void onPeliculaLongClick(Pelicula pelicula);
        void onVerClick(Pelicula pelicula);
    }

    private List<Pelicula> listaPeliculas;
    private OnPeliculaClickListener listener;
    private boolean esListaPendiente;
    private int posicionExpandida = -1;


    public PeliculaAdapter(List<Pelicula> listaPeliculas, OnPeliculaClickListener listener, boolean esListaPendiente) {
        this.listaPeliculas = listaPeliculas;
        this.listener = listener;
        this.esListaPendiente = esListaPendiente;
    }

    public class PeliculaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvFecha, tvCalificacion, tvNotas, tvDirector, tvGenero, tvAnyo, tvTiempoEnDias;
        Button btnVer;
        LinearLayout layExpandible;
        public PeliculaViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCalificacion = itemView.findViewById(R.id.tvCalificacion);
            btnVer = itemView.findViewById(R.id.btnVer);
            layExpandible = itemView.findViewById(R.id.layExpandible);
            tvNotas = itemView.findViewById(R.id.tvNotas);
            tvDirector = itemView.findViewById(R.id.tvDirector);
            tvGenero = itemView.findViewById(R.id.tvGenero);
            tvAnyo = itemView.findViewById(R.id.tvAnyo);
            tvTiempoEnDias = itemView.findViewById(R.id.tiempoEnDias);
        }

        public void bind(Pelicula pelicula, OnPeliculaClickListener listener, int position) {
            String fecha;
            tvTitulo.setText(pelicula.getTitulo());
            btnVer.setVisibility(esListaPendiente ? View.VISIBLE : View.GONE);


            // Expansión del rectángulo al hacer click
            if (position == posicionExpandida) {
                layExpandible.setVisibility(View.VISIBLE);
                tvDirector.setText(pelicula.getDirector() != null && !pelicula.getDirector().isEmpty()
                        ? "Director: " + pelicula.getDirector()
                        : "Sin director");
                tvGenero.setText(pelicula.getGenero() != null && !pelicula.getGenero().isEmpty()
                        ? "Género: " + pelicula.getGenero()
                        : "Género desconocido");
                tvAnyo.setText(pelicula.getAnyo() > 0 ? "Año: " + pelicula.getAnyo() : "Año desconocido");
                tvNotas.setText(pelicula.getNota() != null && !pelicula.getNota().isEmpty()
                        ? "Notas: " + pelicula.getNota()
                        : "Sin notas");
                if (pelicula.isVista()) {
                    tvTiempoEnDias.setVisibility(View.VISIBLE);
                    tvTiempoEnDias.setText(pelicula.diasEnLaLista() + " días");
                } else {
                    tvTiempoEnDias.setVisibility(View.GONE);
                }

            } else {
                layExpandible.setVisibility(View.GONE);
            }


            //Es la lista de películas pendientes o la lista de películas vistas:
            if (esListaPendiente) { // Lista de películas pendientes
                fecha = pelicula.getFechaAdicion();
                tvFecha.setText((fecha != null && !fecha.trim().isEmpty()) ? "Añadida: " + fecha : "Sin fecha");
            } else { // Lista de películas vistas
                fecha = pelicula.getFechaVista();
                tvFecha.setText((fecha != null && !fecha.trim().isEmpty()) ? "Vista: " + fecha : "Sin fecha");
                tvCalificacion.setText(pelicula.getCalificacion() > 0 ? pelicula.getCalificacion() + "/10" : "-");
            }

            // Click en la película
            itemView.setOnClickListener(v -> {
                int prev = posicionExpandida;
                if (position == posicionExpandida) {
                    posicionExpandida = RecyclerView.NO_POSITION;
                } else {
                    posicionExpandida = position;
                }
                notifyItemChanged(prev);
                notifyItemChanged(posicionExpandida);

                if (listener != null) listener.onPeliculaClick(pelicula);
            });

            // Long click en la película
            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onPeliculaLongClick(pelicula);
                return true;
            });

            // Click en el botón VER
            btnVer.setOnClickListener(v -> {
                if (listener != null) listener.onVerClick(pelicula);
            });
        }
    }


    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pelicula, parent, false);
        return new PeliculaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula pelicula = listaPeliculas.get(position);
        holder.bind(pelicula, listener, position);
    }

    @Override
    public int getItemCount() {
        return listaPeliculas.size();
    }

    public void actualizarLista(List<Pelicula> nuevasPeliculas) {
        this.listaPeliculas = nuevasPeliculas;
        posicionExpandida = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }
}

