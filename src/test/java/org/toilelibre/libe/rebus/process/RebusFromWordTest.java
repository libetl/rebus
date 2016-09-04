package org.toilelibre.libe.rebus.process;

import org.junit.BeforeClass;
import org.junit.Test;
import org.toilelibre.libe.rebus.init.PhonemesIndexer;
import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.structs.Word;
import org.toilelibre.libe.rebus.process.RebusFromPhonemes;

public class RebusFromWordTest {

    private static Data data;

    @BeforeClass
    public static void prepareData () {
        RebusFromWordTest.data = new Data ();
    }
    
    @Test
    public void miracleNotFound () {
        System.out.println (RebusFromPhonemes.getRebus (data, 
                PhonemesIndexer.wordToPhonemes (data, new Word ("miracle"))));
    }
    
    @Test
    public void sea () {
        System.out.println (RebusFromPhonemes.getRebus (data, 
                PhonemesIndexer.wordToPhonemes (data, new Word ("As far as i know"))));
    }
    @Test
    public void eyeAndWave () {
        System.out.println (RebusFromPhonemes.getRebus (data, 
                PhonemesIndexer.wordToPhonemes (data, new Word ("microwave"))));
    }

}
