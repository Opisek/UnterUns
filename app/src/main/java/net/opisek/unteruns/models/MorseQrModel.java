package net.opisek.unteruns.models;

import java.util.UUID;

public class MorseQrModel extends QrModel {
    public final MorseModel morse;

    public MorseQrModel(String id, MorseModel morse) {
        super(id);
        this.morse = morse;
    }
}
