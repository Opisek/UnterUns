package net.opisek.unteruns.models;

public class MorseModel {
    public final String id;
    public final String text;

    public MorseModel(String text) {
        this(text.toLowerCase().replaceAll(" ", "-"), text);
    }

    public MorseModel(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
