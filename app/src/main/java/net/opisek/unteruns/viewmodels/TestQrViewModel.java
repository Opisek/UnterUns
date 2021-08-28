package net.opisek.unteruns.viewmodels;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.MorseQrModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.models.WaypointModel;
import net.opisek.unteruns.repositories.MainRepository;

import java.util.Arrays;

public class TestQrViewModel extends QrViewModel {

    private MutableLiveData<Pair<String, Long>> toast;
    public MutableLiveData<Pair<String, Long>> getToast() {
        if (toast == null) toast = new MutableLiveData<>();
        return toast;
    }

    @Override
    public void onQrScan(QrModel qr) {
        String desc;
        if ((qr instanceof RouteQrModel)) {
            String name = ((RouteQrModel)qr).location.name;
            desc = "Route QR: " + ((RouteQrModel)qr).location.name;
        } else if ((qr instanceof MorseQrModel)) {
            desc = "Morse QR: " + ((MorseQrModel)qr).morse.text;
            //desc = "Morse QR: " + Arrays.toString(((MorseQrModel)qr).morse.morse);
        } else {
            desc = "Unknown QR";
        }
        getToast().setValue(new Pair(desc, System.currentTimeMillis()));
    }
}
