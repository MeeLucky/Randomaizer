package com.example.randomaizer;

import java.util.Random;

class Randomayzer {
    private static Random rand = new Random();

    static int GetRandom(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }


        return rand.nextInt((max - min) + 1) + min;
    }

    static String GetCoinRandom(boolean isCoin) {
        if (isCoin)
            return rand.nextBoolean() ? "орёл" : "решка";
        else
            return rand.nextBoolean() ? "да" : "нет";
    }
}
