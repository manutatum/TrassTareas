    package com.example.proyectomanueljimenez.actividades;

    import android.app.AlertDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.preference.PreferenceManager;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.proyectomanueljimenez.adaptadores.AdaptadorTarea;
    import com.example.proyectomanueljimenez.R;
    import com.example.proyectomanueljimenez.clases.Tarea;
    import com.example.proyectomanueljimenez.database.BaseDatosTarea;
    import com.example.proyectomanueljimenez.repositorios.RepositorioFactory;
    import com.example.proyectomanueljimenez.repositorios.RepositorioTareas;
    import com.example.proyectomanueljimenez.utils.FicherosUtils;
    import com.example.proyectomanueljimenez.viewmodels.ListadoTareasViewModel;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;
    import java.util.concurrent.Executor;
    import java.util.concurrent.Executors;

    public class ListadoTareas extends AppCompatActivity {

        private RecyclerView rvTareas;
        private TextView tvNoTareas;
        private static List<Tarea> tareas = new ArrayList<>();
        private boolean isPriorityActivated;
        private AdaptadorTarea adaptador;
        private RepositorioTareas repositorioTareas;
        private ListadoTareasViewModel listadoTareasViewModel;

        // LAUNCHER PARA CREAR TAREA
        private ActivityResultLauncher<Intent> crearTareaLauncher;
        private ActivityResultLauncher<Intent> editarTareaLauncher;

        private Tarea tareaSeleccionada;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_listado_tareas);

            listadoTareasViewModel =new ViewModelProvider(this).get(ListadoTareasViewModel.class);

            repositorioTareas = RepositorioFactory.obtenerRepositorioTareas(this);

            // MOSTRAR BARRA DE ACCION
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
                getSupportActionBar().setTitle("Listado de Tareas");
            }

            tvNoTareas = findViewById(R.id.tvNoTareas);

            //Creamos el adaptador
            adaptador = new AdaptadorTarea(tareas);

            if (savedInstanceState != null) {
                isPriorityActivated = savedInstanceState.getBoolean("isPriorityActivated", false);
                actualizarAdaptador();
            }

            rvTareas = findViewById(R.id.rvTareas);
            rvTareas.setAdapter(adaptador);
            //Asignamos un LinearLayout vertical al RecyclerView de forma que los datos se vean en formato lista.
            rvTareas.setLayoutManager( new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

            registerForContextMenu(rvTareas);

            //CARGAMOS LOS DATOS
            listadoTareasViewModel.getTareas().observe(this, tareasActualizadas -> {
                if (tareasActualizadas != null) {
                    adaptador.setTareas(tareasActualizadas);
                    actualizarAdaptador();
                    tareas = tareasActualizadas;
                    actualizarVisibilidad();
                } else {
                    Log.e("ListadoTareas", "Tareas actualizadas es null");
                }
            });

            // Actualizamos la visibilidad al iniciar
            actualizarVisibilidad();

            crearTareaLauncher = registerForActivityResult(
              new ActivityResultContracts.StartActivityForResult(),
              result -> {
                  if (result.getResultCode() == RESULT_OK) {
                      Intent data = result.getData();
                      if (data != null) {
                          Tarea nuevaTarea = (Tarea) data.getSerializableExtra("nuevaTarea");

                          Executor executor = Executors.newSingleThreadExecutor();

                          executor.execute(new InsertarTarea(nuevaTarea));

                          tareas.add(nuevaTarea);

                          adaptador.notifyItemInserted(tareas.size() - 1);

                          actualizarVisibilidad();
                      }
                  }
              }
            );

            editarTareaLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Tarea tareaEditada = (Tarea) data.getSerializableExtra("TareaEditada");

                                int posicionTarea = tareas.indexOf(tareaSeleccionada);

                                Executor executor = Executors.newSingleThreadExecutor();

                                executor.execute(new EditarTarea(tareaEditada));

                                tareas.set(posicionTarea,tareaEditada);

                                adaptador.notifyItemChanged(posicionTarea);

                                actualizarVisibilidad();
                            }
                        }
                    }
            );
        }

        private void actualizarAdaptador() {
            if (adaptador != null) {
                adaptador.setPriorityActivated(isPriorityActivated);
                adaptador.notifyDataSetChanged();
            }
        }

        private void actualizarVisibilidad() {
            if (tareas.isEmpty()){
                rvTareas.setVisibility(View.GONE);
                tvNoTareas.setVisibility(View.VISIBLE);
            } else {
                rvTareas.setVisibility(View.VISIBLE);
                tvNoTareas.setVisibility(View.GONE);
            }
        }

        // INFLAR EL MENU QUE TENGAMOS CREADO
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_principal, menu);
            menu.setGroupVisible(R.id.group_info,true);
            menu.setGroupVisible(R.id.group_gestion,true);

            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.it_salir){
                String fraseFin = getString(R.string.frase_final);
                Toast.makeText(this, fraseFin, Toast.LENGTH_LONG).show();
                finishAffinity();
            } else if (id == R.id.it_acerca){
                mostrarDialog();
            } else if (id == R.id.it_prioritarias){
                if (!tareas.isEmpty()){
                    isPriorityActivated = !isPriorityActivated;
                    actualizarAdaptador();
                    if(isPriorityActivated){
                        Toast.makeText(this, "Mostrando las tareas prioritarias", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.star_prioritaria_50);
                    }else{
                        Toast.makeText(this, "Mostrando todas las tareas", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.star_no_prioritaria_50);
                    }
                }else{
                    Toast.makeText(this, "NO HAY TAREAS", Toast.LENGTH_SHORT).show();
                }

            } else if (id == R.id.it_crear_tarea) {
                Intent intentCrearTarea = new Intent(this, CrearTareaActivity.class);
                crearTareaLauncher.launch(intentCrearTarea);
            } else if (id == R.id.it_preferencias){
                Intent intent = new Intent(this, PreferenciasActivity.class);
                startActivity(intent);
            } else if (id == R.id.it_estadisticas) {
                Intent intent = new Intent(this, EstadisticasActivity.class);
                startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onContextItemSelected(@NonNull MenuItem item) {
            int selectedPosition = adaptador.getSelectedPosition();

            // COMPROBACION PARA SABER SI HA SELECCIONADO ALGO
            if (selectedPosition == RecyclerView.NO_POSITION) return super.onContextItemSelected(item);

            tareaSeleccionada = tareas.get(selectedPosition);

            if (item.getItemId() == R.id.menuItemEditar){

                Intent intentEditarTarea = new Intent(this, EditarTareaActivity.class);
                intentEditarTarea.putExtra("tareaEditar", tareaSeleccionada);
                editarTareaLauncher.launch(intentEditarTarea);

            }else if (item.getItemId() == R.id.menuItemBorrar){
                borrarTarea(selectedPosition);
            }
            return super.onContextItemSelected(item);
        }

        private void borrarTarea(int selectedPosition) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.borrar_tarea_titulo))
                    .setMessage(getString(R.string.borrar_tarea_mensaje) + " " +tareaSeleccionada.getTitulo() + "?")
                    .setPositiveButton(getString(R.string.btn_aceptar), (dialogInterface, i) -> {
                        Executor executor = Executors.newSingleThreadExecutor();

                        executor.execute(new BorrarTarea(tareaSeleccionada));

                        // Cuando borramos una tarea eliminamos todos los archivos relacionados con el
                        FicherosUtils.eliminarArchivo(this, tareaSeleccionada.getUrlDoc(),tareaSeleccionada.getUrlAud(),tareaSeleccionada.getUrlPho(), tareaSeleccionada.getUrlVid());

                        adaptador.notifyItemRemoved(selectedPosition);

                        actualizarVisibilidad();

                        tareas.remove(tareaSeleccionada);

                        dialogInterface.dismiss();
                    })
                    .setNegativeButton(getString(R.string.btn_cancelar), (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        }

        private void mostrarDialog() {
            String titulo = getString(R.string.titulo_dialog);
            String instituto = getString(R.string.nombre_centro);
            String autor = getString(R.string.nombre_autor);
            int year = Calendar.getInstance().get(Calendar.YEAR);

            new AlertDialog.Builder(this)
                    .setTitle(titulo)
                    .setMessage(instituto + "\n" + autor + "\n" + year)
                    .setPositiveButton(getString(R.string.btn_aceptar), (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        }

        // GUARDAMOS EL ESTADO DE PRIORIDAD
        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putBoolean("isPriorityActivated", isPriorityActivated);
        }

        class BorrarTarea implements Runnable {

            private Tarea tarea;

            public BorrarTarea(Tarea tarea) {
                this.tarea = tarea;
            }

            @Override
            public void run() {
                repositorioTareas.deleteTarea(tarea);
            }
        }

        class InsertarTarea implements Runnable {

            private Tarea tarea;

            public InsertarTarea(Tarea tarea) {
                this.tarea = tarea;
            }

            @Override
            public void run() {
                repositorioTareas.insertTareas(tarea);
            }
        }

        class EditarTarea implements Runnable {

            private Tarea tarea;

            public EditarTarea(Tarea tarea) {
                this.tarea = tarea;
            }

            @Override
            public void run() {
                Log.i("tareaEditada", (tarea != null) ? tarea.getTitulo() : "null");
                repositorioTareas.updateTarea(tarea);
            }
        }
    }