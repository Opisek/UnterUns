package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.MorseQrModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;

public class MorseQrViewModel extends QrViewModel {

    private MutableLiveData<Pair<int[], Integer>> morseSequence;
    public MutableLiveData<Pair<int[], Integer>> getMorseSequence() {
        if (morseSequence == null) morseSequence = new MutableLiveData<>();
        return morseSequence;
    }

    private int dataID;
    private boolean morseLocked;

    public MorseQrViewModel() {
        morseLocked = false;
        dataID = 0;
    }

    public void playbackFinished() {
        morseLocked = false;
    }

    @Override
    public void onQrScan(QrModel qr) {
        if (!(qr instanceof MorseQrModel)) return;
        if (morseLocked) return;
        morseLocked = true;
        morseSequence.setValue(new Pair(((MorseQrModel) qr).morse.morse, ++dataID));
    }
}
