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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityPerfilBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;

public class PerfilActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference baseDatos;

    ActivityPerfilBinding binding;
    BottomNavigationView navigationView;
    TextInputEditText nombre, email, genero;
    Button cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        baseDatos= FirebaseDatabase.getInstance().getReference("usuariosApp");

        nombre= findViewById(R.id.EditNombrePerfil);
        email = findViewById(R.id.EditEmailPerfil);
        genero = findViewById(R.id.EditGeneroPerfil);
        cerrarSesion=findViewById(R.id.botonCerrarSesion);


        //extraer datos de la base firebase
        baseDatos.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if (snapshot.exists()){
                    //obteniendo datos
                    String correos=""+snapshot.child("correo").getValue();
                    String generos=""+snapshot.child("genero").getValue();
                    String nombres=""+snapshot.child("nombre").getValue();

                    //seteamos datos
                    nombre.setText(nombres.toUpperCase());
                    email.setText(correos);
                    genero.setText(generos.toUpperCase());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        startActivity(new Intent(this, homeLogin.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
                finish();
                break;
            case R.id.favoritos:
                startActivity(new Intent(PerfilActivity.this, FavoritosActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.mapa:
                startActivity(new Intent(PerfilActivity.this, MapaActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.mensajes:
                startActivity(new Intent(PerfilActivity.this, ChatsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.perfil:
                break;
        }
    }
}