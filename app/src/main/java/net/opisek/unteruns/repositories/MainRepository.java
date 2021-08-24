package net.opisek.unteruns.repositories;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.GpsModel;
import net.opisek.unteruns.models.LocationModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.models.WaypointModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.SortedMap;
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
        initializeQrCodes();
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
        locations = new HashMap<>();
        addLocation(new LocationModel("eingang",    "Waldeingang",          48.651969645503144d, 11.767538939916697d));
        addLocation(new LocationModel("kreuzung",   "Kreuzung",             48.655620681852994d, 11.768300862382462d));
        addLocation(new LocationModel("koloman",    "Kapelle St. Koloman",  48.661686910045980d, 11.756360174609688d));
        addLocation(new LocationModel("see",        "Kleiner See",          48.667600660245240d, 11.765604095755492d));
        addLocation(new LocationModel("kapellchen","Kleines Kapellchen",  48.660858476010720d, 11.771331706837545d));
        addLocation(new LocationModel("ggm",        "GGM",                  48.649638113113600d, 11.769270510916570d));

        addLocation(new LocationModel("eingang-kreuzung-1",48.655620681852994d, 11.768300862382462d));
    }

    public LocationModel getLocation(String id) {
        return locations.get(id);
    }

    // ROUTES

    private LinkedHashMap<String, RouteModel> routes;
    private void addRoute(RouteModel route) {
        routes.put(route.name, route);
    }

    private void initializeRoutes() {
        routes = new LinkedHashMap<>();
        addRoute(
                new RouteModel("Leicht", "4 Stunden",
                        new WaypointModel[]{
                                new WaypointModel(getLocation("ggm")),
                                new WaypointModel(getLocation("eingang")),
                                new WaypointModel(getLocation("koloman")),
                                new WaypointModel(getLocation("kapellchen"), Riddle.POSTCARDS),
                                new WaypointModel(getLocation("ggm"))
                        }
                )
        );
        addRoute(
                new RouteModel("Schwer", "24 Stunden",
                        new WaypointModel[]{
                            new WaypointModel(getLocation("ggm")),
                            new WaypointModel(getLocation("eingang")),
                            new WaypointModel(getLocation("eingang-kreuzung-1")),
                            new WaypointModel(getLocation("kreuzung")),
                            new WaypointModel(getLocation("koloman")),
                            new WaypointModel(getLocation("see")),
                            new WaypointModel(getLocation("kapellchen"), Riddle.POSTCARDS),
                            new WaypointModel(getLocation("ggm"))
                        }
                )
        );
    }

    public Pair<String, String>[] getRoutes() {
        String[] routeOptions = (String[])routes.keySet().toArray();
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        for (int i = 0; i < routeOptions.length; i++) {
            String name = routeOptions[i];
            result.add(new Pair<>(name, routes.get(name).description));
        }
        return (Pair<String, String>[])result.toArray();
    }

    private RouteModel route;
    private int routeProgress;

    public boolean pickRoute(String name) {
        if (!routes.containsKey(name)) return false;
        route = routes.get(name);
        routeProgress = 0;
        return true;
    }

    public WaypointModel nextWaypoint() {
        if (routeProgress >= route.waypoints.length-1) return null;
        return route.waypoints[routeProgress+1];
    }

    public WaypointModel nextStop() {
        WaypointModel result = null;
        for (int i = routeProgress+1; i < route.waypoints.length; i++) {
            if (!route.waypoints[i].intermediate) {
                result = route.waypoints[i];
                break;
            }
        }
        return result;
    }

    public WaypointModel currentStop() {
        return route.waypoints[routeProgress];
    }

    public void reachedWaypoint() {
        routeProgress++;
    }

    public void lostWaypoint() {
        routeProgress--;
    }

    // QR CODES

    private HashMap<UUID, QrModel> qrCodes;
    private void addQrCode(QrModel qr) {
        qrCodes.put(qr.id, qr);
    }

    private void initializeQrCodes() {
        qrCodes = new HashMap<>();
        addQrCode(new RouteQrModel("17064bd6-5ee2-4b52-b397-d4beac844307", getLocation("eingang")));
        addQrCode(new RouteQrModel("6e40c051-df1f-4964-b77d-647c1d02265e", getLocation("kreuzung")));
        addQrCode(new RouteQrModel("d47bac64-3020-4a25-a5d6-1b781ac07d8a", getLocation("koloman")));
        addQrCode(new RouteQrModel("11558407-bcd0-4dd0-9bf8-a2766f750a08", getLocation("see")));
        addQrCode(new RouteQrModel("a7eecb31-a434-4bdb-b1ea-615193db4921", getLocation("kappellchen")));
        addQrCode(new RouteQrModel("ba02ab0e-aa12-456a-b1d0-912341590b6d", getLocation("ggm")));
    }

    public QrModel getQrCode(String id) {
        return qrCodes.get(UUID.fromString(id));
    }

    // RIDDLES

    public enum Riddle {
        NONE,
        POSTCARDS
    }
}
