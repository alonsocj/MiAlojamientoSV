package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMapaBinding;

public class MapaActivity extends AppCompatActivity {

    private ActivityMapaBinding binding;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        navigationView = binding.bottomNavigation;

        navigationView.setSelectedItemId(R.id.mapa);

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
                startActivity(new Intent(MapaActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.favoritos:
                startActivity(new Intent(MapaActivity.this, FavoritosActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mapa:
                break;
            case R.id.mensajes:
                startActivity(new Intent(MapaActivity.this, MensajesActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                startActivity(new Intent(MapaActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }
}