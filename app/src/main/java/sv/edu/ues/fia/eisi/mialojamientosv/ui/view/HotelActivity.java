package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.SplashScreen;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityHotelBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;

public class HotelActivity extends AppCompatActivity {

    ActivityHotelBinding binding;
    TextView tvNombreHotel, tvDireccionHotel, tvDescriptionHotel;
    LottieAnimationView botonf;
    Hotel hotel;
    Perfil perfil;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        botonf = binding.animationFavoriteHotel;
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

        //Se recupera el perfil que esta logeado, por el momento es un numero fijo
        perfil = Perfil.find(Perfil.class, "ID_PERFIL = " + 1, null).get(0);
        //Se comprueba si existe le favorito en ese caso se pone la animación ya activa
        int i1 = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = "+ perfil.getId(), null).size();
        if (i1 != 0) {
            botonf.setProgress(1);
        }

        botonf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comprueba si ya existe el favorito
                int i1 = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = "+ perfil.getId(), null).size();
                //Si no existe crea el favorito y activa la animación
                if(i1 ==0){
                    Favorito favorito = new Favorito(1,perfil,hotel);
                    favorito.save();
                    botonf.playAnimation();
                    Toast.makeText(view.getContext(), "Se ha agregado el hotel a tus favoritos", Toast.LENGTH_LONG).show();
                    Log.d("prueba", i1+"");
                }else{
                // Si existe el favorito lo elimina y desactiva la animación
                    Favorito favoritoold = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = "+ perfil.getId(), null).get(0);
                    favoritoold.delete();
                    botonf.setProgress(0);
                    Log.d("prueba", i1+"");
                    Toast.makeText(view.getContext(), "Hotel eliminado tus favoritos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}