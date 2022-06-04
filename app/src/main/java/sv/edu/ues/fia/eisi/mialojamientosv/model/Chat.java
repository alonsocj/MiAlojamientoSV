package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Chat extends SugarRecord<Chat> {
    private String idChat;
    private String emisor;
    private String receptor;

    public Chat() {
    }

    public Chat(String idChat, String emisor, String receptor) {
        this.idChat = idChat;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
}
