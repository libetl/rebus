package org.toilelibre.libe.rebus.actions;

import org.toilelibre.libe.rebus.model.context.Data;

public class LoadData {

    private static Data INSTANCE;

    public static Data ensureEnglishDataLoaded () {
        if (LoadData.INSTANCE != null) {
            return LoadData.INSTANCE;
        }
        return buildEnglishData ();
    }
    
    public static Data getLoadedData () {
        return LoadData.INSTANCE;
    }
    
    private synchronized static Data buildEnglishData () {
        return new Data ("src/main/resources/images.txt", "src/main/resources/eng_phone.txt", "src/main/resources/phonemes.txt");

    }
}
