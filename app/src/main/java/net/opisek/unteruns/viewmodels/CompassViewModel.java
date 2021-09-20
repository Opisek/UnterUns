package net.opisek.unteruns.viewmodels;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.WaypointModel;
import net.opisek.unteruns.repositories.GpsRepository;
import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.views.QrActivity;

import static androidx.core.content.ContextCompat.startActivity;

public class CompassViewModel extends ViewModel {

    private MainRepository mainRepository;
    private GpsRepository gpsRepository;

    private WaypointModel nextWaypoint;
    private WaypointModel nextStop;

    private long activityStartTimestamp;

    private boolean compassActive;

    public CompassViewModel() {
        activityStartTimestamp = System.currentTimeMillis();

        mainRepository = MainRepository.getInstance();
        nextWaypoint = mainRepository.nextWaypoint(); // clear?
        nextStop = mainRepository.nextStop(); // clear?

        compassActive = true;
        gpsRepository = GpsRepository.getInstance();
        gpsRepository.setGpsListener(new GpsRepository.GpsListener() {
            @Override
            public void onGpsUpdated() {
                if (compassActive) {
                    boolean isFinalWaypoint = nextWaypoint == nextStop;

                    getBearing().setValue(gpsRepository.getBearing(nextWaypoint.location.location));

                    float dist = gpsRepository.getDistance(nextWaypoint.location.location);
                    getDistanceWaypoint().setValue(dist);
                    float distStop = dist;
                    if (!isFinalWaypoint) distStop += mainRepository.getDistanceUntilNextStop();
                    getDistanceStop().setValue(distStop);

                    if (((dist <= mainRepository.distanceWaypointFind() && !isFinalWaypoint) || distStop <= mainRepository.distanceStopFind()) && System.currentTimeMillis() - activityStartTimestamp > 1000) {
                        mainRepository.reachedWaypoint();
                        if (nextWaypoint == nextStop) {
                            getStopReached().setValue(true);
                            compassActive = false;
                        } else {
                            nextWaypoint = mainRepository.nextWaypoint();
                            setProgress(mainRepository.getRouteProgress());
                        }
                    }
                }
            }
        });
    }

    private MutableLiveData<Float> bearing;
    public MutableLiveData<Float> getBearing() {
        if (bearing == null) bearing = new MutableLiveData<Float>();
        return bearing;
    }

    private MutableLiveData<Float> distanceWaypoint;
    public MutableLiveData<Float> getDistanceWaypoint() {
        if (distanceWaypoint == null) distanceWaypoint = new MutableLiveData<Float>();
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

    private MutableLiveData<Boolean> stopReached;
    public MutableLiveData<Boolean> getStopReached() {
        if (stopReached == null) {
            stopReached = new MutableLiveData<Boolean>();
            stopReached.setValue(false);
        }
        return stopReached;
    }

    private MutableLiveData<Integer> progress;
    public MutableLiveData<Integer> getProgress() {
        if (progress == null) {
            progress = new MutableLiveData<>();
            progress.setValue(mainRepository.getRouteProgress());
        }
        return progress;
    }
    private void setProgress(Integer value) {
        if (progress == null) progress = new MutableLiveData<>();
        progress.setValue(value);
    }
}
