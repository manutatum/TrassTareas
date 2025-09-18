package com.example.proyectomanueljimenez.fragmentos;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.utils.ConfigurationUtil;

public class FragmentPreferencias extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferencias, rootKey);

        Preference temaSwitch = findPreference("preferenciasTema");
        Preference fuenteLetra = findPreference("preferenciasFuente");

        Preference criterio = findPreference("preferenciasCriterio");
        Preference ordenSwitch = findPreference("preferenciasOrden");

        // Cambio de tema
        if (temaSwitch != null) {
            temaSwitch.setOnPreferenceChangeListener((preference, valor) -> {
                boolean tema = (boolean) valor;
                cambiarTema(tema);
                return true;
            });
        }

        if (fuenteLetra != null) {
            fuenteLetra.setOnPreferenceChangeListener((preference, valor) -> {

                String tamañoFuente = String.valueOf(valor);
                float nuevoTamaño = 0;

                switch (tamañoFuente) {
                    case "1":
                        nuevoTamaño = 0.8f;
                        break;
                    case "2":
                        nuevoTamaño = 1.1f;
                        break;
                    case "3":
                        nuevoTamaño = 1.3f;
                        break;
                }

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.putFloat("tamañoFuente", nuevoTamaño);
                editor.apply();

                ConfigurationUtil.actualizarTamanoFuente(getContext(),nuevoTamaño);

                return true;
            });
        }

        if (criterio != null) {
            criterio.setOnPreferenceChangeListener((preference, newValue) -> {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();

                editor.putString("preferenciasCriterio", (String) newValue);
                editor.apply();

                return true;
            });
        }

        if (ordenSwitch != null) {
            ordenSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();

                editor.putBoolean("preferenciasOrden", (Boolean) newValue);
                editor.apply();

                return true;
            });
        }
    }

    private void cambiarTema ( boolean tema){
        if (tema) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}