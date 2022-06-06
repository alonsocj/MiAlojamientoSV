package sv.edu.ues.fia.eisi.mialojamientosv.model;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String mensaje;
    private String nombre;
    private String hora;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String hora) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.hora=hora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
