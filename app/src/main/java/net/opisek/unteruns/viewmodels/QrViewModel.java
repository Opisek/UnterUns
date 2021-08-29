package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.repositories.MainRepository;

public abstract class QrViewModel extends ViewModel {

    private long throttle = 0;

    public abstract void onQrScan(QrModel qr);

    public void interpretUUID(String input) {
        final long time = System.currentTimeMillis();
        /*if (throttle == 0) {         // uncomment to add delay before first scan
            throttle = time - 1500;
            return;
        }*/
        if (time - throttle >= 3000) {
            QrModel res = MainRepository.getInstance().getQrCode(input);
            if (res != null) {
                throttle = time;
                this.onQrScan(res);
            } else {
                setCorrectQr(false);
            }
        }
    }

    private MutableLiveData<Pair<Boolean, Integer>> correctQr;
    public MutableLiveData<Pair<Boolean, Integer>> getCorrectQr() {
        if (correctQr == null) {
            correctQr = new MutableLiveData<>();
            correctQr.setValue(new Pair(false, 0));
        }
        return correctQr;
    }
    public void setCorrectQr(Boolean isCorrect) {
        int index = 0;
        if (correctQr == null) correctQr = new MutableLiveData<>();
        else index = correctQr.getValue().second+1;
        correctQr.setValue(new Pair(isCorrect, index));
        if (!isCorrect) throttle = System.currentTimeMillis() + 3000;
    }
}
