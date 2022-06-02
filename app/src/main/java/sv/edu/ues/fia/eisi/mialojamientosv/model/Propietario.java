package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Propietario extends SugarRecord<Propietario> {
    private String idPropietario;
    private String nombre;

    public Propietario() {
    }

    public Propietario(String idPropietario, String nombre) {
        this.idPropietario = idPropietario;
        this.nombre = nombre;
    }

    public String getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(String idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
