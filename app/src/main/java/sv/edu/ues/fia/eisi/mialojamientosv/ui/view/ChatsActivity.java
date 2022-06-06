
package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListChatsAdapter;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityChatsBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;

public class ChatsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ActivityChatsBinding binding;
    BottomNavigationView navigationView;
    ListView listaChats;
    List<Chat> listadoChat, listadoChatFirebase;
    List<Chat> listadoChatUsuario;
    private DatabaseReference databaseReference;
    String idPerfil="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


        //Inicializamos nuestro listado de chat para el perfil logueado
        listadoChatUsuario=new ArrayList<>();
        listadoChatFirebase=new ArrayList<>();

        //Extraemos los chats de la base de datos
        listadoChat = Chat.listAll(Chat.class);

        //Cargamos la base de datos y cargamos los chats
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getChatsFirebase();

        if(listadoChat==null){
            listadoChat=listadoChatFirebase;
        }else{
            //Actualizamos los chats de la base de datos local
            for (int i=0;i<listadoChatFirebase.size();i++){
                Chat chat=listadoChatFirebase.get(i);
                for (int j=0;j<listadoChat.size();j++){
                    if (!(chat.getIdChat().equals(listadoChat.get(j).getIdChat()))){
                        listadoChat.add(listadoChatFirebase.get(i));
                    }
                }
            }
        }

        //Seleccionamos los chats del perfil logueado
        for(int i=0;i<listadoChat.size();i++){
           Chat valor=listadoChat.get(i);
           if(valor.getEmisor().equals(idPerfil) || valor.getReceptor().equals(idPerfil)){
               listadoChatUsuario.add(valor);
           }
        }

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

    private void getChatsFirebase(){
        databaseReference.child("ListadoChats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot data: snapshot.getChildren()) {
                        Chat chat = new Chat();
                        chat.setIdChat(data.child("idChat").getValue().toString());
                        chat.setNombre(data.child("nombre").getValue().toString());
                        chat.setEmisor(data.child("emisor").getValue().toString());
                        chat.setReceptor(data.child("receptor").getValue().toString());
                        listadoChatFirebase.add(chat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}