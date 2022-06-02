package sv.edu.ues.fia.eisi.mialojamientosv;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        llenarHoteles();
        llenarPerfil();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 5195);

    }

    private void llenarHoteles() {
        Propietario.deleteAll(Propietario.class);
        Hotel.deleteAll(Hotel.class);
        for (int i = 0; i < 10; i++) {
            Hotel hotel = new Hotel();
            Propietario propietario = new Propietario();
            propietario.setIdPropietario("" + i);
            propietario.setNombre("Propietario " + i);
            propietario.save();
            hotel.setPropietario(propietario);
            hotel.setIdHotel(i);
            hotel.setTitulo("Hotel " + i);
            hotel.setDescripcion("Descripcion " + i);
            hotel.setDireccion("Direccion " + i);
            hotel.setLatitudH("Latitud " + i);
            hotel.setLongitudH("Longitud " + i);
            hotel.save();
        }
    }
    private void llenarPerfil() {
        Favorito.deleteAll(Favorito.class);
        Perfil.deleteAll(Perfil.class);
            Perfil perfil = new Perfil();
            perfil.setIdPerfil(1);
            perfil.setNombre("Perfil 1");
            perfil.setLatitud("Latitud 1");
            perfil.setLongitud("Longitud 1");
            perfil.setGenero("Masculino");
            perfil.setEmail("email");
            perfil.save();
    }
}