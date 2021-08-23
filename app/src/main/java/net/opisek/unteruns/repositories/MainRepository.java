package net.opisek.unteruns.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.GpsModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainRepository {

    // SINGLETON

    private static MainRepository instance;

    public static MainRepository getInstance() {
        if (instance == null) instance = new MainRepository();
        return instance;
    }
    private MainRepository() {
        initiateQrCodes();
    }

    // COMPASS

    private MutableLiveData<Float> compassRotation;

    public MutableLiveData<Float> getCompassRotation() {
        compassRotation = GpsModel.getInstance().getCompassRotation();
        return compassRotation;
    }

    // QR CODES

    private HashMap<UUID, QrModel> qrCodes;
    private void addQrCode(QrModel qr) {
        qrCodes.put(qr.id, qr);
    }

    private void initiateQrCodes() {
        qrCodes = new HashMap<>();
        addQrCode(new RouteQrModel("17064bd6-5ee2-4b52-b397-d4beac844307", "Waldeingang"));
        addQrCode(new RouteQrModel("6e40c051-df1f-4964-b77d-647c1d02265e", "Kreuzung"));
        addQrCode(new RouteQrModel("d47bac64-3020-4a25-a5d6-1b781ac07d8a", "Kapelle St. Koloman"));
        addQrCode(new RouteQrModel("11558407-bcd0-4dd0-9bf8-a2766f750a08", "Kleiner See"));
        addQrCode(new RouteQrModel("a7eecb31-a434-4bdb-b1ea-615193db4921", "Kleines Kappellchen"));
        addQrCode(new RouteQrModel("ba02ab0e-aa12-456a-b1d0-912341590b6d", "GGM"));
    }

    public QrModel getQrModel(String id) {
        return qrCodes.get(UUID.fromString(id));
    }
}
