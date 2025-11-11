package com.proyecto.reciclatech.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Historial;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class HistorialService {
    private final MongoCollection<Document> collection;

    public HistorialService() {
        MongoDatabase db = MongoConnection.getDatabase("ReciclaTech");
        this.collection = db.getCollection("historial");
    }

    public void guardar(Historial historial) {
        Document doc = new Document("carnet", historial.getCarnet())
                .append("nombreResiduo", historial.getNombreResiduo())
                .append("categoria", historial.getCategoria())
                .append("fecha", historial.getFecha());
        collection.insertOne(doc);
    }

    public List<Historial> obtenerPorUsuario(String carnet) {
        List<Historial> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(eq("carnet", carnet)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Historial h = new Historial(
                        doc.getString("carnet"),
                        doc.getString("nombreResiduo"),
                        doc.getString("categoria")
                );
                h.setFecha(doc.getString("fecha"));
                lista.add(h);
            }
        }
        return lista;
    }

    public List<Historial> obtenerTodos() {
        List<Historial> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Historial h = new Historial(
                        doc.getString("carnet"),
                        doc.getString("nombreResiduo"),
                        doc.getString("categoria")
                );
                h.setFecha(doc.getString("fecha"));
                lista.add(h);
            }
        }
        return lista;
    }
}
