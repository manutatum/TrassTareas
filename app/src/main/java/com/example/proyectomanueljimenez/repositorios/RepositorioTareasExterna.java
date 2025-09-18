package com.example.proyectomanueljimenez.repositorios;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectomanueljimenez.clases.Tarea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RepositorioTareasExterna extends RepositorioTareas{

    // DATOS BD
    private String ip;
    private String puerto;
    private String nombreBD;
    private String usuario;
    private String password;

    // BD
    private Connection conexion;
    private Statement statement;
    private ResultSet resultSet;

    // THREAD
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public RepositorioTareasExterna(String ip, String nombreBD, String puerto, String usuario, String password) {
        this.ip = ip;
        this.puerto = puerto;
        this.nombreBD = nombreBD;
        this.usuario = usuario;
        this.password = password;
    }

    private Connection crearConexion() {
        String url = "jdbc:mysql://" + ip + ":" + puerto + "/" + nombreBD +
                "?useSSL=false&connectTimeout=3000&socketTimeout=3000";
        Log.i("DB", "Intentando conectar con: " + url);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, usuario, password);
            Log.i("DB", "Conexión exitosa");
            return conn;
        } catch (ClassNotFoundException e) {
            Log.e("DB_ERROR", "Driver no encontrado", e);
            return null;
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error al conectar", e);
            return null;
        }
    }

    private void cerrarConexion(Connection conexion, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            Log.e("ERROR_CERRANDO", "Error al cerrar la conexión", e);
        }
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasAlfabeticasAscendente() {
        final MutableLiveData<List<Tarea>> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Tarea> listaTareas = new ArrayList<>();

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(new ArrayList<>());
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Tarea ORDER BY titulo ASC");

                while (resultSet.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));

                    listaTareas.add(tarea);
                }

                tareasLive.postValue(listaTareas);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasAlfabeticasDescendente() {
        final MutableLiveData<List<Tarea>> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Tarea> listaTareas = new ArrayList<>();

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(new ArrayList<>());
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Tarea ORDER BY titulo DESC");

                while (resultSet.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));

                    listaTareas.add(tarea);
                }

                tareasLive.postValue(listaTareas);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorFechaCreacionAscendente() {
        final MutableLiveData<List<Tarea>> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Tarea> listaTareas = new ArrayList<>();

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(new ArrayList<>());
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Tarea ORDER BY fechaCreacion ASC");

                while (resultSet.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));

                    listaTareas.add(tarea);
                }

                tareasLive.postValue(listaTareas);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorFechaCreacionDescendente() {
        final MutableLiveData<List<Tarea>> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Tarea> listaTareas = new ArrayList<>();

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(new ArrayList<>());
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Tarea ORDER BY fechaCreacion DESC");

                while (resultSet.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));

                    listaTareas.add(tarea);
                }

                tareasLive.postValue(listaTareas);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorProgresoAscendente() {
        final MutableLiveData<List<Tarea>> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Tarea> listaTareas = new ArrayList<>();

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(new ArrayList<>());
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Tarea ORDER BY progreso ASC");

                while (resultSet.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));

                    listaTareas.add(tarea);
                }

                tareasLive.postValue(listaTareas);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<List<Tarea>> obtenerTareasPorProgresoDescendente() {
        final MutableLiveData<List<Tarea>> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Tarea> listaTareas = new ArrayList<>();

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(new ArrayList<>());
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Tarea ORDER BY progreso DESC");

                while (resultSet.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));

                    listaTareas.add(tarea);
                }

                tareasLive.postValue(listaTareas);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<Integer> getTareasPrioritarias() {
        final MutableLiveData<Integer> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            Integer resultado = 0;

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(0);
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT COUNT(titulo) as cuenta FROM Tarea WHERE prioritaria = 1");

                if (resultSet.next()) {
                    resultado = resultSet.getInt("cuenta");
                }

                tareasLive.postValue(resultado);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<Integer> getTareasCompletadas() {
        final MutableLiveData<Integer> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            Integer resultado = 0;

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(0);
                }

                statement = conexion.createStatement();
                resultSet = statement.executeQuery("SELECT COUNT(titulo) as cuenta FROM Tarea WHERE progreso = 100");

                if (resultSet.next()) {
                    resultado = resultSet.getInt("cuenta");
                }

                tareasLive.postValue(resultado);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<Integer> getTareasAtrasadas(Date fechaActual) {
        final MutableLiveData<Integer> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            Integer resultado = 0;

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(0);
                }

                String query = "SELECT COUNT(_id) AS cuenta FROM Tarea " +
                        "WHERE fechaFin < ? AND progreso < 100";
                PreparedStatement preparedStatement = conexion.prepareStatement(query);


                // Convertir la fecha actual a formato SQL
                java.sql.Date fechaActualSQL = new java.sql.Date(fechaActual.getTime());
                preparedStatement.setDate(1, fechaActualSQL);

                // Ejecutar la consulta
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    resultado = resultSet.getInt("cuenta");
                }

                tareasLive.postValue(resultado);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public LiveData<Tarea> getTareaMasCercana(Date fechaActual) {
        final MutableLiveData<Tarea> tareasLive = new MutableLiveData<>();

        executor.submit(() -> {
            Connection conexion = null;
            Statement statement = null;
            ResultSet resultSet = null;
            Tarea tarea = null;

            try {
                conexion = crearConexion();

                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    tareasLive.postValue(tarea);
                }

                String query = "SELECT * FROM Tarea " +
                        "WHERE fechaFin >= ? AND progreso < 100 " + // Tareas no completadas y con fecha futura
                        "ORDER BY fechaFin ASC " + // Ordenar por fecha más cercana
                        "LIMIT 1"; // Obtener solo la primera tarea
                PreparedStatement preparedStatement = conexion.prepareStatement(query);


                // Convertir la fecha actual a formato SQL
                java.sql.Date fechaActualSQL = new java.sql.Date(fechaActual.getTime());
                preparedStatement.setDate(1, fechaActualSQL);

                // Ejecutar la consulta
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    tarea = new Tarea();
                    tarea.setId(resultSet.getInt("_id"));
                    tarea.setTitulo(resultSet.getString("titulo"));
                    tarea.setDescripcion(resultSet.getString("descripcion"));
                    tarea.setProgreso(resultSet.getInt("progreso"));
                    tarea.setFechaCreacion(resultSet.getDate("fechaCreacion"));
                    tarea.setFechaFin(resultSet.getDate("fechaFin"));
                    tarea.setPrioritaria(resultSet.getBoolean("prioritaria"));
                    tarea.setUrlDoc(resultSet.getString("url_doc"));
                    tarea.setUrlVid(resultSet.getString("url_vid"));
                    tarea.setUrlAud(resultSet.getString("url_aud"));
                    tarea.setUrlPho(resultSet.getString("url_pho"));
                }

                tareasLive.postValue(tarea);

            } catch (SQLException e) {
                Log.e("ERROR", "Error en la consulta", e);
            } finally {
                cerrarConexion(conexion, statement, resultSet);
            }
        });

        return tareasLive;
    }

    @Override
    public void insertTareas(Tarea... tareas) {
        executor.submit(() -> {
            Connection conexion = null;
            PreparedStatement preparedStatement = null;

            try {
                // Crear la conexión
                conexion = crearConexion();
                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    return; // Salir del método si no hay conexión
                }

                // Consulta SQL para insertar una tarea
                String query = "INSERT INTO Tarea (titulo, descripcion, progreso, fechaCreacion, fechaFin, prioritaria, url_doc, url_vid, url_aud, url_pho) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = conexion.prepareStatement(query);

                // Insertar cada tarea
                for (Tarea tarea : tareas) {
                    preparedStatement.setString(1, tarea.getTitulo());
                    preparedStatement.setString(2, tarea.getDescripcion());
                    preparedStatement.setInt(3, tarea.getProgreso());
                    preparedStatement.setDate(4, new java.sql.Date(tarea.getFechaCreacion().getTime()));
                    preparedStatement.setDate(5, new java.sql.Date(tarea.getFechaFin().getTime()));
                    preparedStatement.setBoolean(6, tarea.isPrioritaria());
                    preparedStatement.setString(7, tarea.getUrlDoc());
                    preparedStatement.setString(8, tarea.getUrlVid());
                    preparedStatement.setString(9, tarea.getUrlAud());
                    preparedStatement.setString(10, tarea.getUrlPho());

                    // Ejecutar la inserción
                    preparedStatement.executeUpdate();
                }

                Log.d("INSERT_TAREAS", "Tareas insertadas correctamente");

            } catch (SQLException e) {
                Log.e("ERROR", "Error al insertar tareas", e);
            } finally {
                // Cerrar la conexión y liberar recursos
                cerrarConexion(conexion, preparedStatement, null);
            }
        });
    }

    @Override
    public void deleteTarea(Tarea tarea) {
        executor.submit(() -> {
            Connection conexion = null;
            PreparedStatement preparedStatement = null;

            try {
                // Crear la conexión
                conexion = crearConexion();
                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    return; // Salir del método si no hay conexión
                }

                // Consulta SQL para eliminar una tarea
                String query = "DELETE FROM Tarea WHERE _id = ?";
                preparedStatement = conexion.prepareStatement(query);

                // Asignar el ID de la tarea a eliminar
                preparedStatement.setInt(1, tarea.getId());

                // Ejecutar la eliminación
                int filasEliminadas = preparedStatement.executeUpdate();

                if (filasEliminadas > 0) {
                    Log.d("DELETE_TAREA", "Tarea eliminada correctamente");
                } else {
                    Log.e("DELETE_TAREA", "No se encontró la tarea con ID: " + tarea.getId());
                }

            } catch (SQLException e) {
                Log.e("ERROR", "Error al eliminar la tarea", e);
            } finally {
                // Cerrar la conexión y liberar recursos
                cerrarConexion(conexion, preparedStatement, null);
            }
        });
    }

    @Override
    public void updateTarea(Tarea tarea) {
        executor.submit(() -> {
            Connection conexion = null;
            PreparedStatement preparedStatement = null;

            try {
                // Crear la conexión
                conexion = crearConexion();
                if (conexion == null) {
                    Log.e("DB", "CONEXIÓN NULL");
                    return; // Salir del método si no hay conexión
                }

                // Crear la consulta SQL para actualizar la tarea
                String query = "UPDATE Tarea SET " +
                        "titulo = ?, " +
                        "descripcion = ?, " +
                        "progreso = ?, " +
                        "fechaCreacion = ?, " +
                        "fechaFin = ?, " +
                        "prioritaria = ?, " +
                        "url_doc = ?, " +
                        "url_vid = ?, " +
                        "url_aud = ?, " +
                        "url_pho = ? " +
                        "WHERE _id = ?"; // Actualizar la tarea con el ID correspondiente

                preparedStatement = conexion.prepareStatement(query);

                // Asignar los valores de la tarea a la consulta
                preparedStatement.setString(1, tarea.getTitulo());
                preparedStatement.setString(2, tarea.getDescripcion());
                preparedStatement.setInt(3, tarea.getProgreso());
                preparedStatement.setDate(4, new java.sql.Date(tarea.getFechaCreacion().getTime()));
                preparedStatement.setDate(5, new java.sql.Date(tarea.getFechaFin().getTime()));
                preparedStatement.setBoolean(6, tarea.isPrioritaria());
                preparedStatement.setString(7, tarea.getUrlDoc());
                preparedStatement.setString(8, tarea.getUrlVid());
                preparedStatement.setString(9, tarea.getUrlAud());
                preparedStatement.setString(10, tarea.getUrlPho());
                preparedStatement.setInt(11, tarea.getId());

                // Ejecutar la consulta
                int filasActualizadas = preparedStatement.executeUpdate();

                if (filasActualizadas > 0) {
                    Log.d("UPDATE_TAREA", "Tarea actualizada correctamente");
                } else {
                    Log.e("UPDATE_TAREA", "No se encontró la tarea con ID: " + tarea.getId());
                }

            } catch (SQLException e) {
                Log.e("ERROR", "Error al actualizar la tarea", e);
            } finally {
                // Cerrar la conexión y liberar recursos
                cerrarConexion(conexion, preparedStatement, null);
            }
        });
    }
}
