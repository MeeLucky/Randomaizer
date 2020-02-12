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

    static String GetWordRandom(int wordlen, int spread) {

//        char[] alphabetEN = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
//                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] ENglas = new char[]{'a', 'e', 'i', 'o', 'u', 'y'};
        char[] ENsogl = new char[]{'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k',
                'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'};

        StringBuilder word = new StringBuilder();

        //чтобы иногда начиналось с гласных
        if (rand.nextBoolean()) {
            char[] x = ENglas;
            ENglas = ENsogl;
            ENsogl = x;
        }
        //обязательно тут потому что выше они могу поеменяться местами
        int glen = ENglas.length;
        int slen = ENsogl.length;

        //разброс длины слова
        spread = rand.nextInt(spread);
        wordlen -= rand.nextBoolean() ? -spread : spread;

        //сборка слова
        for (int i = 0; i < wordlen; i++) {
            if (i % 2 == 0)
                word.append(ENsogl[Randomayzer.GetRandom(0, slen - 1)]);
            else
                word.append(ENglas[Randomayzer.GetRandom(0, glen - 1)]);
        }


        return word.toString();
    }
}
