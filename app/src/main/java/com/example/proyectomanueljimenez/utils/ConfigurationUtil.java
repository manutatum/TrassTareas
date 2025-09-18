package com.example.proyectomanueljimenez.utils;

import android.content.Context;
import android.content.res.Configuration;

public class ConfigurationUtil {

    public static void actualizarTamanoFuente(Context contexto, float nuevoTamano) {
        if (contexto != null) {
            // Obtener la configuración actual
            Configuration config = contexto.getResources().getConfiguration();

            // Modificar la escala de la fuente
            config.fontScale = nuevoTamano; // Cambiar la escala de la fuente

            // Aplicar los cambios sin necesidad de reiniciar la actividad
            contexto.getResources().updateConfiguration(config, contexto.getResources().getDisplayMetrics());
        }
    }

}
