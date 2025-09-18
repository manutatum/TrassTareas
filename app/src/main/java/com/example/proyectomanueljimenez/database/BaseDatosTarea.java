package com.example.proyectomanueljimenez.database;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.proyectomanueljimenez.clases.Tarea;
import com.example.proyectomanueljimenez.daos.TareaDAO;
import com.example.proyectomanueljimenez.repositorios.RepositorioTareas;
import com.example.proyectomanueljimenez.repositorios.RepositorioTareasExterna;
import com.example.proyectomanueljimenez.repositorios.RepositorioTareasInterna;
import com.example.proyectomanueljimenez.typeConverters.DateConverter;
import com.example.proyectomanueljimenez.typeConverters.UriConverter;

@Database(entities = {Tarea.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, UriConverter.class})
public abstract class BaseDatosTarea extends RoomDatabase {

    private static volatile BaseDatosTarea INSTANCIA;

    public static BaseDatosTarea getInstance(Context context) {
        // Si se utiliza la BD interna, se construye la instancia Room.
        if (INSTANCIA == null) {
            INSTANCIA = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BaseDatosTarea.class,
                    "dbTareas"
            ).build();
        }

        return INSTANCIA;
    }

    public static void destroyInstance() {
        INSTANCIA = null;
    }

    public abstract TareaDAO tareaDAO();
}