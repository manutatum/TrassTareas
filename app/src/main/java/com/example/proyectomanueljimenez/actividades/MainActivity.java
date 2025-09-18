    package com.example.proyectomanueljimenez.actividades;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.view.animation.AnimationUtils;
    import android.widget.Button;
    import android.widget.ImageView;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.app.AppCompatDelegate;
    import androidx.preference.PreferenceManager;

    import com.example.proyectomanueljimenez.R;
    import com.example.proyectomanueljimenez.utils.ConfigurationUtil;
    import com.google.android.material.textview.MaterialTextView;

    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    public class MainActivity extends AppCompatActivity {

        private SharedPreferences preferences;

        private ImageView imageViewLogo;
        private MaterialTextView tvTitulo;

        private ExecutorService executor;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            aplicarTemaAplicacion();
            setContentView(R.layout.activity_main);

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // INICIALIZAMOS LOS COMPONENTES
            Button btnEntrar = findViewById(R.id.btnEntrar);
            imageViewLogo = findViewById(R.id.imageViewLogo);
            tvTitulo = findViewById(R.id.tvTitulo);

            executor = Executors.newSingleThreadExecutor();

            cargarAnimaciones();

            btnEntrar.setOnClickListener(this::mandarSegundaPantalla);
        }

        private void cargarAnimaciones() {

            executor.execute(() -> {
                runOnUiThread(() -> {
                    imageViewLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                    tvTitulo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                });
            });

        }

        private void aplicarTemaAplicacion() {

            preferences = PreferenceManager.getDefaultSharedPreferences(this);

            // TRUE = CLARO; FALSE = OSCURO
            boolean tema = preferences.getBoolean("preferenciasTema",true);

            float tamañoFuente = preferences.getFloat("tamañoFuente", 1.0f);


            ConfigurationUtil.actualizarTamanoFuente(this, tamañoFuente);


            if (tema) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

        }

        private void mandarSegundaPantalla(View view) {
            // Crear el Intent para iniciar la segunda actividad
            Intent intent = new Intent(MainActivity.this, ListadoTareas.class);
            startActivity(intent); // Iniciar la actividad
        }
    }