//package com.example.proyectofinaldam2final;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//

// CLASE DEPRECADA (deprecated?)
// LA BORRARÉ MÁS ADELANTE



//public class PeliculaPendienteAdapter extends RecyclerView.Adapter<PeliculaPendienteAdapter.ViewHolder> {
//
//    public interface OnVerClickListener {
//        void onVerClick(Pelicula pelicula);
//    }
//
//    private List<Pelicula> listaPeliculas;
//    private OnVerClickListener listener;
//
//    public PeliculaPendienteAdapter(List<Pelicula> listaPeliculas, OnVerClickListener listener) {
//        this.listaPeliculas = listaPeliculas;
//        this.listener = listener;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvTitulo, tvFechaAdicion;
//        Button btnVer;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tvTitulo = itemView.findViewById(R.id.tvTituloPendiente);
//            tvFechaAdicion = itemView.findViewById(R.id.tvFechaAdicion);
//            btnVer = itemView.findViewById(R.id.btnVer);
//        }
//
//        public void bind(Pelicula pelicula, OnVerClickListener listener) {
//            tvTitulo.setText(pelicula.getTitulo());
//            tvFechaAdicion.setText("Añadida: " + pelicula.getFechaAdicion());
//
//            btnVer.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onVerClick(pelicula);
//                }
//            });
//        }
//    }
//
//    @NonNull
//    @Override
//    public PeliculaPendienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View vista = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_pelicula_pendiente, parent, false);
//        return new ViewHolder(vista);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Pelicula pelicula = listaPeliculas.get(position);
//        holder.bind(pelicula, listener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return listaPeliculas.size();
//    }
//}
