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
        RebusFromWordTest.data = LoadData.onlyEnglishRebusData ();
    }

    @Test
    public void miracleNotFound () {
        System.out.println (BuildRebusFromSentence.getRebus (RebusFromWordTest.data, PhonemesIndexer.wordToPhonemes (RebusFromWordTest.data, new Word ("miracle"))));
    }

    @Test
    public void aahzPharAahzKnow () {
        System.out.println (BuildRebusFromSentence.getWordsRebusFromSentence ("As far as i know"));
    }

    @Test
    public void mickFromMicrowave () {
        System.out.println (BuildRebusFromSentence.getRebus (RebusFromWordTest.data, PhonemesIndexer.wordToPhonemes (RebusFromWordTest.data, new Word ("microwave"))));
    }

}
