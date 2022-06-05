package sv.edu.ues.fia.eisi.mialojamientosv.Adapters;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import sv.edu.ues.fia.eisi.mialojamientosv.R;

public class HolderMensaje extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView mensaje;
    private TextView hora;

    public HolderMensaje(View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.usuario);
        mensaje = (TextView) itemView.findViewById(R.id.mensaje);
        hora = (TextView) itemView.findViewById(R.id.hora);
    }
    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }
}