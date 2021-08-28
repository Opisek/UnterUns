package net.opisek.unteruns.repositories;

import java.util.ArrayList;

public class MorseRepository {
    private static MorseRepository instance;

    public static MorseRepository getInstance() {
        if (instance == null) instance = new MorseRepository();
        return instance;
    }

    public MorseRepository() {
        initializeMorseSequences();
    }

    private int[][] morseSequences;
    private int morseSequenceAddIndex;
    private void addMorseSequence(int[] Sequence) { morseSequences[++morseSequenceAddIndex] = Sequence; }

    private void initializeMorseSequences() {
        morseSequences = new int[26][];
        morseSequenceAddIndex = -1;
        addMorseSequence(new int[] {0, 1});          // A
        addMorseSequence(new int[] {1, 0, 0, 0});    // B
        addMorseSequence(new int[] {1, 0 ,1, 0});    // C
        addMorseSequence(new int[] {1, 0, 0});       // D
        addMorseSequence(new int[] {0});             // E
        addMorseSequence(new int[] {0, 0, 1, 0});    // F
        addMorseSequence(new int[] {1, 1, 0});       // G
        addMorseSequence(new int[] {0, 0, 0, 0});    // H
        addMorseSequence(new int[] {0, 0});          // I
        addMorseSequence(new int[] {0, 1, 1, 1});    // J
        addMorseSequence(new int[] {1, 0, 1});       // K
        addMorseSequence(new int[] {0, 1, 0, 0});    // L
        addMorseSequence(new int[] {1, 1});          // M
        addMorseSequence(new int[] {1, 0});          // N
        addMorseSequence(new int[] {1, 1, 1});       // O
        addMorseSequence(new int[] {0, 1, 1, 0});    // P
        addMorseSequence(new int[] {1, 1, 0, 1});    // Q
        addMorseSequence(new int[] {0, 1, 0});       // R
        addMorseSequence(new int[] {0, 0, 0});       // S
        addMorseSequence(new int[] {1});             // T
        addMorseSequence(new int[] {0, 0, 1});       // U
        addMorseSequence(new int[] {0, 0, 0, 1});    // V
        addMorseSequence(new int[] {0, 1, 1});       // W
        addMorseSequence(new int[] {1, 0, 0, 1});    // X
        addMorseSequence(new int[] {1, 0, 1, 1});    // Y
        addMorseSequence(new int[] {1, 1, 0, 0});    // Z
    }

    public int[] getMorseFromChar(char c) {
        if (c == ' ') return new int[] {3};
        if (c < 'a' || c > 'z') {
            if (c < 'A' || c > 'Z') {
                return new int[] {};
            } else {
                return morseSequences[c - 'A'];
            }
        } else {
            return morseSequences[c - 'a'];
        }
    }

    public int[] getMorseFromString(String s) {
        ArrayList<int[]> parts = new ArrayList<>();
        for (char c : s.toCharArray()) {
            int[] part = getMorseFromChar(c);
            if (part.length > 0) parts.add(part);
        }

        ArrayList<Integer> stream = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            int[] part = parts.get(i);
            if (part.length == 0) continue;
            if (i > 0 && part[0] != 3 && stream.get(stream.size()-1) != 3) stream.add(2);
            for (int j = 0; j < part.length; j++) stream.add(part[j]);
        }

        int[] output = new int[stream.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = stream.get(i).intValue();
        }

        return output;
    }
}
