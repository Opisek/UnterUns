package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.models.WaypointModel;
import net.opisek.unteruns.repositories.MainRepository;

public class ContinueQrViewModel extends QrViewModel {
    private MutableLiveData<MainRepository.Riddle> riddle;
    public MutableLiveData<MainRepository.Riddle> getRiddle() {
        if (riddle == null) riddle = new MutableLiveData<>();
        return riddle;
    }

    @Override
    public void onQrScan(QrModel qr) {
        if (!(qr instanceof RouteQrModel)) return; // not a box code
        if (!MainRepository.getInstance().setStop(((RouteQrModel)qr).location)) return; // not on the picked route
        riddle.setValue(MainRepository.getInstance().currentStop().riddle);
    }
}
