package com.proyecto.reciclatech.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class PuntosService {

    private final MongoDatabase database;

    public PuntosService() {
        this.database = MongoConnection.getDatabase("ReciclaTech");
    }

    public int obtenerPuntos(String carnet) {
        MongoCollection<Document> collection = database.getCollection("puntos");
        Document doc = collection.find(eq("carnet", carnet)).first();
        return (doc != null) ? doc.getInteger("puntos", 0) : 0;
    }

    public void sumarPuntos(String carnet, int puntosGanados) {
        MongoCollection<Document> collection = database.getCollection("puntos");
        Document doc = collection.find(eq("carnet", carnet)).first();

        if (doc == null) {
            collection.insertOne(new Document("carnet", carnet).append("puntos", puntosGanados));
        } else {
            int puntosActuales = doc.getInteger("puntos", 0) + puntosGanados;
            collection.updateOne(eq("carnet", carnet),
                    new Document("$set", new Document("puntos", puntosActuales)));
        }
    }
}
