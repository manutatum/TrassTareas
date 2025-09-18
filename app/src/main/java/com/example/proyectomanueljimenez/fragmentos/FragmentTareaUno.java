package com.example.proyectomanueljimenez.fragmentos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.interfaces.ComunicacionFragmentoTareaUno;
import com.example.proyectomanueljimenez.viewmodels.TareaViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentTareaUno extends Fragment {

    private EditText tituloTarea,fechaCreacion,fechaObjetivo;
    private Spinner progresoTarea;
    private SwitchCompat prioridadTarea;
    private Button btSiguiente, btnCancelar;

    // CREAR VIEWMODEL
    private TareaViewModel tareaViewModel;

    public FragmentTareaUno() {}

    // COMUNICADOR
    private ComunicacionFragmentoTareaUno comunicador;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicacionFragmentoTareaUno) {
            comunicador = (ComunicacionFragmentoTareaUno) context;
        }else{
            throw new ClassCastException(context + " debe implementar ComunicacionFragmentoTareaUno");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tareaViewModel = new ViewModelProvider(requireActivity()).get(TareaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmento = inflater.inflate(R.layout.fragment_tarea_uno, container, false);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        int[] valoresProgreso = {0, 25, 50, 75, 100};

        // COGEMOS LOS ELEMENTOS Y LES PONEMOS ESCUCHADORES PARA GUARDAR SU VALOR CUANDO CAMBIE
        // CARGAMOS LOS VALORES QUE HAYA EN EL VIEW MODEL SI LOS TIENE GUARDADOS

        tituloTarea = fragmento.findViewById(R.id.etTituloTareaFragment);

        tituloTarea.setOnKeyListener((v, keyCode, event) -> {
            tareaViewModel.setTituloTarea(tituloTarea.getText().toString());
            return false;
        });

        tituloTarea.setText(tareaViewModel.getTituloTarea().getValue() != null ? tareaViewModel.getTituloTarea().getValue() : "");

        fechaCreacion = fragmento.findViewById(R.id.etFechaCreacionTareaFragment);

        fechaCreacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fechaCreacionDate = null;
                try {
                    fechaCreacionDate = sdf.parse(fechaCreacion.getText().toString());
                } catch (ParseException e) {
                    Log.e("FechaCreacionError", "Fecha Creacion inválida: " + e.getMessage());
                }
                if (fechaCreacionDate != null) tareaViewModel.setFechaCreacionTarea(fechaCreacionDate);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fechaCreacion.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getChildFragmentManager(),"datePicker");
        });

        if (tareaViewModel.getFechaCreacionTarea().getValue() != null) fechaCreacion.setText(sdf.format(tareaViewModel.getFechaCreacionTarea().getValue()));

        fechaObjetivo = fragmento.findViewById(R.id.etFechaObjetivoTareaFragment);

        fechaObjetivo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fechaObjetivoDate = null;
                try {
                    fechaObjetivoDate = sdf.parse(fechaObjetivo.getText().toString());
                } catch (ParseException e) {
                    Log.e("FechaObjetivoError", "Fecha Objetivo inválida: " + e.getMessage());
                }
                if (fechaObjetivoDate != null) tareaViewModel.setFechaFinTarea(fechaObjetivoDate);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fechaObjetivo.setOnClickListener(v -> {
            String fechaCreacionText = fechaCreacion.getText().toString();

            DatePickerFragment datePickerFragment = new DatePickerFragment();


            Bundle args = new Bundle();
            args.putString("fechaCreacion", fechaCreacionText);

            datePickerFragment.setArguments(args);
            datePickerFragment.show(getChildFragmentManager(),"datePicker");
        });

        if (tareaViewModel.getFechaFinTarea().getValue() != null) fechaObjetivo.setText(sdf.format(tareaViewModel.getFechaFinTarea().getValue()));

        progresoTarea = fragmento.findViewById(R.id.spinnerProgreso);

        progresoTarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tareaViewModel.setProgresoTarea(valoresProgreso[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Integer progreso = tareaViewModel.getProgresoTarea().getValue();

        if (progreso != null) {
            int selectedPosition = -1;

            // Verifica si el progreso está en el arreglo
            for (int i = 0; i < valoresProgreso.length; i++) {
                if (valoresProgreso[i] == progreso) {
                    selectedPosition = i;
                    break;
                }
            }

            // Si el progreso no está en los valores válidos, selecciona la primera posición (índice 0)
            if (selectedPosition != -1) {
                progresoTarea.setSelection(selectedPosition);
            } else {
                progresoTarea.setSelection(0); // Establecer una selección por defecto si el valor no está presente
            }
        }

        prioridadTarea = fragmento.findViewById(R.id.switchPrioritaria);

        prioridadTarea.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tareaViewModel.setPrioridadTarea(isChecked);
        });

        if (tareaViewModel.getPrioridadTarea().getValue() != null) prioridadTarea.setChecked(tareaViewModel.getPrioridadTarea().getValue());

        btSiguiente = fragmento.findViewById(R.id.btnSiguiente);

        // CLICK DEL BOTON
        btSiguiente.setOnClickListener(v -> {
            // LLAMAMOS A LA FUNCION
            comunicador.onClickBotonSiguiente();
        });

        btnCancelar = fragmento.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(v -> comunicador.onClickBotonCancelar());

        return fragmento;
    }

    private void guardarValoresViewModel() {
        tareaViewModel.setTituloTarea(tituloTarea.getText().toString());

        if (!fechaCreacion.getText().toString().isEmpty() && isFechaValida(fechaCreacion.getText().toString())){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fechaCreacionDate = null;
            try {
                fechaCreacionDate = sdf.parse(fechaCreacion.getText().toString());
            } catch (ParseException e) {
                Log.e("FechaCreacionError", "Fecha Creacion inválida: " + e.getMessage());
            }
            if (fechaCreacionDate != null) tareaViewModel.setFechaCreacionTarea(fechaCreacionDate);
        }

        if (!fechaObjetivo.getText().toString().isEmpty() && isFechaValida(fechaObjetivo.getText().toString())){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fechaObjetivoDate = null;
            try {
                fechaObjetivoDate = sdf.parse(fechaObjetivo.getText().toString());
            } catch (ParseException e) {
                Log.e("FechaObjetivoError", "Fecha Objetivo inválida: " + e.getMessage());
            }
            if (fechaObjetivoDate != null) tareaViewModel.setFechaFinTarea(fechaObjetivoDate);
        }

        if (progresoTarea.getSelectedItem() != null){
            int[] valores = {
                0,25,50,75,100
            };
            int posicion = progresoTarea.getSelectedItemPosition();
            tareaViewModel.setProgresoTarea(valores[posicion]);
        }

        tareaViewModel.setPrioridadTarea(prioridadTarea.isChecked());
    }

    private boolean isFechaValida(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            sdf.setLenient(false); // Para no aceptar fechas inválidas
            sdf.parse(fecha); // Si se lanza una ParseException, significa que la fecha no es válida
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void actualizarFechaCreacion(String fecha) {
        fechaCreacion.setText(fecha);
    }

    public void actualizarFechaObjetivo(String fecha) {
        fechaObjetivo.setText(fecha);
    }
}