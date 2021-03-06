package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListMensajesAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.TextToSpeechManager;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Mensaje;
import sv.edu.ues.fia.eisi.mialojamientosv.R;

public class MensajesActivity extends AppCompatActivity{
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference baseDatos;

    private EditText mensaje;
    private TextView hotel,nombre;
    private Button enviar, audio;
    private ListMensajesAdapter adapter;
    private RecyclerView rvMensajes;
    private FirebaseDatabase database;

    //Inicializamos la entrada de texto a voz
    //Texto a Voz
    TextToSpeechManager textToSpeech;
    private boolean cargado = false;

    //Este es se utiliza para grabar voz
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    String idPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        baseDatos= FirebaseDatabase.getInstance().getReference("usuariosApp");


        //Extraemos el usuario logueado
        baseDatos.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if (snapshot.exists()){
                    //obteniendo datos
                    idPerfil=""+snapshot.child("nombre").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setContentView(R.layout.activity_mensajes);

        mensaje = findViewById(R.id.txtMensaje);
        hotel = findViewById(R.id.contactoMensaje);
        enviar = findViewById(R.id.btnEnviar);
        audio = findViewById(R.id.btnAudio);
        rvMensajes=findViewById(R.id.rvMensajes);

        //Inicializando TextToSpeech
        textToSpeech=new TextToSpeechManager();
        textToSpeech.init(this);

        //Obteniendo el codigo del chat
        Bundle datosExtras=getIntent().getExtras();
        String codigoChat= datosExtras.getString("codigoChat");
        String nombreHotel=datosExtras.getString("nombreHotel");
        String receptor=datosExtras.getString("receptor");

        //Inicializamos la base de datos
        baseDatos = FirebaseDatabase.getInstance().getReference().child("Chats").child(codigoChat);  //Sala de Chat, donde se "Guardaran" los mensajes

        //Inicializando el Chat
        hotel.setText(nombreHotel);

        adapter =new ListMensajesAdapter(this);
        LinearLayoutManager linear=new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(linear);
        rvMensajes.setAdapter(adapter);

        enviar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String validacion=mensaje.getText().toString();
                validacion=validacion.replace(" ","");
                if(validacion.equals("")){
                    mensaje.setText("");
                    mensaje.setHint("Porfavor d??gite un mensaje");
                }else {
                    baseDatos.push().setValue(new Mensaje(mensaje.getText().toString(),idPerfil.toString(),obtenerHora()+"    "+obtenerFecha()));
                    NotificacionTopico();
                    mensaje.setText("");
                    mensaje.setHint("Escribe un mensaje");
                    textToSpeech.iniciarCola("Mensaje Enviado");
                }
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metodo donde se graba la voz y se guarda en la base
                iniciarEntradaVoz();
            }
        });

        //Cuando el adaptador adquiere un objeto
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        baseDatos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Cuando agreguemos un dato a la base de datos, lo agregara a la lista de chat
                Mensaje m = dataSnapshot.getValue(Mensaje.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    //se graba la voz y si se realiza pasa al siguiente metodo
    private void iniciarEntradaVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla para grabar el mensaje");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){

        }
    }
    //Guarda el texto en el textarea y lo agrega el mensaje
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null != data ){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mensaje.setText(result.get(0));
                }
                break;
            }
        }
    }
    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String obtenerHora(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf= new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().toZoneId().toString()));
        return sdf.format(date);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String obtenerFecha(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().toZoneId().toString()));
        return sdf.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.apagar();
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
            Toast.makeText(this,"Ocurrio un error",Toast.LENGTH_LONG);
        }
    }
}