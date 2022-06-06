package sv.edu.ues.fia.eisi.mialojamientosv;

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

import java.util.Collections;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListHotelAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMainBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.FavoritosActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.MapaActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.ChatsActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.PerfilActivity;

public class MainActivity extends AppCompatActivity {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    GestureDetectorCompat gestureDetectorCompat;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private ActivityMainBinding binding;
    BottomNavigationView navigationView;
    RecyclerView listHoteles;
    List<Hotel> hoteles;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetectorCompat=new GestureDetectorCompat(this, new GestureDetectorListener());

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

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
        Collections.reverse(hoteles);
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
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }else{
                        //onSwipeLeft();
                        startActivity(new Intent(MainActivity.this, FavoritosActivity.class));
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
                startActivity(new Intent(MainActivity.this, ChatsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }


}