package net.opisek.unteruns.viewmodels;

import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RightWrongQrModel;

public class RightWrongQrViewModel extends QrViewModel {
    @Override
    public void onQrScan(QrModel qr) {
        if (!(qr instanceof RightWrongQrModel)) {
            setCorrectQr(false);
            return;
        }
        setCorrectQr(((RightWrongQrModel)qr).isRight);
    }
}
