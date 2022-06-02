package sv.edu.ues.fia.eisi.mialojamientosv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListHotelAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMainBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.FavoritosActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.MapaActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.MensajesActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.PerfilActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView navigationView;
    RecyclerView listHoteles;
    List<Hotel> hoteles;
    LottieAnimationView botonf;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //recylcerview
        listHoteles = binding.listHoteles;
        //botom navigation
        navigationView = binding.bottomNavigation;
        navigationView.setSelectedItemId(R.id.explore);
        //lista de todoos los hoteles
        hoteles = Hotel.listAll(Hotel.class);
        //manager de recycler
        listHoteles.setLayoutManager(new LinearLayoutManager(this));


        ListHotelAdapter adapter = new ListHotelAdapter(hoteles);
        listHoteles.setAdapter(adapter);

        /*
         * BadgeDrawable badgeDrawable = navigationView.getOrCreateBadge(R.id.mensajes);
         * badgeDrawable.setVisible(true);
         * badgeDrawable.setNumber(4);
         */

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setActivity(item);
                return true;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void setActivity(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                break;
            case R.id.favoritos:
                startActivity(new Intent(MainActivity.this, FavoritosActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mapa:
                startActivity(new Intent(MainActivity.this, MapaActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mensajes:
                startActivity(new Intent(MainActivity.this, MensajesActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }


}