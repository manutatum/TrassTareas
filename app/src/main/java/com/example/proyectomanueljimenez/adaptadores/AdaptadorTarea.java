package com.example.proyectomanueljimenez.adaptadores;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.actividades.DescripcionActivity;
import com.example.proyectomanueljimenez.clases.Tarea;
import com.example.proyectomanueljimenez.repositorios.RepositorioFactory;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdaptadorTarea extends RecyclerView.Adapter<AdaptadorTarea.TareaViewHolder> {

    private List<Tarea> tareas;
    private boolean isPriorityActivated;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public AdaptadorTarea(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
        notifyDataSetChanged();
    }

    public void setPriorityActivated(boolean isPriorityActivated){
        this.isPriorityActivated = isPriorityActivated;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tarea,parent,false);

        return new TareaViewHolder(item,this);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {

        holder.bindTarea(tareas.get(position), isPriorityActivated);

    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgPrioridad;
        private final MaterialTextView tituloTarea;
        private final TextView diasRestantes;
        private final TextView fechaFin;
        private final ProgressBar barraProgreso;

        private ExecutorService executor = Executors.newSingleThreadExecutor();

        private final AdaptadorTarea adaptador;

        public TareaViewHolder(@NonNull View itemView, AdaptadorTarea adaptador) {
            super(itemView);

            executor.execute(() -> {
                itemView.post(() -> {
                    itemView.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.deslizar_dentro));
                });
            });

            this.adaptador = adaptador;

            imgPrioridad = itemView.findViewById(R.id.imgPrioridad);
            tituloTarea = itemView.findViewById(R.id.tvTitulo);
            diasRestantes = itemView.findViewById(R.id.tvDiasRestantes);
            fechaFin = itemView.findViewById(R.id.tvFecha);
            barraProgreso = itemView.findViewById(R.id.barraProgreso);

            // CREAMOS EL MENU CONTEXTUAL
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                MenuInflater inflater = new MenuInflater(itemView.getContext());
                inflater.inflate(R.menu.menu_contextual,menu);
                adaptador.selectedPosition = getAdapterPosition();
            });

        }

        public void bindTarea(Tarea tarea, boolean isPriorityActivated) {

            itemView.setOnClickListener(v -> {

                Log.i("tarea", (tarea != null) ? tarea.getTitulo() : "null");
                // Creamos un intent
                Intent intentDescripcion = new Intent(itemView.getContext(), DescripcionActivity.class);

                intentDescripcion.putExtra("tarea",tarea);

                // Lanzamos el intent
                itemView.getContext().startActivity(intentDescripcion);
            });

            imgPrioridad.setOnClickListener(v -> {
                // Cambiamos el estado de prioridad
                tarea.setPrioritaria(!tarea.isPrioritaria());

                actualizarTarea(tarea);

                // Actualizamos la imagen inmediatamente
                if (tarea.isPrioritaria()) {
                    imgPrioridad.setImageResource(R.drawable.star_prioritaria_24);
                } else {
                    imgPrioridad.setImageResource(R.drawable.star_no_prioritaria_24);
                }

                // Notifica al adaptador si es necesario (opcional)
                // Esto asegura que otras partes de la vista también se actualicen
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    adaptador.notifyItemChanged(getAdapterPosition());
                }
            });

            // Usamos setImageResource para asignar imágenes de recursos
            if (tarea.isPrioritaria()) {
                imgPrioridad.setImageResource(R.drawable.star_prioritaria_24);
                imgPrioridad.setColorFilter(ContextCompat.getColor(itemView.getContext(),R.color.color_estrella));
                tituloTarea.setTypeface(null, Typeface.BOLD);
            } else {
                imgPrioridad.setImageResource(R.drawable.star_no_prioritaria_24);
                imgPrioridad.setColorFilter(Color.BLACK);
                tituloTarea.setTypeface(null, Typeface.NORMAL);
            }

            tituloTarea.setText(tarea.getTitulo());

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();

            if (tarea.isPrioritaria() || !isPriorityActivated){
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }else{
                layoutParams.height = 0;
                layoutParams.width = 0;
            }

            // COMPROBAMOS SI ESTA COMPLETADA, SI LO ESTÁ TACHAMOS EL TEXTO SI NO LO ESTA
            // LO QUITAMOS
            if (tarea.getProgreso() == 100) {
                diasRestantes.setText(R.string.tvDias);
                tituloTarea.setPaintFlags(tituloTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tituloTarea.setPaintFlags(tituloTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            // CALCULAMOS LOS DIAS QUE QUEDAN O LOS DIAS QUE LLEVA TARDE
            Date fechaActual = new Date();
            Date fechaFinal = tarea.getFechaFin();

            // Formateamos la fecha de finalización en formato dd/MM/yyyy
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = sdf.format(fechaFinal);

            long diferencia = fechaFinal.getTime() - fechaActual.getTime();

            // Convertimos la diferencia a días
            long diasRestantesCantidad = diferencia / (1000 * 60 * 60 * 24);

            if (tarea.getProgreso() != 100){
                diasRestantes.setText(String.format("%d", diasRestantesCantidad));
                if (diferencia < 0){
                    diasRestantes.setTypeface(null, Typeface.BOLD);
                    diasRestantes.setTextColor(Color.RED);
                }else{
                    diasRestantes.setTypeface(null, Typeface.NORMAL);
                    diasRestantes.setTextColor(Color.BLACK);
                }
            }

            fechaFin.setText(fechaFormateada);

            barraProgreso.setProgress(Math.min(tarea.getProgreso(), 100));

        }

        private void actualizarTarea(Tarea tarea) {
            Executor executor = Executors.newSingleThreadExecutor();

            executor.execute(()->{
                RepositorioFactory.obtenerRepositorioTareas(itemView.getContext()).updateTarea(tarea);
            });
        }
    }
}