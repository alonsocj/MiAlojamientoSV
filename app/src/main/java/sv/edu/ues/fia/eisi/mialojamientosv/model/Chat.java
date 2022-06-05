package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Chat extends SugarRecord<Chat> {
    private String idChat;
    private String nombre;
    private Integer emisor;
    private Integer receptor;

    public Chat() {
    }

    public Chat(String idChat, String nombre, Integer emisor, Integer receptor) {
        this.idChat = idChat;
        this.nombre = nombre;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEmisor() {
        return emisor;
    }

    public void setEmisor(Integer emisor) {
        this.emisor = emisor;
    }

    public Integer getReceptor() {
        return receptor;
    }

    public void setReceptor(Integer receptor) {
        this.receptor = receptor;
    }
}
