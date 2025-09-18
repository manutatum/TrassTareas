package com.example.proyectomanueljimenez.clases;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Tarea implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "progreso")
    private int progreso;

    @ColumnInfo(name = "fechaCreacion")
    private Date fechaCreacion;

    @ColumnInfo(name = "fechaFin")
    private Date fechaFin;

    @ColumnInfo(name = "prioritaria", defaultValue = "false")
    private boolean prioritaria;

    @ColumnInfo(name = "url_doc")
    private String urlDoc;

    @ColumnInfo(name = "url_pho")
    private String urlPho;

    @ColumnInfo(name = "url_aud")
    private String urlAud;

    @ColumnInfo(name = "url_vid")
    private String urlVid;

    public Tarea() {
    }

    public Tarea(String titulo, String descripcion, int progreso, Date fechaCreacion, Date fechaFin, boolean prioritaria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaFin = fechaFin;
        this.fechaCreacion = fechaCreacion;
        this.prioritaria = prioritaria;
    }

    public Tarea(String titulo, String descripcion, int progreso, Date fechaCreacion, Date fechaFin, boolean prioritaria, String urlDoc, String urlPho, String urlAud, String urlVid) {
        this.fechaFin = fechaFin;
        this.prioritaria = prioritaria;
        this.urlDoc = urlDoc;
        this.urlPho = urlPho;
        this.urlAud = urlAud;
        this.urlVid = urlVid;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaCreacion = fechaCreacion;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getProgreso() {
        return progreso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public String getUrlDoc() {
        return urlDoc;
    }

    public void setUrlDoc(String urlDoc) {
        this.urlDoc = urlDoc;
    }

    public String getUrlVid() {
        return urlVid;
    }

    public void setUrlVid(String urlVid) {
        this.urlVid = urlVid;
    }

    public String getUrlAud() {
        return urlAud;
    }

    public void setUrlAud(String urlAud) {
        this.urlAud = urlAud;
    }

    public String getUrlPho() {
        return urlPho;
    }

    public void setUrlPho(String urlPho) {
        this.urlPho = urlPho;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
