package com.proyecto.reciclatech.model;

import java.util.List;

public class Pregunta {
    private String id;
    private String texto;
    private List<String> opciones;
    private String respuestaCorrecta;
    private int dificultad; // 1=fácil, 2=media, 3=difícil

    public Pregunta() {}

    public Pregunta(String texto, List<String> opciones, String respuestaCorrecta, int dificultad) {
        this.texto = texto;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        this.dificultad = dificultad;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public int getDificultad() { return dificultad; }
    public void setDificultad(int dificultad) { this.dificultad = dificultad; }
}
