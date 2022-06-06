package sv.edu.ues.fia.eisi.mialojamientosv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityFavoritosBinding;

public class favoritoDetalleActivity extends AppCompatActivity {
    Button pushEspecifico;
    LottieAnimationView botonf;
    DatabaseReference data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito_detalle);
        pushEspecifico = findViewById(R.id.pushE);
        botonf = (LottieAnimationView) findViewById(R.id.animationFavorite);
        data = FirebaseDatabase.getInstance().getReference();
        botonf.setProgress(1);
        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                
            }
        });
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
    private void NotificacionTopico() {
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try{
            json.put("to","/topics/"+"enviaratodos");
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
}