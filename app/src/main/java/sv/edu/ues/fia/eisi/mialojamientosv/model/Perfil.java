package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;


public class Perfil extends SugarRecord<Perfil> {
    private int idPerfil;
    private String nombre;
    private String genero;
    private String email;

    public Perfil() {
    }

    public Perfil(int idPerfil, String nombre, String genero, String email) {
        this.idPerfil = idPerfil;
        this.nombre = nombre;
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
