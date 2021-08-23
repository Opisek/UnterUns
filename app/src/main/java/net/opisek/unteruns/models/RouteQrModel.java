package net.opisek.unteruns.models;

import java.util.UUID;

public class RouteQrModel extends QrModel {
    public final LocationModel location;

    public RouteQrModel(String id, LocationModel location) {
        super(id);
        this.location = location;
    }
}
