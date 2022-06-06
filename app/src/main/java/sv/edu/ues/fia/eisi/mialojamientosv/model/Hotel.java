package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Hotel extends SugarRecord<Hotel> {
    private String idHotel;
    private String titulo;
    private String imagen;
    private String descripcion;
    private String direccion;
    private String latitudH;
    private String longitudH;
    private String evaluaciones;
    Propietario propietario;

    public Hotel() {
    }

    public Hotel(String idHotel, String titulo, String imagen, String descripcion, String direccion, String latitudH, String longitudH, Propietario propietario) {
        this.idHotel = idHotel;
        this.titulo = titulo;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.latitudH = latitudH;
        this.longitudH = longitudH;
        this.propietario = propietario;
    }

    public String getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(String idHotel) {
        this.idHotel = idHotel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitudH() {
        return latitudH;
    }

    public void setLatitudH(String latitudH) {
        this.latitudH = latitudH;
    }

    public String getLongitudH() {
        return longitudH;
    }

    public void setLongitudH(String longitudH) {
        this.longitudH = longitudH;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public String getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(String evaluaciones) {
        this.evaluaciones = evaluaciones;
    }
}
