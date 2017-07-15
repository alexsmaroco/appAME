package ufjf.ame;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;

/**
 * Created by Alex on 14/07/2017.
 */

public class Evento {
    private String id;
    private Location loc;
    private String tipoEvt;
    private ArrayList<String> suporte;
    private Image img;
    private boolean isConfirmado;
    private float influenciaTotal; // influencia 'recebida' atualmente
    private float influenciaNecessaria; // influencia necessaria para confirmar o evento
    private ArrayList<String> usersId; // guarda quem votou

    public Evento() {
        usersId = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getTipoEvt() {
        return tipoEvt;
    }

    public void setTipoEvt(String tipoEvt) {
        this.tipoEvt = tipoEvt;
    }


    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public boolean isConfirmado() {
        return isConfirmado;
    }

    public void setConfirmado(boolean confirmado) {
        isConfirmado = confirmado;
    }

    public float getInfluenciaTotal() {
        return influenciaTotal;
    }

    public void setInfluenciaTotal(float influenciaTotal) {
        this.influenciaTotal = influenciaTotal;
    }

    public float getInfluenciaNecessaria() {
        return influenciaNecessaria;
    }

    public void setInfluenciaNecessaria(float influenciaNecessaria) {
        this.influenciaNecessaria = influenciaNecessaria;
    }

    public ArrayList<String> getSuporte() {
        return suporte;
    }

    public void setSuporte(ArrayList<String> suporte) {
        this.suporte = suporte;
    }

    public ArrayList<String> getUsersId() {
        return usersId;
    }

    public void setUsersId(ArrayList<String> usersId) {
        this.usersId = usersId;
    }

    public void addUserId(String uid) {
        this.usersId.add(uid);
    }
}
