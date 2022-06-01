package sv.edu.ues.fia.eisi.mialojamientosv.model;

public class Perfil {
    private int idPerfil;
    private String nombre;
    private String Latitud;
    private String Longitud;
    private String Genero;
    private String email;

    public Perfil() {
    }

    public Perfil(int idPerfil, String nombre, String latitud, String longitud, String genero, String email) {
        this.idPerfil = idPerfil;
        this.nombre = nombre;
        Latitud = latitud;
        Longitud = longitud;
        Genero = genero;
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
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
