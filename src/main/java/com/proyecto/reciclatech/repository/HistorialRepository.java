package com.proyecto.reciclatech.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Historial;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class HistorialRepository {

    private final MongoDatabase database;

    public HistorialRepository() {
        this.database = MongoConnection.getDatabase("ReciclaTech");
    }

    public void guardar(Historial historial) {
        MongoCollection<Document> collection = database.getCollection("historial");

        Document doc = new Document("carnet", historial.getCarnet())
                .append("nombreResiduo", historial.getNombreResiduo())
                .append("categoria", historial.getCategoria())
                .append("fecha", historial.getFecha());

        collection.insertOne(doc);
    }

    public List<Historial> obtenerPorCarnet(String carnet) {
        MongoCollection<Document> collection = database.getCollection("historial");
        List<Historial> lista = new ArrayList<>();

        for (Document doc : collection.find(eq("carnet", carnet))) {
            Historial h = new Historial();
            h.setId(doc.getObjectId("_id").toHexString());
            h.setCarnet(doc.getString("carnet"));
            h.setNombreResiduo(doc.getString("nombreResiduo"));
            h.setCategoria(doc.getString("categoria"));
            h.setFecha(doc.getString("fecha"));
            lista.add(h);
        }
        return lista;
    }

    public List<Historial> obtenerTodos() {
        MongoCollection<Document> collection = database.getCollection("historial");
        List<Historial> lista = new ArrayList<>();

        for (Document doc : collection.find()) {
            Historial h = new Historial();
            h.setId(doc.getObjectId("_id").toHexString());
            h.setCarnet(doc.getString("carnet"));
            h.setNombreResiduo(doc.getString("nombreResiduo"));
            h.setCategoria(doc.getString("categoria"));
            h.setFecha(doc.getString("fecha"));
            lista.add(h);
        }
        return lista;
    }
}
