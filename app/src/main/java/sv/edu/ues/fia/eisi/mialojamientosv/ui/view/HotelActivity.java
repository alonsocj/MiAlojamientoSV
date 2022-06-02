package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityHotelBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;

public class HotelActivity extends AppCompatActivity {

    ActivityHotelBinding binding;
    TextView tvNombreHotel, tvDireccionHotel, tvDescriptionHotel;
    Hotel hotel;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tvNombreHotel = binding.tvHotelName;
        tvDireccionHotel = binding.tvAddress;
        tvDescriptionHotel = binding.tvHotelDescription;

        if (savedInstanceState != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("idHotel");
            }
        } else {
            id = (int) getIntent().getSerializableExtra("idHotel");
        }
        hotel = Hotel.find(Hotel.class, "ID_HOTEL = " + id, null).get(0);
        if (hotel != null) {
            tvNombreHotel.setText(hotel.getTitulo());
            tvDescriptionHotel.setText(hotel.getDescripcion());
            tvDireccionHotel.setText(hotel.getDireccion());
        }
    }
}