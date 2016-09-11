package org.toilelibre.libe.rebus.actions;

import org.toilelibre.libe.rebus.model.context.Data;

public class LoadData {

    private static Data INSTANCE;

    public static Data ensureEnglishDataLoaded () {
        if (LoadData.INSTANCE == null) {
            LoadData.INSTANCE = LoadData.buildEnglishData ();
        }
        return LoadData.INSTANCE;
    }

    public static Data getLoadedData () {
        return LoadData.INSTANCE;
    }

    private synchronized static Data buildEnglishData () {
        return new Data ("src/main/resources/images.txt", "src/main/resources/wordsForEquations.txt", "src/main/resources/eng_phone.txt", "src/main/resources/phonemes.txt");
    }

    public static Data onlyEnglishRebusData () {
        LoadData.INSTANCE = new Data ("", "src/main/resources/wordsForEquations.txt", "src/main/resources/eng_phone.txt", "src/main/resources/phonemes.txt");
        return LoadData.INSTANCE;
    }

    public static void setRemainingCost (final int value) {
        LoadData.INSTANCE.getSettings ().setRemainingCost (value);
    }

    public static void setLettersMissing (final int value) {
        LoadData.INSTANCE.getSettings ().setLettersMissing (value);
    }

    public static void setLengthDelta (final int value) {
        LoadData.INSTANCE.getSettings ().setLengthDelta (value);
    }

    public static String getPicture (final String picture) {
        return LoadData.INSTANCE.getImages ().get (picture);
    }
}
