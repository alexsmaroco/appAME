package ufjf.ame;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Alex.
 */

public class Usuario implements Serializable {
    private String uid;
    private String name;
    private float influencia;
    private int codClasse; // 1 para usuario comum, 2 para super-usuario e 3 para admin


    public Usuario() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getInfluencia() {
        return influencia;
    }

    public void setInfluencia(float influencia) {
        this.influencia = influencia;
    }

    public int getCodClasse() {
        return codClasse;
    }

    public void setCodClasse(int codClasse) {
        this.codClasse = codClasse;
    }

}
