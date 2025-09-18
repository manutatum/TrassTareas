package com.example.proyectomanueljimenez.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

public class FicherosUtils {

    public static void eliminarArchivo(Context contexto, String... rutasArchivo) {

        boolean isBorrado = true;

        for (String rutaArchivo : rutasArchivo) {

            if (!rutaArchivo.isBlank()){
                File archivo = new File(rutaArchivo);

                if (archivo.exists()) {
                    if (!archivo.delete()) {
                        isBorrado = false;
                    }
                } else {
                    isBorrado = false;
                }
            }
        }

        if (!isBorrado){
            Toast.makeText(contexto, "Error al borrar los archivos", Toast.LENGTH_SHORT).show();
        }
    }
}
