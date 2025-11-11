package com.proyecto.reciclatech.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import com.proyecto.reciclatech.model.Usuario;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.model.IndexOptions;

import static com.mongodb.client.model.Filters.eq;

public class UsuarioService {
    private final MongoCollection<Document> collection;

    public UsuarioService() {
        MongoDatabase db = MongoConnection.getDatabase("ReciclaTech");
        this.collection = db.getCollection("usuarios");

        // Crear índice único en carnet si no existe
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(new Document("carnet", 1), indexOptions);
    }

    // Crear usuario
    public boolean crearUsuario(Usuario usuario) {
        try {
            Document doc = new Document("carnet", usuario.getCarnet())
                    .append("carrera", usuario.getCarrera())
                    .append("password", usuario.getPassword())
                    .append("puntos", 0); // nuevo campo
            collection.insertOne(doc);
            System.out.println("[MongoDB] Usuario creado con carnet: " + usuario.getCarnet());
            return true;
        } catch (Exception e) {
            System.err.println("[MongoDB] Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    public void modificarUsuario(String carnetAntiguo, Usuario usuarioActualizado) {
        // Buscar el usuario con el carnet antiguo
        var usuarioExistente = collection.find(eq("carnet", carnetAntiguo)).first();

        if (usuarioExistente == null) {
            System.out.println("[MongoDB] No se encontró usuario con correo " + carnetAntiguo);
            return;
        }

        // Verificar si el nuevo carnet ya existe y es diferente al antiguo
        if (!carnetAntiguo.equals(usuarioActualizado.getCarnet())) {
            var carnetRepetido = collection.find(eq("carnet", usuarioActualizado.getCarnet())).first();
            if (carnetRepetido != null) {
                System.out.println("[MongoDB] Error: el nuevo usuario " + usuarioActualizado.getCarnet() + " ya existe.");
                return;
            }
        }

        // Actualizar los campos
        collection.updateOne(
                eq("carnet", carnetAntiguo),
                new Document("$set", new Document("carnet", usuarioActualizado.getCarnet())
                        .append("carrera", usuarioActualizado.getCarrera())
                        .append("password", usuarioActualizado.getPassword()))
        );

        System.out.println("[MongoDB] Usuario con correo " + carnetAntiguo + " modificado exitosamente.");
    }

    // Buscar usuario por carnet
    public Usuario buscarPorCarnet(String carnet) {
        Document doc = collection.find(eq("carnet", carnet)).first();
        if (doc != null) {
            Usuario u = new Usuario(
                    doc.getString("carnet"),
                    doc.getString("carrera"),
                    doc.getString("password")
            );
            ObjectId id = doc.getObjectId("_id");
            if (id != null) u.setId(id.toHexString());
            u.setPuntos(doc.getInteger("puntos", 0)); // cargar puntos
            return u;
        }
        return null;
    }

    // Validar login
    public boolean validarLogin(String carnet, String password) {
        Usuario usuario = buscarPorCarnet(carnet);

        if (usuario == null) {
            System.out.println("[LOGIN] Usuario no existe");
            return false;
        }

        if (!usuario.getPassword().equals(password)) {
            System.out.println("[LOGIN] Contraseña incorrecta");
            return false;
        }
        System.out.println("[LOGIN] Usuario " + carnet + " ingresó correctamente");
        return true;
    }

}
