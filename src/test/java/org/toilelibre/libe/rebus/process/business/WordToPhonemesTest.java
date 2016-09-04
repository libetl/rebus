package org.toilelibre.libe.rebus.process.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.toilelibre.libe.rebus.init.PhonemesIndexer;
import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.structs.Phoneme;

public class WordToPhonemesTest {
    
    private static Data data;

    @BeforeClass
    public static void prepareData () {
        data = new Data ();
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
                fos.write ((phonemes + ":" + line + "\r\n").toString ().getBytes ());
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

}
