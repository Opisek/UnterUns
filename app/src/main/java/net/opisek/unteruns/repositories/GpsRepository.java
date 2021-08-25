package net.opisek.unteruns.repositories;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import static android.content.ContentValues.TAG;

public class GpsRepository implements LocationListener {

    GpsListener listener;
    public interface GpsListener {
        public void onGpsUpdated();
    }
    public void setGpsListener(GpsListener listener) {
        this.listener = listener;
    }

    private static GpsRepository instance;
    private Location destination;
    private Location currentLocation;

    public static GpsRepository getInstance() {
        if (instance == null) instance = new GpsRepository();
        return instance;
    }

    private GpsRepository() {
        listener = null;
    }

    public float getBearing(Location l1) {
        return getBearing(currentLocation, l1);
    }

    public float getBearing(Location l1, Location l2) {
        if (l1 == null || l2 == null) return 0f;
        return l1.distanceTo(l2);
    }

    public float getDistance(Location l1) {
        return getDistance(currentLocation, l1);
    }

    public float getDistance(Location l1, Location l2) {
        if (l1 == null || l2 == null) return 0f;
        Log.v(TAG, "DISTANCE, lat1=" + l1.getLatitude() + " lat2=" + l2.getLatitude() + " lon1=" + l1.getLongitude() + " lon2=" + l2.getLongitude());
        return l1.distanceTo(l2);
    }

    @Override
    public void onLocationChanged(Location loc) {
        //Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + loc.getLatitude() + ", lon=" + loc.getLongitude());
        currentLocation = loc;
        if (listener != null) listener.onGpsUpdated();
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
