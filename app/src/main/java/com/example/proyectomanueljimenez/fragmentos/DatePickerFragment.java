package com.example.proyectomanueljimenez.fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog con la fecha actual
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), this, year, month, day);

        // Obtener la fecha de creación pasada desde el Bundle
        if (getArguments() != null){
            String fechaCreacion = getArguments().getString("fechaCreacion", "");
            Calendar fechaCreacionCalendar = Calendar.getInstance();

            if (!fechaCreacion.isEmpty()) {
                String[] fechaParts = fechaCreacion.split("/");
                int dia = Integer.parseInt(fechaParts[0]);
                int mes = Integer.parseInt(fechaParts[1]) - 1;  // Los meses van de 0 a 11
                int anio = Integer.parseInt(fechaParts[2]);
                fechaCreacionCalendar.set(anio, mes, dia);
            }

            // Establecer la fecha mínima en el DatePickerDialog
            datePickerDialog.getDatePicker().setMinDate(fechaCreacionCalendar.getTimeInMillis());
        }

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // El formato que se va a mostrar en el EditText
        String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;

        FragmentTareaUno fragment = (FragmentTareaUno) getParentFragment();
        if (fragment != null){
            if (getArguments() != null){
                fragment.actualizarFechaObjetivo(fechaSeleccionada);
            }else{
                fragment.actualizarFechaCreacion(fechaSeleccionada);
            }
        }
    }
}
