package sv.edu.ues.fia.eisi.mialojamientosv;

import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;

public class ControlDB {

    public static void llenarBase(){
        llenarHoteles();
        llenarPerfil();
        llenarChats();
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
            hotel.setIdHotel(i);
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
        for(int i=0;i<4;i++){
            Chat chat = new Chat();
            chat.setIdChat("reser1");
            chat.setNombre("Hotel1");
            chat.setEmisor(1);
            chat.setReceptor(1);
            chat.save();

            Chat chat1 = new Chat();
            chat1.setIdChat("reser2");
            chat1.setNombre("Hotel1");
            chat1.setEmisor(2);
            chat1.setReceptor(2);
            chat1.save();

            Chat chat2 = new Chat();
            chat2.setIdChat("reser3");
            chat2.setNombre("Hotel1");
            chat2.setEmisor(2);
            chat2.setReceptor(1);
            chat2.save();
        }
    }
}
