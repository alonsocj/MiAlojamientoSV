package sv.edu.ues.fia.eisi.mialojamientosv.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.HotelActivity;

public class ListHotelAdapter extends RecyclerView.Adapter<ListHotelAdapter.HotelViewHolder> {

    List<Hotel> listaHoteles;

    public ListHotelAdapter(List<Hotel> listaHoteles) {
        this.listaHoteles = listaHoteles;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hoteles, null, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        holder.title.setText(listaHoteles.get(position).getTitulo());
        holder.description.setText(listaHoteles.get(position).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaHoteles.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_hotel_title);
            description = itemView.findViewById(R.id.textView_hotel_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context ctx = v.getContext();
                    Intent intent = new Intent(ctx, HotelActivity.class);
                    intent.putExtra("idHotel", listaHoteles.get(getAdapterPosition()).getIdHotel());
                    ctx.startActivity(intent);
                }
            });
        }
    }
}
