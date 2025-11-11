package com.proyecto.reciclatech.service;

import com.proyecto.reciclatech.model.Basura;
import com.proyecto.reciclatech.repository.BasuraRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Basura> obtenerTodas() {
        return repository.obtenerTodos();
    }

    /**
     * 🧮 Calcula puntos personales según la cantidad de residuos reciclados
     */
    public int calcularPuntos(List<Basura> lista) {
        if (lista == null) return 0;
        return lista.size() * 10; // Por ejemplo, 10 puntos por residuo
    }

    /**
     * 📊 Conteo de residuos por categoría del usuario actual
     */
    public Map<String, Integer> obtenerConteoPorCategoria(String carnet) {
        List<Basura> historial = repository.obtenerHistorialPorUsuario(carnet);
        Map<String, Integer> conteo = new HashMap<>();

        for (Basura b : historial) {
            conteo.put(b.getCategoria(), conteo.getOrDefault(b.getCategoria(), 0) + 1);
        }
        return conteo;
    }

    /**
     * 🌎 Conteo global de residuos por categoría (para el gráfico global)
     */
    public Map<String, Integer> obtenerConteoGlobal() {
        List<Basura> todos = repository.obtenerTodos();
        Map<String, Integer> conteo = new HashMap<>();

        for (Basura b : todos) {
            conteo.put(b.getCategoria(), conteo.getOrDefault(b.getCategoria(), 0) + 1);
        }
        return conteo;
    }
}
