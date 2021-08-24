package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.repositories.MainRepository;

public abstract class QrViewModel extends ViewModel {

    private long throttle = 0;

    public abstract void onQrScan(QrModel qr);

    public void interpretUUID(String input) {
        QrModel res = MainRepository.getInstance().getQrCode(input);
        if (res != null) {
            final long time = System.currentTimeMillis();
            if (time - throttle >= 3000) {
                throttle = time;
                this.onQrScan(res);
            }
        }
    }
}
