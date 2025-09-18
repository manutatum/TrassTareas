package com.example.proyectomanueljimenez.repositorios;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.proyectomanueljimenez.database.BaseDatosTarea;

public class RepositorioFactory {

    public static RepositorioTareas obtenerRepositorioTareas(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean bdExterna = preferences.getBoolean("preferencia_bd", false);

        if (bdExterna) {

            // Leemos la configuración de conexión para la base de datos externa de las preferencias
            String ip = preferences.getString("preferencia_ip", "10.0.2.2");
            String nombreBD = preferences.getString("preferencia_nombrebd", "trasstarea");
            String puertoDB = preferences.getString("preferencia_puerto", "8080");
            String usuario = preferences.getString("preferencia_usuario", "usuario");
            String password = preferences.getString("preferencia_password", "root");

            return new RepositorioTareasExterna(ip, nombreBD, puertoDB, usuario, password);
        } else {
            // Para la BD interna se utiliza Room, se obtiene la instancia de BaseDatosTarea
            BaseDatosTarea db = BaseDatosTarea.getInstance(context);
            return new RepositorioTareasInterna(db);
        }
    }
}
