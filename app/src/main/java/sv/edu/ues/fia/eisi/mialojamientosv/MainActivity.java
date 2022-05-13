package sv.edu.ues.fia.eisi.mialojamientosv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMainBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.ExploreFragment;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.FavoritosFragment;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.MapaFragment;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.MensajeFragment;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView navigationView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        navigationView = binding.bottomNavigation;

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new MapaFragment()).commit();
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
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.explore:
                        fragment = new ExploreFragment();
                        break;
                    case R.id.favoritos:
                        fragment = new FavoritosFragment();
                        break;
                    case R.id.mapa:
                        fragment = new MapaFragment();
                        break;
                    case R.id.mensajes:
                        fragment = new MensajeFragment();
                        break;
                    case R.id.perfil:
                        fragment = new PerfilFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, Objects.requireNonNull(fragment)).commit();
                return true;
            }
        });
    }
}