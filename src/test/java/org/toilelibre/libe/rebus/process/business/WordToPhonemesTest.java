package org.toilelibre.libe.rebus.process.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;
import org.toilelibre.libe.rebus.objects.structs.Phoneme;

public class WordToPhonemesTest {

    @Test
    public void fileToPhonemes () throws IOException {
        new File ("src/main/resources/phonemes.txt").delete ();
        BufferedReader br =
                new BufferedReader (new InputStreamReader (new FileInputStream (new File ("src/main/resources/dictionary.txt"))));
        String line;
        FileOutputStream fos = new FileOutputStream (new File ("src/main/resources/phonemes.txt"));
        while ((line = br.readLine ()) != null){
            List<Phoneme> phonemes = WordToPhonemes.wordToPhonemes (line);
            if (phonemes.size () <= 6) {
                fos.write ((phonemes + ":" + line + "\r\n").toString ().getBytes ());
            }
        }
        br.close ();
        fos.close ();
    }
    
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
    
    @Test
    public void bicycle () {
        System.out.println(WordToPhonemes.wordToPhonemes ("bicycle"));
    }

}
