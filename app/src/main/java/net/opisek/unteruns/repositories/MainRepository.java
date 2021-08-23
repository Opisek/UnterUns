package net.opisek.unteruns.repositories;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.GpsModel;
import net.opisek.unteruns.models.LocationModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;

import java.util.HashMap;
import java.util.UUID;

public class MainRepository {

    // SINGLETON

    private static MainRepository instance;

    public static MainRepository getInstance() {
        if (instance == null) instance = new MainRepository();
        return instance;
    }
    private MainRepository() {
        initiateLocations();
        initializeRoutes();
        initiateQrCodes();
    }

    // COMPASS

    private MutableLiveData<Float> compassRotation;

    public MutableLiveData<Float> getCompassRotation() {
        compassRotation = GpsModel.getInstance().getCompassRotation();
        return compassRotation;
    }

    // LOCATIONS

    private HashMap<String, LocationModel> locations;
    private void addLocation(LocationModel loc) {
        locations.put(loc.id, loc);
    }

    private void initiateLocations() {
        addLocation(new LocationModel("eingang",    "Waldeingang",          48.651969645503144d, 11.767538939916697d));
        addLocation(new LocationModel("kreuzung",   "Kreuzung",             48.655620681852994d, 11.768300862382462d));
        addLocation(new LocationModel("koloman",    "Kapelle St. Koloman",  48.661686910045980d, 11.756360174609688d));
        addLocation(new LocationModel("see",        "Kleiner See",          48.667600660245240d, 11.765604095755492d));
        addLocation(new LocationModel("kappellchen","Kleines Kappellchen",  48.660858476010720d, 11.771331706837545d));
        addLocation(new LocationModel("ggm",        "GGM",                  48.649638113113600d, 11.769270510916570d));
    }

    public LocationModel getLoc(String id) {
        return locations.get(id);
    }

    // ROUTES

    private HashMap<String, LocationModel[]> routes;

    private void initializeRoutes() {
        routes.put("easy", new LocationModel[] {
                getLoc("ggm"),
                getLoc("eingang"),
                getLoc("koloman"),
                getLoc("kapellchen"),
                getLoc("ggm")
        });
        routes.put("hard", new LocationModel[] {
                getLoc("ggm"),
                getLoc("eingang"),
                getLoc("kreuzung"),
                getLoc("koloman"),
                getLoc("see"),
                getLoc("kapellchen"),
                getLoc("ggm")
        });
    }

    // QR CODES

    private HashMap<UUID, QrModel> qrCodes;
    private void addQrCode(QrModel qr) {
        qrCodes.put(qr.id, qr);
    }

    private void initiateQrCodes() {
        qrCodes = new HashMap<>();
        addQrCode(new RouteQrModel("17064bd6-5ee2-4b52-b397-d4beac844307", getLoc("eingang")));
        addQrCode(new RouteQrModel("6e40c051-df1f-4964-b77d-647c1d02265e", getLoc("kreuzung")));
        addQrCode(new RouteQrModel("d47bac64-3020-4a25-a5d6-1b781ac07d8a", getLoc("koloman")));
        addQrCode(new RouteQrModel("11558407-bcd0-4dd0-9bf8-a2766f750a08", getLoc("see")));
        addQrCode(new RouteQrModel("a7eecb31-a434-4bdb-b1ea-615193db4921", getLoc("kappellchen")));
        addQrCode(new RouteQrModel("ba02ab0e-aa12-456a-b1d0-912341590b6d", getLoc("ggm")));
    }

    public QrModel getQr(String id) {
        return qrCodes.get(UUID.fromString(id));
    }
}
