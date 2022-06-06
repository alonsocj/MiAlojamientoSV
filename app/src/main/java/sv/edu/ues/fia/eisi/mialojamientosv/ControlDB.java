package sv.edu.ues.fia.eisi.mialojamientosv;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.ChatsActivity;

public class ControlDB {

    public static void llenarBase(){
        //llenarHoteles();
        llenarPerfil();
        //llenarChats();
    }
    private static void llenarHoteles() {
        Propietario.deleteAll(Propietario.class);
        Hotel.deleteAll(Hotel.class);
        for (int i = 0; i < 10; i++) {
            Hotel hotel = new Hotel();
            Propietario propietario = new Propietario();
            propietario.setIdPropietario("" + i);
            propietario.setNombre("Propietario " + i);
            propietario.save();
            hotel.setPropietario(propietario);
            hotel.setIdHotel(""+i);
            hotel.setTitulo("Hotel " + i);
            hotel.setImagen("https://images.trvl-media.com/hotels/20000000/19770000/19760800/19760779/3498fa05_z.jpg");
            hotel.setDescripcion("Descripcion " + i);
            hotel.setDireccion("Direccion " + i);
            hotel.setLatitudH("Latitud " + i);
            hotel.setLongitudH("Longitud " + i);
            hotel.save();
        }
    }
    private static void llenarPerfil() {
        Favorito.deleteAll(Favorito.class);
        Perfil.deleteAll(Perfil.class);
        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        perfil.setNombre("Perfil 1");
        perfil.setLatitud("Latitud 1");
        perfil.setLongitud("Longitud 1");
        perfil.setGenero("Masculino");
        perfil.setEmail("email");
        perfil.save();
    }

    private static void llenarChats() {
        Perfil.deleteAll(Chat.class);
        for(int i=0;i<2;i++){
            Chat chat = new Chat();
            chat.setIdChat("reser1");
            chat.setNombre("Hotel1");
            chat.setEmisor("1");
            chat.setReceptor("1");
            //chat.save();

            Chat chat1 = new Chat();
            chat1.setIdChat("reser2");
            chat1.setNombre("Hotel1");
            chat1.setEmisor("2");
            chat1.setReceptor("2");
            //chat1.save();

            //Creamos un HashMap para mandar los datos a Firebase
            HashMap<Object,String> Chats=new HashMap<>();
            Chats.put("idChat",chat.getIdChat());
            Chats.put("nombre",chat.getNombre());
            Chats.put("emisor",chat.getEmisor());
            Chats.put("receptor",chat.getReceptor());
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference("ListadoChats");
            reference.child(chat.getIdChat()).setValue(Chats);

            //Creamos un HashMap para mandar los datos a Firebase
            HashMap<Object,String> Chats1=new HashMap<>();
            Chats1.put("idChat",chat1.getIdChat());
            Chats1.put("nombre",chat1.getNombre());
            Chats1.put("emisor",chat1.getEmisor());
            Chats1.put("receptor",chat1.getReceptor());
            database.getReference("ListadoChats");
            reference.child(chat1.getIdChat()).setValue(Chats);
        }
    }
}
