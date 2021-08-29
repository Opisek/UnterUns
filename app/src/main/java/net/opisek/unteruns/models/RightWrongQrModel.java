package net.opisek.unteruns.models;

public class RightWrongQrModel extends QrModel {
    public final boolean isRight;

    public RightWrongQrModel(String id, boolean isRight) {
        super(id);
        this.isRight = isRight;
    }
}
