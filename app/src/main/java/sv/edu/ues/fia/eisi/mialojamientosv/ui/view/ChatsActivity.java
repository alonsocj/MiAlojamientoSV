
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
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ues.fia.eisi.mialojamientosv.Adapters.ListChatsAdaptador;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityChatsBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;

public class ChatsActivity extends AppCompatActivity {

    ActivityChatsBinding binding;
    BottomNavigationView navigationView;
    ListView listaChats;
    List<Chat> listadoChat;
    List<Chat> listadoChatUsuario;
    Integer idPerfil=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Inicializamos nuestro listado de chat para el perfil logueado
        listadoChatUsuario=new ArrayList<>();

        //Extraemos los chats de la base de datos
        listadoChat = Chat.listAll(Chat.class);

        //Seleccionamos los chats del peril logueado
        for(int i=0;i<listadoChat.size();i++){
           Chat valor=listadoChat.get(i);
           if(valor.getEmisor().equals(idPerfil) || valor.getReceptor().equals(idPerfil)){
               listadoChatUsuario.add(valor);
           }
        }

        listaChats = (ListView) findViewById(R.id.ListadoChats);
        listaChats.setAdapter(new ListChatsAdaptador(this, R.layout.list_chat, listadoChatUsuario){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView nombre = (TextView) view.findViewById(R.id.nombreHotel);
                    if (nombre != null)
                        nombre.setText(((Chat) entrada).getNombre());
                }
            }
        });

        listaChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Chat elegido = (Chat) pariente.getItemAtPosition(posicion);
                Intent i = new Intent(ChatsActivity.this, MensajesActivity.class);
                i.putExtra("codigoChat", elegido.getIdChat());
                i.putExtra("nombreHotel", elegido.getNombre());
                startActivity(i);
                CharSequence texto = "Seleccionado: " + elegido.getNombre()+ + elegido.getEmisor()+ elegido.getReceptor();
                Toast toast = Toast.makeText(ChatsActivity.this, texto, Toast.LENGTH_LONG);
                toast.show();
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
}