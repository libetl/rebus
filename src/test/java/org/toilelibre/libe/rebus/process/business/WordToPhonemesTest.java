package org.toilelibre.libe.rebus.process.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.toilelibre.libe.rebus.model.context.Data;
import org.toilelibre.libe.rebus.model.phonemes.Phoneme;
import org.toilelibre.libe.rebus.model.phonemes.PhonemesIndexer;

public class WordToPhonemesTest {
    
    private static Data data;

    @BeforeClass
    public static void prepareData () {
        data = new Data ("src/main/resources/images.txt", 
                "src/main/resources/eng_phone.txt",
                "src/main/resources/phonemes.txt");
    }

    @Test
    public void fileToPhonemes () throws IOException {
        new File ("src/main/resources/phonemes.txt").delete ();
        BufferedReader br =
                new BufferedReader (new InputStreamReader (new FileInputStream (new File ("src/main/resources/dictionary.txt"))));
        String line;
        FileOutputStream fos = new FileOutputStream (new File ("src/main/resources/phonemes.txt"));
        while ((line = br.readLine ()) != null){
            List<Phoneme> phonemes = PhonemesIndexer.wordToPhonemes (data.getPhonemes (), line);
            if (phonemes.size () <= 6) {
                fos.write ((line + '=' + phonemes + "\n").toString ().getBytes ());
            }
        }
        br.close ();
        fos.close ();
    }
    
    @Test
    public void dolphin () {
        System.out.println(PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "dolphin"));
    }
    
    @Test
    public void miracle () {
        System.out.println(PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "miracle"));
    }
    
    @Test
    public void constitution () {
        System.out.println(PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "constitution"));
    }
    
    @Test
    public void squirrelled () {
        System.out.println(PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "squirrelled"));
    }
    
    @Test
    public void bicycle () {
        System.out.println(PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "bicycle"));
    }
    
    @Test
    public void capture () {
        List<Phoneme> phonemes = PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "capture");
        Assert.assertEquals ("/CH ER/", phonemes.get(3).getPhoneme ());
    }
    
    @Test
    public void suture () {
        System.out.println(PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "suture"));
    }
    
    @Test
    public void behoa () {
        Assert.assertEquals ("[/B IH/, /OW/]", PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "behoa").toString ());
    }
    
    @Test
    public void crick () {
        Assert.assertEquals ("[/K/, /R/, /IH/, /K/]", PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "crick").toString ());
    }
    
    @Test
    public void anchorable () {
        Assert.assertEquals ("[/AE/, /N/, /CH/, /AO R/, /AX B AX L/]", PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "anchorable").toString ());
    }
    
    @Test
    public void smutted () {
        Assert.assertEquals ("[/S/, /M/, /Y UW/, /T IH D/]", PhonemesIndexer.wordToPhonemes (data.getPhonemes (), "smutted").toString ());
    }

}
