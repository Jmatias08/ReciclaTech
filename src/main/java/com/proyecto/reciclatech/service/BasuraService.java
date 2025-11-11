package com.proyecto.reciclatech.service;

import com.proyecto.reciclatech.model.Basura;
import com.proyecto.reciclatech.repository.BasuraRepository;

public class BasuraService {

    private final BasuraRepository repository;

    public BasuraService() {
        this.repository = new BasuraRepository();
    }

    /**
     * Obtiene la categoría de un residuo.
     * Primero busca en MongoDB, si no existe llama a la IA y guarda el resultado.
     */
    public Basura obtenerBasura(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return null;
        }

        // Convertimos el nombre a minúsculas para consistencia
        String nombreFormateado = nombre.toLowerCase().trim();

        // 1️⃣ Buscar primero en MongoDB
        Basura basura = repository.buscarPorNombre(nombreFormateado);
        if (basura != null) {
            System.out.println("[MongoDB] Encontrado: " + basura);
            return basura;
        }

        // 2️⃣ Consultar IA solo si no está en la base de datos
        System.out.println("[Groq API] No encontrado en Mongo, consultando IA...");
        String categoria = IAClassifier.clasificar(nombreFormateado);

        // 3️⃣ Guardar en MongoDB si la IA devuelve categoría válida
        if (categoria != null && !categoria.isEmpty()) {
            // Convertir a minúsculas para consistencia
            String cat = categoria.trim();

            // ✅ Solo guardar si pertenece a las categorías válidas
            if (cat.equalsIgnoreCase("Recipientes de plastico") ||
                    cat.equalsIgnoreCase("Carton y papel") ||
                    cat.equalsIgnoreCase("Latas de aluminio") ||
                    cat.equalsIgnoreCase("Organico")) {

                basura = new Basura(nombreFormateado, cat);
                repository.guardar(basura);
                System.out.println("[MongoDB] Guardado nuevo residuo: " + basura);
                return basura;
            } else {
                System.out.println("[Servicio] No es un residuo válido para el contenedor: " + nombreFormateado);
                return null;
            }
        }

        // Si no se pudo clasificar
        System.out.println("[Servicio] No se pudo clasificar el residuo: " + nombreFormateado);
        return null;
    }
}
