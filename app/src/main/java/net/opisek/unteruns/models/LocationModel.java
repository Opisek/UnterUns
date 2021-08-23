package net.opisek.unteruns.models;

import android.location.Location;

public class LocationModel {
    public final String id;
    public final String name;
    public final Location location;
    public final boolean intermediate;

    public LocationModel(String id, double longitude, double latitude) {
        this.id = id;
        this.name = null;
        location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        intermediate = true;
    }

    public LocationModel(String id, String name, double longitude, double latitude) { // + RiddleActivity?
        this.id = id;
        this.name = name;
        location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        intermediate = false;
    }
}