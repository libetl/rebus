package org.toilelibre.libe.rebus.process.business;


import org.junit.Test;

public class WordToPhonemesTest {

    @Test
    public void dolphin () {
        System.out.println(WordToPhonemes.wordToPhonemes ("dolphin"));
    }
    
    @Test
    public void miracle () {
        System.out.println(WordToPhonemes.wordToPhonemes ("miracle"));
    }
    
    @Test
    public void constitution () {
        System.out.println(WordToPhonemes.wordToPhonemes ("constitution"));
    }
    
    @Test
    public void squirrelled () {
        System.out.println(WordToPhonemes.wordToPhonemes ("squirrelled"));
    }

}
