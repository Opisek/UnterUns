package net.opisek.unteruns.models;

public class RouteModel {
    public final WaypointModel[] waypoints;
    public final String name;
    public final String description;

    public RouteModel(String name, String description, WaypointModel[] waypoints) {
        this.name = name;
        this.description = description;
        this.waypoints = waypoints;
    }
}
