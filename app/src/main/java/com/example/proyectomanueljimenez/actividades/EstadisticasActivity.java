package com.example.proyectomanueljimenez.actividades;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.database.BaseDatosTarea;
import com.example.proyectomanueljimenez.repositorios.RepositorioFactory;
import com.example.proyectomanueljimenez.repositorios.RepositorioTareas;

import java.util.Date;

public class EstadisticasActivity extends AppCompatActivity {

    private TextView tvTareasCompletadasResultado, tvTareasPrioritariasResultado, tvTareasAtrasadasResultado, tvTareaMasCercanaResultado;
    private RepositorioTareas repositorioTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estadisticas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        // MOSTRAR BARRA DE ACCION
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle("Estadisticas");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvTareasCompletadasResultado = findViewById(R.id.tvTareasCompletadasResultado);
        tvTareasPrioritariasResultado = findViewById(R.id.tvTareasPrioritariasResultado);
        tvTareasAtrasadasResultado = findViewById(R.id.tvTareasAtrasadasResultado);
        tvTareaMasCercanaResultado = findViewById(R.id.tvTareaMasCercanaResultado);

        repositorioTareas = RepositorioFactory.obtenerRepositorioTareas(this);


        repositorioTareas.getTareasCompletadas().observe(this, valor -> tvTareasCompletadasResultado.setText(valor.toString()));

        repositorioTareas.getTareasPrioritarias().observe(this, valor -> tvTareasPrioritariasResultado.setText(valor.toString()));

        repositorioTareas.getTareasAtrasadas(new Date()).observe(this, valor -> tvTareasAtrasadasResultado.setText(valor.toString()));

        repositorioTareas.getTareaMasCercana(new Date()).observe(this, tarea -> {
            if (tarea != null) {
                tvTareaMasCercanaResultado.setText(tarea.getTitulo());
            } else {
                tvTareaMasCercanaResultado.setText(R.string.no_tarea_cercana);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}