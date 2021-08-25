package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.WaypointModel;
import net.opisek.unteruns.repositories.GpsRepository;
import net.opisek.unteruns.repositories.MainRepository;

public class CompassViewModel extends ViewModel {

    MainRepository mainRepository;
    GpsRepository gpsRepository;

    WaypointModel nextWaypoint;
    WaypointModel nextStop;

    public CompassViewModel() {
        mainRepository = MainRepository.getInstance();
        mainRepository.pickRoute("Leicht");
        nextWaypoint = mainRepository.nextWaypoint();
        nextStop = mainRepository.nextStop();

        gpsRepository = GpsRepository.getInstance();
        gpsRepository.setGpsListener(new GpsRepository.GpsListener() {
            @Override
            public void onGpsUpdated() {
                getBearing().setValue(gpsRepository.getBearing(nextWaypoint.location.location));
                getDistanceWaypoint().setValue(gpsRepository.getDistance(nextWaypoint.location.location));
            }
        });
    }

    private MutableLiveData<Float> bearing;

    public MutableLiveData<Float> getBearing() {
        if (bearing == null) bearing = new MutableLiveData<Float>();
        bearing.setValue(GpsRepository.getInstance().getBearing(nextWaypoint.location.location));
        return bearing;
    }

    private MutableLiveData<Float> distanceWaypoint;

    public MutableLiveData<Float> getDistanceWaypoint() {
        if (distanceWaypoint == null) distanceWaypoint = new MutableLiveData<Float>();
        distanceWaypoint.setValue(GpsRepository.getInstance().getDistance(nextWaypoint.location.location));
        return distanceWaypoint;
    }

    private MutableLiveData<Float> distanceStop;

    public MutableLiveData<Float> getDistanceStop() {
        if (distanceStop == null) distanceStop = new MutableLiveData<Float>();
        return distanceStop;
    }

    private MutableLiveData<String> nameStop;

    public MutableLiveData<String> getNameStop() {
        if (nameStop == null) nameStop = new MutableLiveData<String>();
        nameStop.setValue(nextStop.location.name);
        return nameStop;
    }
}
