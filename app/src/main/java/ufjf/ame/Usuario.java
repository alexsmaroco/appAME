package ufjf.ame;

/**
 * Created by Alex.
 */

public class Usuario {
    private String uid;
    private String name;
    private float influencia;
    private int codClasse; // 1 para usuario comum, 2 para super-usuario e 3 para admin
    private double latitude;
    private double longitude;


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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
