package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Habitacion extends SugarRecord<Habitacion> {
    int cantCamas;
    int disponibilidad;
    int cantBat;
    int cantPersonas;
    float precioPorDia;
    String serviciosExtra;
}
