package com.proyecto.reciclatech.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Puntaje;
import com.proyecto.reciclatech.model.Usuario;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.List;

public class PuntajeService {

    private final MongoDatabase db = MongoConnection.getDatabase("ReciclaTech");
    private final MongoCollection<Document> coleccionPuntajes = db.getCollection("puntajes");

    public void agregarPuntaje(Usuario usuario, int puntos) {
        String carrera = usuario.getCarrera();
        if (carrera == null || carrera.isBlank()) return;

        Document carreraDoc = coleccionPuntajes.find(Filters.eq("carrera", carrera)).first();

        if (carreraDoc == null) {
            Document nuevo = new Document("carrera", carrera)
                    .append("puntajeTotal", puntos);
            coleccionPuntajes.insertOne(nuevo);
        } else {
            int total = carreraDoc.getInteger("puntajeTotal", 0) + puntos;
            coleccionPuntajes.updateOne(Filters.eq("carrera", carrera),
                    new Document("$set", new Document("puntajeTotal", total)));
        }
    }

    public List<Puntaje> obtenerRanking() {
        List<Puntaje> ranking = new ArrayList<>();
        coleccionPuntajes.find()
                .sort(new Document("puntajeTotal", -1))
                .forEach(doc -> {
                    Puntaje p = new Puntaje();
                    p.setCarrera(doc.getString("carrera"));
                    p.setPuntajeTotal(doc.getInteger("puntajeTotal", 0));
                    ranking.add(p);
                });
        return ranking;
    }
}
