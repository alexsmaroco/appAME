package ufjf.ame;

import java.io.Serializable;

/**
 * Created by Alex on 15/07/2017.
 */

public class Local implements Serializable {
    private double latitude;
    private double longitude;
    private long time;

    public Local() {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
