package ufjf.ame;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Alex on 14/07/2017.
 */

public class Evento implements Serializable{
    private String id;
    private Local loc;
    private String tipoEvt;
    private ArrayList<String> suporte;
    private Bitmap img;
    private float influenciaTotal; // influencia 'recebida' atualmente
    private float influenciaNecessaria; // influencia necessaria para confirmar o evento
    private ArrayList<String> usersId; // guarda quem votou
    private ArrayList<String> usersName;

    public Evento() {
        usersId = new ArrayList<String>();
        usersName = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Local getLoc() {
        return loc;
    }

    public void setLoc(Local loc) {
        this.loc = loc;
    }

    public String getTipoEvt() {
        return tipoEvt;
    }

    public void setTipoEvt(String tipoEvt) {
        this.tipoEvt = tipoEvt;
    }


    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
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

    public ArrayList<String> getUsersName() {
        return usersName;
    }

    public void setUsersName(ArrayList<String> usersName) {
        this.usersName = usersName;
    }

    public void addUserId(String uid) {
        this.usersId.add(uid);
    }

    public void addUserName(String name) { this.usersName.add(name); }

    public boolean isConfirmado() {
        return (this.influenciaTotal >= this.influenciaNecessaria);
    }
}
