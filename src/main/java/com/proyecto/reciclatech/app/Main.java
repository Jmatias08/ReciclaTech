package com.proyecto.reciclatech.app;

import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;

public class Main {
    public static void main(String[] args) {
        MongoDatabase db = MongoConnection.getDatabase("ReciclaTech");
        System.out.println("Database name: " + db.getName());
    }
}
