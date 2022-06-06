package sv.edu.ues.fia.eisi.mialojamientosv.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Mensaje;

public class ListMensajesAdapter extends RecyclerView.Adapter<ListHolderMensaje> {
    private List<Mensaje> listMensaje = new ArrayList<>();
    private Context c;

    public ListMensajesAdapter(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }
    @Override
    public ListHolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_mensaje,parent,false);
        return new ListHolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(ListHolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        holder.getHora().setText(listMensaje.get(position).getHora());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
