package net.opisek.unteruns.models;

import java.util.UUID;

public class RouteQrModel extends QrModel {
    public final String name;

    public RouteQrModel(String id, String name) {
        super(id);
        this.name = name;
    }
}
