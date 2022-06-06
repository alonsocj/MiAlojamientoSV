package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.ButtCap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityPerfilBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;

public class PerfilActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ActivityPerfilBinding binding;
    BottomNavigationView navigationView;
    EditText nombre, email, genero;
    Integer idPerfil=1;
    Button cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        nombre= findViewById(R.id.EditNombrePerfil);
        email = findViewById(R.id.EditEmailPerfil);
        genero = findViewById(R.id.EditGeneroPerfil);
        cerrarSesion=findViewById(R.id.botonCerrarSesion);

        //Extraemos los perfiles de la base de datos
        List<Perfil> listadoPerfiles=Perfil.listAll(Perfil.class);

        //Mostramos datos para el perfil logueado
        for(int i=0;i<listadoPerfiles.size();i++){
            Perfil perfil=new Perfil();
            perfil=listadoPerfiles.get(i);
            if(perfil.getIdPerfil()==idPerfil){
                nombre.setText(perfil.getNombre());
                email.setText(perfil.getEmail());
                genero.setText(perfil.getGenero());
            }
        }

        navigationView = binding.bottomNavigation;

        navigationView.setSelectedItemId(R.id.perfil);

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

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion(){
        firebaseAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    protected void onStart(){
        verificarInicioSesion();
        super.onStart();
    }

    private void verificarInicioSesion(){
        if(firebaseUser!=null){
        }else{
            startActivity(new Intent(this, homeLogin.class));
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void setActivity(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.favoritos:
                startActivity(new Intent(PerfilActivity.this, FavoritosActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mapa:
                startActivity(new Intent(PerfilActivity.this, MapaActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mensajes:
                startActivity(new Intent(PerfilActivity.this, ChatsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                break;
        }
    }
}