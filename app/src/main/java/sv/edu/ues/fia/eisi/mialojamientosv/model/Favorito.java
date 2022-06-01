package sv.edu.ues.fia.eisi.mialojamientosv.model;

import com.orm.SugarRecord;

public class Favorito extends SugarRecord<Favorito> {
    private int idFavorito;
    private Perfil perfil;
    private Hotel hotel;

    public Favorito() {
    }

    public Favorito(int idFavorito, Perfil perfil, Hotel hotel) {
        this.idFavorito = idFavorito;
        this.perfil = perfil;
        this.hotel = hotel;
    }

    public int getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
