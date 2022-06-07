package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;

public class PerfilActivity extends AppCompatActivity {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    GestureDetectorCompat gestureDetectorCompat;

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

        gestureDetectorCompat=new GestureDetectorCompat(this, new PerfilActivity.GestureDetectorListener());


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        baseDatos= FirebaseDatabase.getInstance().getReference("usuariosApp");

        nombre= findViewById(R.id.EditNombrePerfil);
        email = findViewById(R.id.EditEmailPerfil);
        genero = findViewById(R.id.EditGeneroPerfil);
        cerrarSesion=findViewById(R.id.botonCerrarSesion);


        //Extraemos el usuario logueado
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

    //Implementando gestos
    private class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent rightEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            boolean result=false;
            float diffX = moveEvent.getX()-rightEvent.getX();
            float diffY = moveEvent.getY()-rightEvent.getY();

            if(Math.abs(diffX)>Math.abs(diffY)){
                //right or left swipe
                if(Math.abs(diffX)> SWIPE_THRESHOLD && Math.abs(velocityX)> SWIPE_VELOCITY_THRESHOLD){

                    if (diffX>0){
                        //onSwipeRight();
                        startActivity(new Intent(PerfilActivity.this, ChatsActivity.class));
                    }else{
                        //onSwipeLeft();
                        startActivity(new Intent(PerfilActivity.this, PerfilActivity.class));
                    }
                    result=true;
                }
            }else{
                //up or down swipe
                if(Math.abs(diffY)> SWIPE_THRESHOLD && Math.abs(velocityY)> SWIPE_VELOCITY_THRESHOLD){
                    if (diffY>0){
                        //onSwipeBottom();
                    }else{
                        //onSwipeTop();
                    }
                }
                result=false;
            }
            return result;
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    //Fin implementar gestos

    private void cerrarSesion(){
        firebaseAuth.signOut();
        startActivity(new Intent(this, homeLogin.class));
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