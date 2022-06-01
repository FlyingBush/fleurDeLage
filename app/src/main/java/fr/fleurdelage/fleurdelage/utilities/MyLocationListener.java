package fr.fleurdelage.fleurdelage.utilities;

import android.location.Location;
import android.location.LocationListener;

public class MyLocationListener implements LocationListener {
    private Location location;
    private double latitude;
    private double longitude;

    public Location getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onLocationChanged(Location loc) {
        this.location = loc;
        this.latitude = loc.getLatitude();
        this.longitude = loc.getLongitude();
        System.out.println(loc.getAccuracy());
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}
}
