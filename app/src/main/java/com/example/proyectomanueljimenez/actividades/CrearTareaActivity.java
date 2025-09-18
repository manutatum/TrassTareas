package com.example.proyectomanueljimenez.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.clases.Tarea;
import com.example.proyectomanueljimenez.fragmentos.FragmentTareaDos;
import com.example.proyectomanueljimenez.fragmentos.FragmentTareaUno;
import com.example.proyectomanueljimenez.interfaces.ComunicacionFragmentoTareaDos;
import com.example.proyectomanueljimenez.interfaces.ComunicacionFragmentoTareaUno;
import com.example.proyectomanueljimenez.utils.FicherosUtils;
import com.example.proyectomanueljimenez.viewmodels.TareaViewModel;

import java.util.Date;

public class CrearTareaActivity extends AppCompatActivity implements ComunicacionFragmentoTareaUno, ComunicacionFragmentoTareaDos {

    private FragmentManager fragmentManager;
    private FragmentTareaUno fragmentTareaUno;
    private FragmentTareaDos fragmentTareaDos;

    private TareaViewModel tareaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tareaViewModel = new ViewModelProvider(this).get(TareaViewModel.class);

        fragmentTareaUno = new FragmentTareaUno();
        fragmentTareaDos = new FragmentTareaDos();

        fragmentManager = getSupportFragmentManager();

        // CARGAR EL PRIMER FRAGMENTO SI NO HAY NINGUNO
        if(savedInstanceState == null)
            fragmentManager.beginTransaction().add(R.id.contenedorFragmentos, fragmentTareaUno).commit();
    }

    @Override
    public void onClickBotonSiguiente() {
        if (!fragmentTareaDos.isAdded()) fragmentManager.beginTransaction().replace(R.id.contenedorFragmentos, fragmentTareaDos).commit();
    }

    @Override
    public void onClickBotonCancelar() {
        finish();
    }

    @Override
    public void onClickBotonVolver() {
        if (!fragmentTareaUno.isAdded()) fragmentManager.beginTransaction().replace(R.id.contenedorFragmentos, fragmentTareaUno).commit();
    }

    @Override
    public void onClickBotonAceptar() {
        if (camposValidos()){
            String tituloTarea = tareaViewModel.getTituloTarea().getValue();
            String descripcionTarea = tareaViewModel.getDescripcionTarea().getValue();

            int progresoTarea = tareaViewModel.getProgresoTarea().getValue() != null
                    ? tareaViewModel.getProgresoTarea().getValue()
                    : 0; // Valor predeterminado

            Date fechaCreacion = tareaViewModel.getFechaCreacionTarea().getValue();
            Date fechaFin = tareaViewModel.getFechaFinTarea().getValue();

            boolean isPrioritaria = tareaViewModel.getPrioridadTarea().getValue() != null
                    ? tareaViewModel.getPrioridadTarea().getValue()
                    : false; // Valor predeterminado

            // URL
            String urlDoc = (tareaViewModel.getUrlDoc().getValue() == null ) ? "" : tareaViewModel.getUrlDoc().getValue();
            String urlAud = (tareaViewModel.getUrlAud().getValue() == null ) ? "" : tareaViewModel.getUrlAud().getValue();
            String urlVid = (tareaViewModel.getUrlVid().getValue() == null ) ? "" : tareaViewModel.getUrlVid().getValue();
            String urlPho = (tareaViewModel.getUrlPho().getValue() == null ) ? "" : tareaViewModel.getUrlPho().getValue();


            // CREAMOS LA TAREA
            Tarea nuevaTarea = new Tarea(tituloTarea,descripcionTarea,progresoTarea,fechaCreacion,fechaFin,isPrioritaria,urlDoc,urlPho,urlAud,urlVid);

            // CREAMOS EL INTENT DE VUELTA
            Intent resultado = new Intent();
            resultado.putExtra("nuevaTarea",nuevaTarea);

            setResult(RESULT_OK,resultado);
            finish();

        }else{
            Toast.makeText(getApplicationContext(),"DEBES RELLENAR TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickBotonBorrar() {
        if (tareaViewModel.getUrlVid().getValue() != null){
            FicherosUtils.eliminarArchivo(this, tareaViewModel.getUrlVid().getValue());
            tareaViewModel.setUrlVid("");
        }

        if (tareaViewModel.getUrlDoc().getValue() != null){
            FicherosUtils.eliminarArchivo(this, tareaViewModel.getUrlDoc().getValue());
            tareaViewModel.setUrlDoc("");
        }

        if (tareaViewModel.getUrlAud().getValue() != null){
            FicherosUtils.eliminarArchivo(this, tareaViewModel.getUrlAud().getValue());
            tareaViewModel.setUrlAud("");
        }

        if (tareaViewModel.getUrlPho().getValue() != null){
            FicherosUtils.eliminarArchivo(this, tareaViewModel.getUrlPho().getValue());
            tareaViewModel.setUrlPho("");
        }
    }

    private boolean camposValidos() {
        // Comprobar si los campos del ViewModel son válidos
        return tareaViewModel.getDescripcionTarea().getValue() != null &&
                !tareaViewModel.getDescripcionTarea().getValue().isEmpty() &&
                tareaViewModel.getTituloTarea().getValue() != null &&
                !tareaViewModel.getTituloTarea().getValue().isEmpty() &&
                tareaViewModel.getFechaCreacionTarea().getValue() != null &&
                tareaViewModel.getFechaFinTarea().getValue() != null;
    }
}