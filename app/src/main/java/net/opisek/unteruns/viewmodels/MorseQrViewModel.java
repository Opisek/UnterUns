package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.MorseQrModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;

public class MorseQrViewModel extends QrViewModel {

    private MutableLiveData<Pair<String, Long>> toast;
    public MutableLiveData<Pair<String, Long>> getToast() {
        if (toast == null) toast = new MutableLiveData<>();
        return toast;
    }

    @Override
    public void onQrScan(QrModel qr) {
        if (!(qr instanceof MorseQrModel)) return;

    }
}
