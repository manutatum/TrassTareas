package com.example.proyectomanueljimenez.actividades;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.clases.Tarea;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DescripcionActivity extends AppCompatActivity {

    private TextView tvTitulo, tvDescripcion, tvDocumento, tvPhoto, tvAudio, tvVideo;

    private ImageButton imgbtnDoc, imgbtnPho, imgbtnAud, imgbtnVid;

    private MediaPlayer mediaPlayer;

    private Executor executor;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_descripcion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Tarea tarea = (Tarea) intent.getSerializableExtra("tarea");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (tarea != null) {
            tvTitulo = findViewById(R.id.tvTituloDetalles);
            tvDescripcion = findViewById(R.id.tvDescripcionDetalles);
            tvDocumento = findViewById(R.id.tvDocumento);
            tvPhoto = findViewById(R.id.tvPhoto);
            tvAudio = findViewById(R.id.tvAudio);
            tvVideo = findViewById(R.id.tvVideo);

            // BOTONES DE ACCION DE DOCUMENTOS
            imgbtnDoc = findViewById(R.id.imgbtnDoc);
            imgbtnPho = findViewById(R.id.imgbtnPho);
            imgbtnAud = findViewById(R.id.imgbtnAud);
            imgbtnVid = findViewById(R.id.imgbtnVid);

            executor = Executors.newSingleThreadExecutor();
            handler = new Handler(Looper.getMainLooper());

            // ASIGNAMOS LOS TEXTOS
            cargarTextosTarea(tarea);

            imgbtnDoc.setOnClickListener(v -> {});

            imgbtnPho.setOnClickListener(v -> {
                mostrarImagen(tarea);
            });

            imgbtnAud.setOnClickListener(v -> {
                mostrarAudio(tarea);
            });

            imgbtnVid.setOnClickListener(v -> {
                mostrarVideo(tarea);
            });
        } else {
            // EN CASO DE HABER UN ERROR AL PASAR LA TAREA, SE MOSTRARA UN ERROR
            Toast.makeText(this, R.string.error_detalles_tarea, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarVideo(Tarea tarea) {
        if (!tarea.getUrlVid().trim().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.img_btn_doc_speakeable_text);

            File archivo = new File(tarea.getUrlVid());

            if (archivo.exists()){
                try {
                    Uri uriVideo = FileProvider.getUriForFile(this, "com.example.proyectomanueljimenez.fileprovider", archivo);

                    getApplicationContext().grantUriPermission(
                            getPackageName(),
                            uriVideo,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));

                    VideoView video = new VideoView(this);

                    LinearLayout.LayoutParams videoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                    video.setLayoutParams(videoParams);
                    video.setVideoURI(uriVideo);

                    ProgressBar progresoVideo = new ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
                    progresoVideo.setMax(video.getDuration());

                    ImageButton btnPlay = new ImageButton(this);
                    btnPlay.setImageIcon(Icon.createWithResource(this, R.drawable.baseline_play_arrow_24));

                    LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, // Ancho
                            LinearLayout.LayoutParams.MATCH_PARENT  // Alto
                    );

                    btnParams.setMargins(0,10,0,10);
                    btnPlay.setLayoutParams(btnParams);

                    Runnable updateProgress = new Runnable() {
                        @Override
                        public void run() {
                            if (video.isPlaying()) {
                                progresoVideo.setProgress(video.getCurrentPosition());
                                handler.postDelayed(this, 500);
                            }
                        }
                    };

                    btnPlay.setOnClickListener(v1 -> {
                        if (!video.isPlaying()){
                            video.start();
                            progresoVideo.setMax(video.getDuration());
                            handler.post(updateProgress);
                        }
                    });

                    video.setOnCompletionListener(mp -> {
                        progresoVideo.setProgress(0);
                        handler.removeCallbacks(updateProgress);
                    });

                    layout.addView(video);
                    layout.addView(btnPlay);
                    layout.addView(progresoVideo);

                    builder.setView(layout);

                    builder.setNegativeButton("Cancelar", (dialog, which) -> {
                        handler.removeCallbacks(updateProgress);
                        dialog.dismiss();
                    });

                }catch (IllegalArgumentException e){
                    Log.e("FileProvider", "Error en URI: " + e.getMessage());
                }
            }


            builder.show();
        }
    }

    private void mostrarAudio(Tarea tarea) {
        if (!tarea.getUrlAud().trim().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.img_btn_doc_speakeable_text);

            File archivo = new File(tarea.getUrlAud());

            if (archivo.exists()){
                try {
                    Uri uriAudio = FileProvider.getUriForFile(this, "com.example.proyectomanueljimenez.fileprovider", archivo);

                    getApplicationContext().grantUriPermission(
                            getPackageName(),
                            uriAudio,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));

                    mediaPlayer = MediaPlayer.create(this, uriAudio);

                    ProgressBar progresoAudio = new ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
                    progresoAudio.setMax(mediaPlayer.getDuration());

                    ImageButton btnPlay = new ImageButton(this);
                    btnPlay.setImageIcon(Icon.createWithResource(this, R.drawable.baseline_play_arrow_24));

                    LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, // Ancho
                            LinearLayout.LayoutParams.MATCH_PARENT  // Alto
                    );

                    btnParams.setMargins(0,10,0,10);
                    btnPlay.setLayoutParams(btnParams);

                    btnPlay.setOnClickListener(v1 -> {
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()){
                            mediaPlayer.setLooping(false);
                            mediaPlayer.start();

                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPosition = mediaPlayer.getCurrentPosition();
                                        progresoAudio.post(() -> progresoAudio.setProgress(currentPosition));
                                    }
                                    handler.postDelayed(this, 500);
                                }
                            });
                        }
                    });

                    layout.addView(btnPlay);
                    layout.addView(progresoAudio);

                    builder.setView(layout);

                }catch (IllegalArgumentException e){
                    Log.e("FileProvider", "Error en URI: " + e.getMessage());
                }
            }
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                dialog.dismiss();
            });

            builder.setOnCancelListener(dialog -> {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            });

            builder.show();
        }
    }

    private void mostrarImagen(Tarea tarea) {
        if (!tarea.getUrlPho().trim().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.img_btn_doc_speakeable_text);

            File archivo = new File(tarea.getUrlPho());

            if (archivo.exists()){
                try {
                    Uri uriFoto = FileProvider.getUriForFile(this, "com.example.proyectomanueljimenez.fileprovider", archivo);

                    getApplicationContext().grantUriPermission(
                            getPackageName(),
                            uriFoto,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));

                    ImageView imagen = new ImageView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            700
                    );
                    imagen.setLayoutParams(params);
                    imagen.setAdjustViewBounds(true);
                    imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imagen.setPadding(5,5,5,5);

                    Glide.with(this)
                            .load(uriFoto)
                            .into(imagen);

                    layout.addView(imagen);
                    builder.setView(layout);
                }catch (IllegalArgumentException e){
                    Log.e("FileProvider", "Error en URI: " + e.getMessage());
                }
            }
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();
        }
    }

    /**
     *
     * Carga los TextView con los datos de la tarea
     *
     * @param tarea Objeto de la clase {@link Tarea}
     *
     */
    private void cargarTextosTarea(Tarea tarea) {
        tvTitulo.setText(tarea.getTitulo().trim().isEmpty() ? "No tiene título" : tarea.getTitulo());
        tvDescripcion.setText(tarea.getDescripcion().trim().isEmpty() ? "No hay ninguna descripción" : tarea.getDescripcion());
        tvDocumento.setText(tarea.getUrlDoc().trim().isEmpty() ? "No tiene ningún documento adjunto" : tarea.getUrlDoc());
        tvPhoto.setText(tarea.getUrlPho().trim().isEmpty() ? "No tiene ninguna foto adjunta" : tarea.getUrlPho());
        tvAudio.setText(tarea.getUrlAud().trim().isEmpty() ? "No tiene ningún audio adjunto" : tarea.getUrlAud());
        tvVideo.setText(tarea.getUrlVid().trim().isEmpty() ? "No tiene ningún video adjunto" : tarea.getUrlVid());
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