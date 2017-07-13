package ufjf.ame;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPS implements LocationListener {

    private LocationManager lm;
    private Location loc;

    public GPS(LocationManager locm) {
        loc = null;
        lm = locm;
        try {
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, this);
                loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public double[] getLocalAtual() {
        if(loc == null) {
            return new double[]{-1,-1};
        }

        return new double[] {loc.getLatitude(),loc.getLongitude()};
    }

    @Override
    public void onLocationChanged(Location location) {
        this.loc = location;
        System.out.println(loc.getLatitude()+ "  " + loc.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
