package com.example.proyectomanueljimenez.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectomanueljimenez.clases.Tarea;

import java.util.Date;
import java.util.List;

@Dao
public interface TareaDAO {

    @Query("SELECT * FROM tarea ORDER BY titulo ASC")
    LiveData<List<Tarea>> obtenerTareasAlfabeticasAscendente();

    @Query("SELECT * FROM tarea ORDER BY titulo DESC")
    LiveData<List<Tarea>> obtenerTareasAlfabeticasDescendente();

    @Query("SELECT * FROM tarea ORDER BY fechaCreacion ASC")
    LiveData<List<Tarea>> obtenerTareasPorFechaCreacionAscendente();

    @Query("SELECT * FROM tarea ORDER BY fechaCreacion DESC")
    LiveData<List<Tarea>> obtenerTareasPorFechaCreacionDescendente();

//    @Query("SELECT * FROM tarea ORDER BY diasRestantes ASC")
//    LiveData<List<Tarea>> obtenerTareasPorDiasRestantesAscendente();
//
//    @Query("SELECT * FROM tarea ORDER BY diasRestantes DESC")
//    LiveData<List<Tarea>> obtenerTareasPorDiasRestantesDescendente();

    @Query("SELECT * FROM tarea ORDER BY progreso ASC")
    LiveData<List<Tarea>> obtenerTareasPorProgresoAscendente();

    @Query("SELECT * FROM tarea ORDER BY progreso DESC")
    LiveData<List<Tarea>> obtenerTareasPorProgresoDescendente();

    @Query("SELECT COUNT(*) FROM tarea WHERE prioritaria = 1")
    LiveData<Integer> getTareasPrioritarias();

    @Query("SELECT COUNT(*) FROM tarea WHERE progreso = 100")
    LiveData<Integer> getTareasCompletadas();

    @Query("SELECT COUNT(*) FROM tarea WHERE fechaFin < :fechaActual")
    LiveData<Integer> getTareasAtrasadas(Date fechaActual);

    @Query("SELECT * FROM tarea WHERE fechaFin > :fechaActual ORDER BY fechaFin ASC LIMIT 1")
    LiveData<Tarea> getTareaMasCercana(Date fechaActual);

    @Insert
    void insertTareas(Tarea... tareas);

    @Delete
    void deleteTarea(Tarea tarea);

    @Update
    void updateTarea(Tarea tarea);
}
