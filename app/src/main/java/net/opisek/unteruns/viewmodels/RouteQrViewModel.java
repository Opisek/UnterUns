package net.opisek.unteruns.viewmodels;

import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.LocationModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.models.WaypointModel;
import net.opisek.unteruns.repositories.GpsRepository;
import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.views.QrActivity;

public class RouteQrViewModel extends QrViewModel {
    private WaypointModel myStop;
    private MainRepository mainRepository;
    private GpsRepository gpsRepository;

    private long activityStartTimestamp;

    public RouteQrViewModel() {
        activityStartTimestamp = System.currentTimeMillis();

        mainRepository = MainRepository.getInstance();
        myStop = mainRepository.currentStop();

        gpsRepository = GpsRepository.getInstance();
        gpsRepository.setGpsListener(new GpsRepository.GpsListener() {
            @Override
            public void onGpsUpdated() {
                if (gpsRepository.getDistance(myStop.location.location) >= 15f && System.currentTimeMillis() - activityStartTimestamp > 1000) {
                    mainRepository.lostWaypoint();
                    lostWaypoint.setValue(true);
                }
            }
        });
        Log.v("Halo", myStop.location.name);
    }

    private MutableLiveData<Boolean> lostWaypoint;
    public MutableLiveData<Boolean> getLostWaypoint() {
        if(lostWaypoint == null) {
            lostWaypoint = new MutableLiveData<>();
            lostWaypoint.setValue(false);
        }
        return lostWaypoint;
    }

    private MutableLiveData<MainRepository.Riddle> riddle;
    public MutableLiveData<MainRepository.Riddle> getRiddle() {
        if (riddle == null) riddle = new MutableLiveData<>();
        return riddle;
    }

    @Override
    public void onQrScan(QrModel qr) {
        if (!(qr instanceof RouteQrModel)) return; // not a box code
        if (((RouteQrModel)qr).location != myStop.location) return; // wrong box
        riddle.setValue(myStop.riddle);
    }
}
