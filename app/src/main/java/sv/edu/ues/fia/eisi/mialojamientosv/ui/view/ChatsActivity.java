
package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListChatsAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityChatsBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;

public class ChatsActivity extends AppCompatActivity {

    GestureDetectorCompat gestureDetectorCompat;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ActivityChatsBinding binding;
    BottomNavigationView navigationView;
    ListView listaChats;
    List<Chat> listadoChat;
    private DatabaseReference databaseReference;
    public static DatabaseReference databaseReference2;
    public static String idPerfil;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Instanciamos los objetos de la vista
        swipeRefreshLayout=binding.refresh;

        gestureDetectorCompat=new GestureDetectorCompat(this, new ChatsActivity.GestureDetectorListener());

        //Instanciamos nuestro autenticador
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("usuariosApp");
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if (snapshot.exists()){
                    //obteniendo datos
                    idPerfil=""+snapshot.child("correo").getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //Extraemos los chats de la base de datos
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        listadoChat=new ArrayList<>();
        getChatsFirebase();
        listadoChat=Chat.listAll(Chat.class);

        //Cargamos los chats del perfil en pantalla
        listaChats = (ListView) findViewById(R.id.ListadoChats);
        listaChats.setAdapter(new ListChatsAdapter(this, R.layout.list_chat, listadoChat){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView nombre = (TextView) view.findViewById(R.id.nombreHotel);
                    if (nombre != null)
                        nombre.setText(((Chat) entrada).getNombre());
                }
            }
        });

        //Abrimos la pantalla de los mensajes del chat correspondiente
        listaChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Chat elegido = (Chat) pariente.getItemAtPosition(posicion);
                Intent i = new Intent(ChatsActivity.this, MensajesActivity.class);
                i.putExtra("codigoChat", elegido.getIdChat());
                i.putExtra("nombreHotel", elegido.getNombre());
                i.putExtra("receptor",elegido.getReceptor());
                startActivity(i);
            }
        });

        //Refrescamos la lista de chats
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        navigationView = binding.bottomNavigation;
        navigationView.setSelectedItemId(R.id.mensajes);

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
                        startActivity(new Intent(ChatsActivity.this, MapaActivity.class));
                    }else{
                        //onSwipeLeft();
                        startActivity(new Intent(ChatsActivity.this, PerfilActivity.class));
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
                startActivity(new Intent(ChatsActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.favoritos:
                startActivity(new Intent(ChatsActivity.this, FavoritosActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mapa:
                startActivity(new Intent(ChatsActivity.this, MapaActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mensajes:
                break;
            case R.id.perfil:
                startActivity(new Intent(ChatsActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }

    public static void getChatsFirebase(){
        //Extraemos los datos de la base de datos Firebase
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child("ListadoChats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chat.deleteAll(Chat.class);
                for (DataSnapshot data:snapshot.getChildren()){
                    databaseReference2.child("ListadoChats").child(data.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Chat chat=new Chat();
                            chat.setIdChat(""+snapshot.child("idChat").getValue());
                            chat.setNombre(""+snapshot.child("nombre").getValue());
                            chat.setEmisor(""+snapshot.child("emisor").getValue());
                            chat.setReceptor(""+snapshot.child("receptor").getValue());

                            Log.d("result", chat.getIdChat()+" "+chat.getNombre()+" "+chat.getEmisor()+" "+chat.getReceptor());

                            List<Chat> listaChats=Chat.listAll(Chat.class);
                            Boolean existe=false;
                            for(int i=0;i<listaChats.size();i++){
                                if(listaChats.get(i).getIdChat().equals(chat.getIdChat())){
                                    existe=true;
                                    break;
                                }
                            }
                            if(!existe){
                                //Seleccionamos los chats del perfil logueado
                                if (chat.getEmisor().equals(idPerfil) || chat.getReceptor().equals(idPerfil)) {
                                    chat.save();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}