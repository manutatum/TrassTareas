package com.example.proyectomanueljimenez.repositorios;

import androidx.lifecycle.LiveData;

import com.example.proyectomanueljimenez.clases.Tarea;
import com.example.proyectomanueljimenez.database.BaseDatosTarea;

import java.util.Date;
import java.util.List;

public class RepositorioTareasInterna extends RepositorioTareas{

    private BaseDatosTarea baseDatosTarea;

    public RepositorioTareasInterna(BaseDatosTarea baseDatosTarea) {
        this.baseDatosTarea = baseDatosTarea;
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasAlfabeticasAscendente() {
        return baseDatosTarea.tareaDAO().obtenerTareasAlfabeticasAscendente();
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasAlfabeticasDescendente() {
        return baseDatosTarea.tareaDAO().obtenerTareasAlfabeticasDescendente();
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorFechaCreacionAscendente() {
        return baseDatosTarea.tareaDAO().obtenerTareasPorFechaCreacionAscendente();
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorFechaCreacionDescendente() {
        return baseDatosTarea.tareaDAO().obtenerTareasPorFechaCreacionDescendente();
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorProgresoAscendente() {
        return baseDatosTarea.tareaDAO().obtenerTareasPorProgresoAscendente();
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorProgresoDescendente() {
        return baseDatosTarea.tareaDAO().obtenerTareasPorProgresoDescendente();
    }

    @Override
    public LiveData<Integer> getTareasPrioritarias() {
        return baseDatosTarea.tareaDAO().getTareasPrioritarias();
    }

    @Override
    public LiveData<Integer> getTareasCompletadas() {
        return baseDatosTarea.tareaDAO().getTareasCompletadas();
    }

    @Override
    public LiveData<Integer> getTareasAtrasadas(Date fechaActual) {
        return baseDatosTarea.tareaDAO().getTareasAtrasadas(new Date());
    }

    @Override
    public LiveData<Tarea> getTareaMasCercana(Date fechaActual) {
        return baseDatosTarea.tareaDAO().getTareaMasCercana(new Date());
    }

    @Override
    public void insertTareas(Tarea... tareas) {
        baseDatosTarea.tareaDAO().insertTareas(tareas);
    }

    @Override
    public void deleteTarea(Tarea tarea) {
        baseDatosTarea.tareaDAO().deleteTarea(tarea);
    }

    @Override
    public void updateTarea(Tarea tarea) {
        baseDatosTarea.tareaDAO().updateTarea(tarea);
    }
}
