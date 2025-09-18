package com.example.proyectomanueljimenez.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.example.proyectomanueljimenez.clases.Tarea;
import com.example.proyectomanueljimenez.repositorios.RepositorioFactory;
import com.example.proyectomanueljimenez.repositorios.RepositorioTareas;

import java.util.ArrayList;
import java.util.List;

public class ListadoTareasViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    // LiveData que almacena los parámetros de ordenación: (criterio, ordenAscendente)
    private final MutableLiveData<Pair<String, Boolean>> parametrosOrdenacion = new MutableLiveData<>();

    // LiveData que se obtiene transformando orderParams a través del DAO.
    private final LiveData<List<Tarea>> tareas;

    private SharedPreferences preferences;

    private RepositorioTareas repositorioTareas;

    public ListadoTareasViewModel(@NonNull Application application) {
        super(application);

        preferences = PreferenceManager.getDefaultSharedPreferences(application);

        preferences.registerOnSharedPreferenceChangeListener(this);

        // Inicializa los parámetros usando las preferencias actuales.
        String criterioInicial = getCriterioFromPrefs(preferences.getString("preferenciasCriterio", "1"));
        boolean ordenInicial = preferences.getBoolean("preferenciasOrden", true);

        repositorioTareas = RepositorioFactory.obtenerRepositorioTareas(application.getApplicationContext());

        parametrosOrdenacion.setValue(new Pair<>(criterioInicial, ordenInicial));

        // Usamos switchMap para transformar los parámetros en el LiveData de tareas.
        tareas = Transformations.switchMap(parametrosOrdenacion, parametros -> {
            // parametros.first es el criterio y parametros.second es el ordenAscendente.
            return obtenerTareasOrdenadas(parametros.first, parametros.second);
        });
    }

    public LiveData<List<Tarea>> getTareas() {
        return tareas;
    }

    public LiveData<List<Tarea>> obtenerTareasOrdenadas(String criterio, boolean ordenAscendente) {

        switch (criterio) {
            case "titulo":
                return ordenAscendente ? repositorioTareas.obtenerTareasAlfabeticasAscendente() :
                        repositorioTareas.obtenerTareasAlfabeticasDescendente();
            case "fechaCreacion":
                return ordenAscendente ? repositorioTareas.obtenerTareasPorFechaCreacionAscendente() :
                        repositorioTareas.obtenerTareasPorFechaCreacionDescendente();
            case "progreso":
                return ordenAscendente ? repositorioTareas.obtenerTareasPorProgresoAscendente() :
                        repositorioTareas.obtenerTareasPorProgresoDescendente();
            default:
                MutableLiveData<List<Tarea>> liveData = new MutableLiveData<>();
                liveData.setValue(new ArrayList<>());
                return liveData;
        }
    }

    private String getCriterioFromPrefs(String criterioValor) {
        switch (criterioValor) {
            case "1":
                return "titulo";
            case "2":
                return "fechaCreacion";
            case "4":
                return "progreso";
            default:
                return "titulo"; // Valor por defecto
        }
    }

    @Override
    protected void onCleared() {
        // Desregistramos el listener para evitar fugas de memoria
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onCleared();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if ("preferenciasCriterio".equals(key) || "preferenciasOrden".equals(key)) {
            // Actualiza los parámetros de ordenación cuando cambien las preferencias
            String nuevoCriterio = getCriterioFromPrefs(sharedPreferences.getString("preferenciasCriterio", "1"));

            boolean ordenAscendente = sharedPreferences.getBoolean("preferenciasOrden", true);

            parametrosOrdenacion.setValue(new Pair<>(nuevoCriterio, ordenAscendente));
        } else if ("preferencia_bd".equals(key)){
            actualizarRepositorioYRecargarTareas();
        }
    }

    private void actualizarRepositorioYRecargarTareas() {
        // Obtener el nuevo repositorio según la preferencia actual
        repositorioTareas = RepositorioFactory.obtenerRepositorioTareas(getApplication().getApplicationContext());

        // Forzar la recarga de las tareas
        Pair<String, Boolean> parametrosActuales = parametrosOrdenacion.getValue();
        if (parametrosActuales != null) {
            parametrosOrdenacion.setValue(parametrosActuales); // Esto disparará Transformations.switchMap
        }
    }
}