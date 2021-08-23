package net.opisek.unteruns.models;

import net.opisek.unteruns.repositories.MainRepository.Riddle;

public class WaypointModel {
    public final LocationModel location;
    public final boolean intermediate;
    public final Riddle riddle;

    public WaypointModel(LocationModel location, Riddle riddle) {
        this.location = location;
        intermediate = location.name == null;
        this.riddle = riddle;
    }

    public WaypointModel(LocationModel location) {
        this(location, Riddle.NONE);
    }
}
