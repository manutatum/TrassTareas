package com.example.proyectomanueljimenez.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class TareaViewModel extends ViewModel {

    // ATRIBUTOS DE UNA TAREA
    private final MutableLiveData<String> tituloTarea = new MutableLiveData<>();
    private final MutableLiveData<String> descripcionTarea = new MutableLiveData<>();
    private final MutableLiveData<Integer> progresoTarea = new MutableLiveData<>();
    private final MutableLiveData<Date> fechaCreacionTarea = new MutableLiveData<>();
    private final MutableLiveData<Date> fechaFinTarea = new MutableLiveData<>();
    private final MutableLiveData<Boolean> prioridadTarea = new MutableLiveData<>();
    private final MutableLiveData<String> urlDoc = new MutableLiveData<>();
    private final MutableLiveData<String> urlAud = new MutableLiveData<>();
    private final MutableLiveData<String> urlPho = new MutableLiveData<>();
    private final MutableLiveData<String> urlVid = new MutableLiveData<>();

    // GETTERS
    public MutableLiveData<String> getTituloTarea() {
        return tituloTarea;
    }

    public MutableLiveData<String> getDescripcionTarea() {
        return descripcionTarea;
    }

    public MutableLiveData<Integer> getProgresoTarea() {
        return progresoTarea;
    }

    public MutableLiveData<Date> getFechaCreacionTarea() {
        return fechaCreacionTarea;
    }

    public MutableLiveData<Date> getFechaFinTarea() {
        return fechaFinTarea;
    }

    public MutableLiveData<Boolean> getPrioridadTarea() {
        return prioridadTarea;
    }

    public MutableLiveData<String> getUrlVid() {
        return urlVid;
    }

    public MutableLiveData<String> getUrlPho() {
        return urlPho;
    }

    public MutableLiveData<String> getUrlAud() {
        return urlAud;
    }

    public MutableLiveData<String> getUrlDoc() {
        return urlDoc;
    }

    // SETTERS DEL MUTABLELIVEDATA
    public void setTituloTarea(String tituloTarea){this.tituloTarea.setValue(tituloTarea);}

    public void setDescripcionTarea(String descripcionTarea){this.descripcionTarea.setValue(descripcionTarea);}

    public void setProgresoTarea(Integer progresoTarea){this.progresoTarea.setValue(progresoTarea);}

    public void setFechaCreacionTarea(Date fechaCreacionTarea){this.fechaCreacionTarea.setValue(fechaCreacionTarea);}

    public void setFechaFinTarea(Date fechaFinTarea){this.fechaFinTarea.setValue(fechaFinTarea);}

    public void setPrioridadTarea(Boolean prioridadTarea){this.prioridadTarea.setValue(prioridadTarea);}

    public void setUrlDoc(String urlDoc){this.urlDoc.setValue(urlDoc);}

    public void setUrlAud(String urlAud){this.urlAud.setValue(urlAud);}

    public void setUrlPho(String urlPho){this.urlPho.setValue(urlPho);}

    public void setUrlVid(String urlVid){this.urlVid.setValue(urlVid);}
}