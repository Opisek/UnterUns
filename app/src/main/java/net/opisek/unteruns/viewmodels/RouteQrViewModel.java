package net.opisek.unteruns.viewmodels;

import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.LocationModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.models.WaypointModel;
import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.views.QrActivity;

public class RouteQrViewModel extends QrViewModel {
    private WaypointModel myStop;

    public RouteQrViewModel() {
        myStop = MainRepository.getInstance().currentStop();
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
