package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Perfil extends SugarRecord<Perfil>{
    private int idPerfil;
    private String nombre;
    private String latitud;
    private String longitud;
    private String genero;
    private String email;

    public Perfil() {
    }

    public Perfil(int idPerfil, String nombre, String latitud, String longitud, String genero, String email) {
        this.idPerfil = idPerfil;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.genero = genero;
        this.email = email;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
