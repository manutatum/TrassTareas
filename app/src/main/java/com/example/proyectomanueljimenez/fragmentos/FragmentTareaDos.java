package com.example.proyectomanueljimenez.fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.proyectomanueljimenez.R;
import com.example.proyectomanueljimenez.interfaces.ComunicacionFragmentoTareaDos;
import com.example.proyectomanueljimenez.utils.FicherosUtils;
import com.example.proyectomanueljimenez.viewmodels.TareaViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FragmentTareaDos extends Fragment {

    EditText descripcion;
    Button btnVolver, btnGuardar, btnBorrar;
    ImageButton btnDocumento, btnImagen, btnAudio, btnVideo;

    // FICHEROS
    ActivityResultLauncher<Intent> archivoLauncher;
    SharedPreferences preferences;

    Uri imagenUri;
    File archivoTemporalFoto;

    // CREAR VIEWMODEL
    private TareaViewModel tareaViewModel;

    // COMUNICADOR
    private ComunicacionFragmentoTareaDos comunicador;

    // LAUNCHER DE PERMISOS
    private final ActivityResultLauncher<String> lanzadorPermisos =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean isGranted) {
                            if (isGranted) {
                                // Permiso concedido, podemos lanzar la cámara.
                                lanzarCamara();
                            } else {
                                // Permiso denegado.
                                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    //Metodo para comprobar si se ha concedido el permiso
    private boolean comprobarPermisoCamara() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    //Metodo para solicitar el permiso al usuario
    private void pedirPermisoCamara() {
        lanzadorPermisos.launch(Manifest.permission.CAMERA);
    }

    public FragmentTareaDos() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicacionFragmentoTareaDos) {
            comunicador = (ComunicacionFragmentoTareaDos) context;
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            throw new ClassCastException(context + " debe implementar ComunicacionFragmentoTareaDos");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ASIGNAR EL VIEW MODEL
        tareaViewModel = new ViewModelProvider(requireActivity()).get(TareaViewModel.class);

        archivoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intentDevuelto = result.getData();

                    if (intentDevuelto != null) {
                        Uri fileUri = intentDevuelto.getData();

                        if (fileUri != null) {
                            // Archivo seleccionado desde galería
                            String mimeType = requireContext().getContentResolver().getType(fileUri);
                            if (mimeType != null && mimeType.startsWith("image/")) archivoTemporalFoto.delete();

                            if (tareaViewModel.getTituloTarea().getValue() != null) eliminarArchivoSiExiste(fileUri);

                            guardarArchivo(fileUri);
                        } else {
                            // Foto tomada con la cámara
                            if (archivoTemporalFoto != null && archivoTemporalFoto.exists()) {
                                if (tareaViewModel.getTituloTarea().getValue() != null) eliminarArchivoSiExiste(imagenUri);
                                actualizarViewModelUrls(archivoTemporalFoto.getAbsolutePath(), imagenUri);
                            }
                        }
                    }
                }
        );
    }

    private void eliminarArchivoSiExiste(Uri uri) {

        String mimeType = requireContext().getContentResolver().getType(uri);

        if (mimeType != null) {

            if (mimeType.startsWith("video/") && tareaViewModel.getUrlVid().getValue() != null){
                FicherosUtils.eliminarArchivo(requireContext(), tareaViewModel.getUrlVid().getValue());
                tareaViewModel.setUrlVid("");
            }

            if (mimeType.startsWith("application/") && tareaViewModel.getUrlDoc().getValue() != null){
                FicherosUtils.eliminarArchivo(requireContext(), tareaViewModel.getUrlDoc().getValue());
                tareaViewModel.setUrlDoc("");
            }

            if (mimeType.startsWith("audio/") && tareaViewModel.getUrlAud().getValue() != null){
                FicherosUtils.eliminarArchivo(requireContext(), tareaViewModel.getUrlAud().getValue());
                tareaViewModel.setUrlAud("");
            }

            if (mimeType.startsWith("image/") && tareaViewModel.getUrlPho().getValue() != null){
                FicherosUtils.eliminarArchivo(requireContext(), tareaViewModel.getUrlPho().getValue());
                tareaViewModel.setUrlPho("");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_tarea_dos, container, false);

        descripcion = fragment.findViewById(R.id.etDescripcion);

        // RESTAURAMOS EL VALOR QUE HAYA EN EL VIEW MODEL
        descripcion.setText(tareaViewModel.getDescripcionTarea().getValue());

        descripcion.setOnKeyListener((v, keyCode, event) -> {
            // GUARDAMOS EL VALOR EN EL VIEWMODEL
            tareaViewModel.setDescripcionTarea(descripcion.getText().toString());
            return false;
        });

        btnGuardar = fragment.findViewById(R.id.btnGuardar);
        btnVolver = fragment.findViewById(R.id.btnVolver);
        btnBorrar = fragment.findViewById(R.id.btnBorrar);

        btnDocumento = fragment.findViewById(R.id.btnDocumento);
        btnImagen = fragment.findViewById(R.id.btnImagen);
        btnAudio = fragment.findViewById(R.id.btnAudio);
        btnVideo = fragment.findViewById(R.id.btnVideo);

        // Intent para documentos
        btnDocumento.setOnClickListener(v -> {

            if (tareaViewModel.getTituloTarea().getValue() == null || tareaViewModel.getTituloTarea().getValue().isEmpty()){
                Toast.makeText(requireContext(), "Primero debe ponerle un titulo a la tarea", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                archivoLauncher.launch(intent);
            }
        });

        // Intent para imágenes
        btnImagen.setOnClickListener(v -> {
            if (tareaViewModel.getTituloTarea().getValue() == null || tareaViewModel.getTituloTarea().getValue().isEmpty()){
                Toast.makeText(requireContext(), "Primero debe ponerle un titulo a la tarea", Toast.LENGTH_SHORT).show();
            } else {
                if(comprobarPermisoCamara()) lanzarCamara();
                else pedirPermisoCamara();
            }
        });

        // Intent para audios
        btnAudio.setOnClickListener(v -> {

            if (tareaViewModel.getTituloTarea().getValue() == null || tareaViewModel.getTituloTarea().getValue().isEmpty()){
                Toast.makeText(requireContext(), "Primero debe ponerle un titulo a la tarea", Toast.LENGTH_SHORT).show();
            } else {
                Intent intentGrabaciones = new Intent(Intent.ACTION_GET_CONTENT);
                intentGrabaciones.setType("audio/*");

                // APLICACION GRABADORA
                Intent intentGrabadora = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                chooser.putExtra(Intent.EXTRA_TITLE, "Grabaciones");
                chooser.putExtra(Intent.EXTRA_INTENT, intentGrabaciones);

                Intent[] intentarray= {intentGrabadora};
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentarray);
                archivoLauncher.launch(chooser);
            }
        });

        // Intent para videos
        btnVideo.setOnClickListener(v -> {
            if (tareaViewModel.getTituloTarea().getValue() == null || tareaViewModel.getTituloTarea().getValue().isEmpty()){
                Toast.makeText(requireContext(), "Primero debe ponerle un titulo a la tarea", Toast.LENGTH_SHORT).show();
            } else {
                if (comprobarPermisoCamara()){
                    lanzarCamaraVideo();
                }else {
                    pedirPermisoCamara();
                }
            }

        });

        btnVolver.setOnClickListener(v -> comunicador.onClickBotonVolver());
        btnGuardar.setOnClickListener(v -> comunicador.onClickBotonAceptar());
        btnBorrar.setOnClickListener(v -> comunicador.onClickBotonBorrar());

        return fragment;
    }

    private void lanzarCamaraVideo() {

        Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
        intentGaleria.setAction(Intent.ACTION_GET_CONTENT);
        intentGaleria.setType("video/*");

        // CREAMOS EL INTENT PARA ABRIR LA CAMARA DE VIDEO
        Intent intentCamaraVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // CREAMOS EL CHOOSER
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_TITLE, "Fotos");
        chooser.putExtra(Intent.EXTRA_INTENT, intentGaleria);

        Intent[] intentarray= {intentCamaraVideo};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentarray);

        archivoLauncher.launch(chooser);
    }

    private void lanzarCamara() {

        // BORRAMOS SI YA HAY
        if (archivoTemporalFoto != null && archivoTemporalFoto.exists()) archivoTemporalFoto.delete();

        // CREAMOS EL INTENT QUE ABRIRA LA GALERIA
        Intent intentGaleria = new Intent();
        intentGaleria.setType("image/*");
        intentGaleria.setAction(Intent.ACTION_GET_CONTENT);

        // CREAMOS EL INTENT PARA ABRIR LA CAMARA
        Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {

            archivoTemporalFoto = crearArchivoTemporal();
            imagenUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.proyectomanueljimenez.fileprovider", // FileProvider declarado en el manifest
                    archivoTemporalFoto);
            intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);

            // CREAMOS EL CHOOSER
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_TITLE, "Fotos");
            chooser.putExtra(Intent.EXTRA_INTENT, intentGaleria);

            Intent[] intentarray= {intentCamara};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentarray);

            archivoLauncher.launch(chooser);

        }catch (IOException ex){
            Toast.makeText(requireContext(), "Error al crear el archivo temporal", Toast.LENGTH_LONG).show();
            //Registramos la excepción en los logs para poder investigarla después
            Log.e("Error", Objects.requireNonNull(ex.getMessage()));
        }
    }

    private File crearArchivoTemporal() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String nombreArchivoImagen = "JPEG_" + timeStamp + "_";

        File rutaFoto = getStorageDirectory();

        return File.createTempFile(
                nombreArchivoImagen,    // prefijo
                ".jpg",                 // sufijo
                rutaFoto                // directorio
        );
    }

    // Método para guardar el archivo
    private void guardarArchivo(Uri uri) {

        // Obtenemos el directorio (Interno o SD)
        File storageDir = getStorageDirectory();

        // Obtenemos el nombre del archivo
        String fileName = getFileName(uri);

        // Creamos el archivo de destino en el directorio adecuado
        File destFile = new File(storageDir, fileName);

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

             // Creamos un outputstream para escribir el archivo en el sitio correcto
             OutputStream outputStream = new FileOutputStream(destFile)) {

            // Copiamos el contenido del archivo
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Si no es nulo, que en este caso con el requireContext no deberia
            // llegar aqui si es nulo, pero igualmente comprobamos que no lo sea
            if (inputStream != null){
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                actualizarViewModelUrls(destFile.getAbsolutePath(), uri);

                // Si sale todo bien
                Toast.makeText(requireContext(), "Archivo guardado en: " + destFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }else{
                // En caso de que sea nulo
                Toast.makeText(requireContext(), "Error al guardar el archivo", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            // Cualquier excepcion que salte
            Toast.makeText(requireContext(), "Error al guardar el archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarViewModelUrls(String absolutePath, Uri uri) {

        String mimeType = requireContext().getContentResolver().getType(uri);

        if (mimeType != null) {
            if (mimeType.startsWith("image/")) {
                tareaViewModel.setUrlPho(absolutePath);
            } else if (mimeType.startsWith("audio/")) {
                tareaViewModel.setUrlAud(absolutePath);
            } else if (mimeType.startsWith("video/")) {
                tareaViewModel.setUrlVid(absolutePath);
            } else {
                tareaViewModel.setUrlDoc(absolutePath);
            }
        } else {
            // Si no se detecta MIME, se considera documento por defecto
            tareaViewModel.setUrlDoc(absolutePath);
        }
    }

    private String getFileName(Uri uri) {

        String fileName = "nombre_predeterminado";

        try (Cursor cursor = requireContext().getContentResolver().query(
                uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(0);
            }
        }

        // Si el archivo no tiene nombre
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
            if (fileName == null || fileName.isEmpty()) {
                fileName = "archivo_" + System.currentTimeMillis();
            }
        }

        return fileName;
    }

    // Método para obtener el directorio adecuado
    private File getStorageDirectory() {

        String rutaDirectorios = "archivos_adjuntos/" + tareaViewModel.getTituloTarea().getValue();
        File storageDir = null;

        if (getPreferenceSdCard() && isExternalStorageCorrecto()) {

            // Usar la tarjeta SD
            storageDir = new File(requireContext().getExternalFilesDir(null), rutaDirectorios );

        } else {
            // Usar almacenamiento interno

            // USAMOS REQUIRECONTEXT YA QUE ESTE USA EL GET CONTEXT PERO
            // YA TIENE IMPLEMENTADA UNA EXCEPCION EN CASO DE SER NULL
            storageDir = new File(requireContext().getFilesDir(), rutaDirectorios);
        }

        // Si el directorio no existe, lo creamos
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.e("FileStorage", "Error al crear el directorio");
            }
        }

        return storageDir;
    }

    // Método para obtener la preferencia de SD o Interno
    private boolean getPreferenceSdCard() {
        return preferences.getBoolean("preferencia_sd", false);
    }

    // Métodos para verificar el estado de la memoria externa
    private boolean isExternalStorageCorrecto() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}