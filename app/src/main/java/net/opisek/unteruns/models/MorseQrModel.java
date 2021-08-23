package net.opisek.unteruns.models;

import java.util.UUID;

public class MorseQrModel extends QrModel {
    public final String text;

    public MorseQrModel(String id, String text) {
        super(id);
        this.text = text;
    }
}
