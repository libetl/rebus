package org.toilelibre.libe.rebus.process;

import java.util.ArrayList;
import java.util.List;

import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.structs.Phoneme;
import org.toilelibre.libe.rebus.objects.structs.Word;

public class RebusFromPhonemes {


    private static String getPhonemesAsString (List<Phoneme> phonemes, int start) {
        return phonemes.subList (start, phonemes.size ()).toString ().replace ('[', ' ').replace (']', ' ').trim ();
    }
    
    public static List<Word> getRebusFromSentence (Data data, String sentence) {
        List<Phoneme> phonemes = new ArrayList<Phoneme> ();
        String [] wordsAsString = sentence.toLowerCase ().split (" ");
        for (String wordAsString : wordsAsString) {
            phonemes.addAll (new Word (wordAsString).getPhonemes ());
        }
        return RebusFromPhonemes.getRebus (data, phonemes);
    }
    
    public static List<Word> getRebusFromSentence (Data data, List<Word> words) {
        List<Phoneme> phonemes = new ArrayList<Phoneme> ();
        for (Word word : words) {
            phonemes.addAll (word.getPhonemes ());
        }
        return RebusFromPhonemes.getRebus (data, phonemes);
    }
    
    public static List<Word> getRebus (Data data, List<Phoneme> phonemes) {
        int i = 0;
        List<Word> result = new ArrayList<Word> ();
        while (i < phonemes.size ()) {
            String phonemesAsString = getPhonemesAsString (phonemes, i);
            Word candidate = null;
            for (Word attempt : data.getWords ()) {
                String attemptPhonemes = getPhonemesAsString (attempt.getPhonemes(), 0);
                if (phonemesAsString.startsWith (attemptPhonemes) &&
                        (candidate == null || 
                        candidate.getPhonemes ().size () < attempt.getPhonemes ().size ())){
                    candidate = attempt;
                }
            }
            i+= candidate == null ? 1 : candidate.getPhonemes ().size ();
            if (candidate != null) {
                result.add (candidate);
            }
        }
        return result;
    }

}
