package net.opisek.unteruns.models;

import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.repositories.MorseRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MorseModel {
    public final String id;
    public final String text;
    public final int[] morse;

    public MorseModel(String text) {
        this(text.toLowerCase().replaceAll(" ", "-"), text);
    }

    public MorseModel(String id, String text) {
        this.id = id;
        this.text = text;
        morse = MorseRepository.getInstance().getMorseFromString(text);
    }
}
