package com.proyecto.reciclatech.model;

import org.bson.types.ObjectId;
import org.bson.codecs.pojo.annotations.BsonId;

public class Puntaje {
    @BsonId
    private ObjectId id;
    private String carrera;
    private int puntajeTotal;

    public Puntaje() {}

    public Puntaje(String carrera, int puntajeTotal) {
        this.carrera = carrera;
        this.puntajeTotal = puntajeTotal;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public int getPuntajeTotal() { return puntajeTotal; }
    public void setPuntajeTotal(int puntajeTotal) { this.puntajeTotal = puntajeTotal; }
}
