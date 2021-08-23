package net.opisek.unteruns.models;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;

import androidx.lifecycle.MutableLiveData;

import static android.content.Context.SENSOR_SERVICE;

public class GpsModel implements LocationListener {

    private static GpsModel instance;
    private Location destination;
    private Location currentLocation;

    public static GpsModel getInstance() {
        if (instance == null) instance = new GpsModel();
        return instance;
    }

    public GpsModel() {
        compassRotation = new MutableLiveData<>();
        compassRotation.setValue(0f);
    }

    public void setDestination(Location loc) {
        destination = loc;
        setCompassRotation();
    }

    private MutableLiveData<Float> compassRotation;

    public MutableLiveData<Float> getCompassRotation() {
        return compassRotation;
    }

    private void setCompassRotation() {

    }

    @Override
    public void onLocationChanged(Location loc) {
        currentLocation = loc;
        setCompassRotation();
    }
}
