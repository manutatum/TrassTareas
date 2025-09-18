package com.example.proyectomanueljimenez.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.clases.Tarea;
import com.example.proyectomanueljimenez.database.BaseDatosTarea;
import com.example.proyectomanueljimenez.fragmentos.FragmentTareaDos;
import com.example.proyectomanueljimenez.fragmentos.FragmentTareaUno;
import com.example.proyectomanueljimenez.interfaces.ComunicacionFragmentoTareaDos;
import com.example.proyectomanueljimenez.interfaces.ComunicacionFragmentoTareaUno;
import com.example.proyectomanueljimenez.utils.FicherosUtils;
import com.example.proyectomanueljimenez.viewmodels.TareaViewModel;

import java.util.Date;
import java.util.Objects;

public class EditarTareaActivity extends AppCompatActivity implements ComunicacionFragmentoTareaUno, ComunicacionFragmentoTareaDos {

    private Tarea tareaEditable;
    private TareaViewModel tareaViewModel;

    private FragmentManager fragmentManager;
    private final Fragment fragmento1 = new FragmentTareaUno();
    private final Fragment fragmento2 = new FragmentTareaDos();

    private String titulo, descripcion, urlDoc, urlAud, urlVid, urlPho;
    private String originalUrlDoc, originalUrlAud, originalUrlVid, originalUrlPho;
    private Date fechaCreacion, fechaFin;
    private Integer progreso;
    private Boolean prioritaria;

    private BaseDatosTarea bdTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                this.tareaEditable = (Tarea) bundle.getSerializable("tareaEditar");
            }
        }catch (NullPointerException e){
            Log.e("Bundle recibido nulo", e.toString());
        }

        if (tareaEditable != null) {
            originalUrlDoc = tareaEditable.getUrlDoc();
            originalUrlAud = tareaEditable.getUrlAud();
            originalUrlVid = tareaEditable.getUrlVid();
            originalUrlPho = tareaEditable.getUrlPho();
        }

        //Instanciamos el ViewModel
        tareaViewModel = new ViewModelProvider(this).get(TareaViewModel.class);

        //Instanciamos el gestor de fragmentos
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null)
            cambiarFragmento(fragmento1);

        tareaViewModel.setTituloTarea(tareaEditable.getTitulo());
        tareaViewModel.setDescripcionTarea(tareaEditable.getDescripcion());
        tareaViewModel.setPrioridadTarea(tareaEditable.isPrioritaria());
        tareaViewModel.setProgresoTarea(tareaEditable.getProgreso());
        tareaViewModel.setFechaFinTarea(tareaEditable.getFechaFin());
        tareaViewModel.setFechaCreacionTarea(tareaEditable.getFechaCreacion());
        tareaViewModel.setUrlPho(tareaEditable.getUrlPho());
        tareaViewModel.setUrlDoc(tareaEditable.getUrlDoc());
        tareaViewModel.setUrlAud(tareaEditable.getUrlAud());
        tareaViewModel.setUrlVid(tareaEditable.getUrlVid());
    }

    private void cambiarFragmento(Fragment fragment) {
        if (!fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedorFrags, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int fragmentID = Objects.requireNonNull(getSupportFragmentManager().
                findFragmentById(R.id.contenedorFrags)).getId();
        outState.putInt("fragmentoId", fragmentID);
    }

    @Override
    public void onClickBotonVolver() {
        descripcion = tareaViewModel.getDescripcionTarea().getValue();
        urlDoc = tareaViewModel.getUrlDoc().getValue();
        urlAud = tareaViewModel.getUrlAud().getValue();
        urlVid = tareaViewModel.getUrlVid().getValue();
        urlPho = tareaViewModel.getUrlPho().getValue();
        cambiarFragmento(fragmento1);
    }

    @Override
    public void onClickBotonAceptar() {
        // Guardamos todo
        descripcion = tareaViewModel.getDescripcionTarea().getValue();
        urlDoc = tareaViewModel.getUrlDoc().getValue();
        urlAud = tareaViewModel.getUrlAud().getValue();
        urlVid = tareaViewModel.getUrlVid().getValue();
        urlPho = tareaViewModel.getUrlPho().getValue();

        //Creamos un nuevo objeto tarea con los campos editados
        tareaEditable.setTitulo(titulo);
        tareaEditable.setPrioritaria(prioritaria);
        tareaEditable.setDescripcion(descripcion);
        tareaEditable.setFechaFin(fechaFin);
        tareaEditable.setFechaCreacion(fechaCreacion);
        tareaEditable.setProgreso(progreso);
        tareaEditable.setUrlDoc(urlDoc);
        tareaEditable.setUrlPho(urlPho);
        tareaEditable.setUrlAud(urlAud);
        tareaEditable.setUrlVid(urlVid);

        Intent resultado = new Intent();

        resultado.putExtra("TareaEditada",tareaEditable);

        setResult(RESULT_OK, resultado);
        finish();
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

    @Override
    public void onClickBotonSiguiente() {
        titulo = tareaViewModel.getTituloTarea().getValue();
        fechaCreacion = tareaViewModel.getFechaCreacionTarea().getValue();
        fechaFin = tareaViewModel.getFechaFinTarea().getValue();
        progreso = tareaViewModel.getProgresoTarea().getValue();
        prioritaria = tareaViewModel.getPrioridadTarea().getValue();

        cambiarFragmento(fragmento2);
    }

    @Override
    public void onClickBotonCancelar() {
        setResult(RESULT_CANCELED);
        finish();
    }


}