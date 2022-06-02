package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListHotelAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityFavoritosBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;

public class FavoritosActivity extends AppCompatActivity {

    ActivityFavoritosBinding binding;
    BottomNavigationView navigationView;
    RecyclerView listHoteles;
    List<Hotel> hoteles;
    List<Favorito> favoritos ;
    Perfil perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        hoteles = new ArrayList<Hotel>();
        favoritos = new ArrayList<Favorito>();
        //recylcerview
        listHoteles = binding.listHoteles;

        //botom navigation
        navigationView = binding.bottomNavigation;
        navigationView.setSelectedItemId(R.id.favoritos);
        try{
            perfil = Perfil.find(Perfil.class, "ID_PERFIL = " + 1, null).get(0);
            favoritos =  Favorito.findWithQuery(Favorito.class, "SELECT * FROM FAVORITO WHERE PERFIL = "+perfil.getId());
            if(favoritos.size() != 0){
                for (int i = 0; i < favoritos.size(); i++) {
                    hoteles.add(favoritos.get(i).getHotel());
                }
                //manager de recycler
                Toast.makeText(view.getContext(), "AAA", Toast.LENGTH_LONG).show();
                listHoteles.setLayoutManager(new LinearLayoutManager(this));
                ListHotelAdapter adapter = new ListHotelAdapter(hoteles);
                listHoteles.setAdapter(adapter);
            }else{
                Toast.makeText(view.getContext(), "nada", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(view.getContext(), "hola", Toast.LENGTH_LONG).show();
        }

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
                startActivity(new Intent(FavoritosActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.favoritos:
                break;
            case R.id.mapa:
                startActivity(new Intent(FavoritosActivity.this, MapaActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mensajes:
                startActivity(new Intent(FavoritosActivity.this, MensajesActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                startActivity(new Intent(FavoritosActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }
}