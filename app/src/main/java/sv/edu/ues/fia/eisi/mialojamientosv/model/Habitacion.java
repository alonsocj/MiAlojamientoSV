package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Habitacion extends SugarRecord<Habitacion> {
    private int idHabitacion;
    private int cantCamas;
    private int disponibilidad;
    private int cantBat;
    private int cantPersonas;
    private float precioPorDia;
    private String serviciosExtra;
    Hotel idHotel;

    public Habitacion() {
    }

    public Habitacion(int idHabitacion, int cantCamas, int disponibilidad, int cantBat, int cantPersonas, float precioPorDia, String serviciosExtra, Hotel idHotel) {
        this.idHabitacion = idHabitacion;
        this.cantCamas = cantCamas;
        this.disponibilidad = disponibilidad;
        this.cantBat = cantBat;
        this.cantPersonas = cantPersonas;
        this.precioPorDia = precioPorDia;
        this.serviciosExtra = serviciosExtra;
        this.idHotel = idHotel;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getCantCamas() {
        return cantCamas;
    }

    public void setCantCamas(int cantCamas) {
        this.cantCamas = cantCamas;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getCantBat() {
        return cantBat;
    }

    public void setCantBat(int cantBat) {
        this.cantBat = cantBat;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public void setCantPersonas(int cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public float getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(float precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    public String getServiciosExtra() {
        return serviciosExtra;
    }

    public void setServiciosExtra(String serviciosExtra) {
        this.serviciosExtra = serviciosExtra;
    }

    public Hotel getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(Hotel idHotel) {
        this.idHotel = idHotel;
    }
}
