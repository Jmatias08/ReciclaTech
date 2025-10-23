package com.proyecto.reciclatech.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnection {

    private static final String CONNECTION_STRING =
            "mongodb+srv://Admin:admin@reciclatech.7v4gdka.mongodb.net/?retryWrites=true&w=majority&appName=ReciclaTech";

    // Volatile para visibilidad en múltiples hilos
    private static volatile MongoClient mongoClient;

    // Private constructor para evitar instancias
    private MongoConnection() {}

    // Inicialización thread-safe
    private static void initClient() {
        if (mongoClient == null) {
            synchronized (MongoConnection.class) {
                if (mongoClient == null) {
                    try {
                        ServerApi serverApi = ServerApi.builder()
                                .version(ServerApiVersion.V1)
                                .build();

                        MongoClientSettings settings = MongoClientSettings.builder()
                                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                                .serverApi(serverApi)
                                .build();

                        mongoClient = MongoClients.create(settings);

                        // Verificar conexión con ping
                        MongoDatabase database = mongoClient.getDatabase("admin");
                        database.runCommand(new Document("ping", 1));
                        System.out.println("[MongoDB] Conexión exitosa a la base de datos.");
                    } catch (MongoException e) {
                        System.err.println("[MongoDB] Error al conectar a la base de datos: " + e.getMessage());
                        mongoClient = null; // asegurar que no quede un cliente incompleto
                    }
                }
            }
        }
    }

    // Método público para obtener una base de datos específica
    public static MongoDatabase getDatabase(String dbName) {
        initClient();
        if (mongoClient != null) {
            System.out.println("[MongoDB] La conexión sigue activa.");
            return mongoClient.getDatabase(dbName);
        } else {
            System.err.println("[MongoDB] No se puede obtener la base de datos, la conexión no está activa.");
            return null;
        }
    }

    // Método para cerrar la conexión
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            System.out.println("[MongoDB] Conexión cerrada.");
        }
    }
}
