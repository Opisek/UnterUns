package net.opisek.unteruns.models;

import java.util.UUID;

public class QrModel {
    public final UUID id;

    public QrModel(String id) {
        this.id = UUID.fromString(id);
    }
}
