package com.proyecto.reciclatech.model;

public class Session {
    private static Session instancia;
    private Usuario usuario;

    private Session() {}

    public static Session getInstancia() {
        if (instancia == null) {
            instancia = new Session();
        }
        return instancia;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
