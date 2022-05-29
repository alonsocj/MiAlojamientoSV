package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityFavoritosBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMapaBinding;

public class FavoritosActivity extends AppCompatActivity {
    Button pushEspecifico;
    ActivityFavoritosBinding binding;
    BottomNavigationView navigationView;
    LottieAnimationView botonf;
    DatabaseReference data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        pushEspecifico = findViewById(R.id.pushE);
        navigationView = binding.bottomNavigation;
        navigationView.setSelectedItemId(R.id.favoritos);
        botonf = (LottieAnimationView) findViewById(R.id.animationFavorite);
        data = FirebaseDatabase.getInstance().getReference();
        consultaToken();
        pushEspecifico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultaToken();
            }
        });

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

    private void consultaToken(){
        data.child("token").child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String token = snapshot.getValue().toString();
                    NotificacionEspecifico(token);
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void NotificacionEspecifico(String token) {
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try{
            token ="eu61ErSkRQ2O8ybrE8Viqd:APA91bE8hZ-jipUWNzx5ZZtzonQnOQOO4ZB5tjtcvJ_6MqDgA49RQO-r6Pbfk3cdkrHyfR2W2NYWSMtNUW8KzjZzXUtjpTG4-hznl3oWrCHvHUY9JpJUSXwMTVXFy_zSEhQgHNt_GFsD";
            json.put("to",token);
            JSONObject notificacion =new JSONObject();
            notificacion.put("titulo","Mensaje Nuevo");
            notificacion.put("detalle","Tiene mensajes nuevos");
            json.put("data",notificacion);
            String url = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,json,null,null){
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> header = new HashMap<>();
                    header.put("Authorization", "key=AAAALA98pYw:APA91bHRnlGfODSkRge-a_oNmTdSWGLr1WlYytnXsr1FVTgdWHn_kjCL7Vmrv9OtgbEVDJozqAgKlKjyip2-h_3UEokH2VtLRPPnaffb0ZAjsCCkMZZESroqb5H3-fenUmzpQU-nzp3_");
                    header.put("Content-type", "application/json");
                    return header;
                }
            };
            myrequest.add(request);
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this,"no sirve",Toast.LENGTH_LONG);
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