package com.proyecto.reciclatech.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Basura;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class BasuraRepository {

    private final MongoDatabase database;

    public BasuraRepository() {
        this.database = MongoConnection.getDatabase("ReciclaTech");
    }

    public Basura buscarPorNombre(String nombre) {
        MongoCollection<Document> collection = database.getCollection("basura");

        // Buscar nombre exactamente en minúsculas
        Document doc = collection.find(eq("nombre", nombre.toLowerCase().trim())).first();

        if (doc != null) {
            return new Basura(
                    doc.getString("nombre"),
                    doc.getString("categoria")
            );
        }
        return null;
    }

    public void guardar(Basura basura) {
        MongoCollection<Document> collection = database.getCollection("basura");
        Document doc = new Document("nombre", basura.getNombre())
                .append("categoria", basura.getCategoria());
        collection.insertOne(doc);
    }
}
