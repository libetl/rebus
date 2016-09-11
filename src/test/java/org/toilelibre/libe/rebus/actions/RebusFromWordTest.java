package org.toilelibre.libe.rebus.actions;

import org.junit.BeforeClass;
import org.junit.Test;
import org.toilelibre.libe.rebus.model.context.Data;
import org.toilelibre.libe.rebus.model.phonemes.PhonemesIndexer;
import org.toilelibre.libe.rebus.model.word.Word;

public class RebusFromWordTest {

    private static Data data;

    @BeforeClass
    public static void prepareData () {
        RebusFromWordTest.data = new Data ("src/main/resources/images.txt", 
                "src/main/resources/eng_phone.txt",
                "src/main/resources/phonemes.txt");
    }
    
    @Test
    public void miracleNotFound () {
        System.out.println (BuildRebusFromSentence.getRebus (data, 
                PhonemesIndexer.wordToPhonemes (data, new Word ("miracle"))));
    }
    
    @Test
    public void sea () {
        System.out.println (BuildRebusFromSentence.getRebusFromSentence (data, "As far as i know"));
    }
    @Test
    public void eyeAndWave () {
        System.out.println (BuildRebusFromSentence.getRebus (data, 
                PhonemesIndexer.wordToPhonemes (data, new Word ("microwave"))));
    }

}
