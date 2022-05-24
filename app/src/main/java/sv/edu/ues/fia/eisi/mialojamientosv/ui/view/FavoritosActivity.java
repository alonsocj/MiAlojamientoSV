package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityFavoritosBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMapaBinding;

public class FavoritosActivity extends AppCompatActivity {

    ActivityFavoritosBinding binding;
    BottomNavigationView navigationView;
    LottieAnimationView botonf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        navigationView = binding.bottomNavigation;

        navigationView.setSelectedItemId(R.id.favoritos);
        botonf = (LottieAnimationView) findViewById(R.id.animationFavorite);
        botonf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonf.playAnimation();
                //botonf.setProgress(0);
            }
        });

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