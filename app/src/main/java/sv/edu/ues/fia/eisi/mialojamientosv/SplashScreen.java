package sv.edu.ues.fia.eisi.mialojamientosv;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        llenarHoteles();
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
            hotel.setImagen("https://images.trvl-media.com/hotels/20000000/19770000/19760800/19760779/3498fa05_z.jpg");
            hotel.setDescripcion("Descripcion " + i);
            hotel.setDireccion("Direccion " + i);
            hotel.setLatitudH("Latitud " + i);
            hotel.setLongitudH("Longitud " + i);
            hotel.save();
        }
    }
}