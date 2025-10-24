package com.proyecto.reciclatech.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Pregunta;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class CuestionarioService {

    private final MongoDatabase database;

    public CuestionarioService() {
        this.database = MongoConnection.getDatabase("ReciclaTech");
    }

    // Obtener preguntas aleatorias
    public List<Pregunta> obtenerPreguntasAleatorias(int limite) {
        MongoCollection<Document> preguntasCollection = database.getCollection("preguntas");
        List<Pregunta> preguntas = new ArrayList<>();

        for (Document d : preguntasCollection.aggregate(List.of(new Document("$sample", new Document("size", limite))))) {
            preguntas.add(new Pregunta(
                    d.getString("texto"),
                    (List<String>) d.get("opciones"),
                    d.getString("respuestaCorrecta"),
                    d.getInteger("dificultad")
            ));
        }

        return preguntas;
    }

    // Guardar puntaje por carrera
    public void guardarPuntajeCarrera(String carrera, int puntaje) {
        MongoCollection<Document> coll = database.getCollection("puntajes");

        Document existing = coll.find(eq("carrera", carrera)).first();
        if (existing != null) {
            int total = existing.getInteger("puntajeTotal", 0) + puntaje;
            coll.updateOne(eq("carrera", carrera), new Document("$set", new Document("puntajeTotal", total)));
        } else {
            coll.insertOne(new Document("carrera", carrera).append("puntajeTotal", puntaje));
        }
    }
}
