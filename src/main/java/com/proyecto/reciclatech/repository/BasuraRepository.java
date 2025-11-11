package com.proyecto.reciclatech.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Basura;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class BasuraRepository {

    private final MongoDatabase database;

    public BasuraRepository() {
        this.database = MongoConnection.getDatabase("ReciclaTech");
    }

    public Basura buscarPorNombre(String nombre) {
        MongoCollection<Document> collection = database.getCollection("basura");

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
        System.out.println("✅ Guardado en MongoDB: " + doc.toJson());
    }

    /**
     * 📊 Retorna el historial de residuos de un usuario por su carnet.
     */
    public List<Basura> obtenerHistorialPorUsuario(String carnet) {
        MongoCollection<Document> collection = database.getCollection("historial");

        List<Basura> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(eq("carnet", carnet)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                lista.add(new Basura(
                        doc.getString("nombreResiduo"),
                        doc.getString("categoria")
                ));
            }
        }
        return lista;
    }

    /**
     * 🌍 Retorna todos los residuos registrados de todos los usuarios.
     */
    public List<Basura> obtenerTodos() {
        MongoCollection<Document> collection = database.getCollection("historial");

        List<Basura> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                lista.add(new Basura(
                        doc.getString("nombreResiduo"),
                        doc.getString("categoria")
                ));
            }
        }
        return lista;
    }
}
