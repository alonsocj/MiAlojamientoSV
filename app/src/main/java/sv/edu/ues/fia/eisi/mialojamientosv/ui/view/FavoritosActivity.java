package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListHotelAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityFavoritosBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;

public class FavoritosActivity extends AppCompatActivity {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    GestureDetectorCompat gestureDetectorCompat;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ActivityFavoritosBinding binding;
    BottomNavigationView navigationView;
    RecyclerView listHoteles;
    List<Hotel> hoteles;
    List<Favorito> favoritos ;
    Perfil perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetectorCompat=new GestureDetectorCompat(this, new FavoritosActivity.GestureDetectorListener());


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


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
            perfil = Perfil.find(Perfil.class, "ID_PERFIL_F = '" + firebaseUser.getUid()+"'", null).get(0);
            favoritos =  Favorito.findWithQuery(Favorito.class, "SELECT * FROM FAVORITO WHERE PERFIL = "+perfil.getId());
            if(favoritos.size() != 0){
                for (int i = 0; i < favoritos.size(); i++) {
                    hoteles.add(favoritos.get(i).getHotel());
                }
                //manager de recycler
                listHoteles.setLayoutManager(new LinearLayoutManager(this));
                ListHotelAdapter adapter = new ListHotelAdapter(hoteles);
                listHoteles.setAdapter(adapter);
            }else{
            }
        }catch (Exception e){
            Toast.makeText(view.getContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
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
                        startActivity(new Intent(FavoritosActivity.this, MainActivity.class));
                    }else{
                        //onSwipeLeft();
                        startActivity(new Intent(FavoritosActivity.this, MapaActivity.class));
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
                startActivity(new Intent(FavoritosActivity.this, ChatsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                startActivity(new Intent(FavoritosActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }
}