package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;


public class Perfil extends SugarRecord<Perfil> {
    private String idPerfilF;
    private String nombre;
    private String genero;
    private String email;

    public Perfil() {
    }

    public Perfil(String idPerfilF, String nombre, String genero, String email) {
        this.idPerfilF = idPerfilF;
        this.nombre = nombre;
        this.genero = genero;
        this.email = email;
    }

    public String getIdPerfilF() {
        return idPerfilF;
    }

    public void setIdPerfilF(String idPerfilF) {
        this.idPerfilF = idPerfilF;
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
