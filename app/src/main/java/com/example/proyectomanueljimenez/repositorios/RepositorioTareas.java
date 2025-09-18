package com.example.proyectomanueljimenez.repositorios;

import androidx.lifecycle.LiveData;

import com.example.proyectomanueljimenez.clases.Tarea;

import java.util.Date;
import java.util.List;

public abstract class RepositorioTareas {

    public abstract LiveData<List<Tarea>> obtenerTareasAlfabeticasAscendente();
    public abstract LiveData<List<Tarea>> obtenerTareasAlfabeticasDescendente();
    public abstract LiveData<List<Tarea>> obtenerTareasPorFechaCreacionAscendente();
    public abstract LiveData<List<Tarea>> obtenerTareasPorFechaCreacionDescendente();
    public abstract LiveData<List<Tarea>> obtenerTareasPorProgresoAscendente();
    public abstract LiveData<List<Tarea>> obtenerTareasPorProgresoDescendente();
    public abstract LiveData<Integer> getTareasPrioritarias();
    public abstract LiveData<Integer> getTareasCompletadas();
    public abstract LiveData<Integer> getTareasAtrasadas(Date fechaActual);
    public abstract LiveData<Tarea> getTareaMasCercana(Date fechaActual);
    public abstract void insertTareas(Tarea... tareas);
    public abstract void deleteTarea(Tarea tarea);
    public abstract void updateTarea(Tarea tarea);

}
