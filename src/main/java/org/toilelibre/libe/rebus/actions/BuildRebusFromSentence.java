package org.toilelibre.libe.rebus.actions;

import java.util.ArrayList;
import java.util.List;

import org.toilelibre.libe.rebus.model.context.Data;
import org.toilelibre.libe.rebus.model.phonemes.Phoneme;
import org.toilelibre.libe.rebus.model.phonemes.PhonemesIndexer;
import org.toilelibre.libe.rebus.model.word.Word;

public class BuildRebusFromSentence {


    private static String getPhonemesAsString (List<Phoneme> phonemes, int start) {
        return phonemes.subList (start, phonemes.size ()).toString ().replace ('[', ' ').replace (']', ' ').trim ();
    }
    
    public static List<Word> getRebusFromSentence (Data data, String sentence) {
        List<Phoneme> phonemes = new ArrayList<Phoneme> ();
        String [] wordsAsString = sentence.toLowerCase ().split (" ");
        for (String wordAsString : wordsAsString) {
            phonemes.addAll (PhonemesIndexer.wordToPhonemes (data, new Word (wordAsString)));
        }
        return BuildRebusFromSentence.getRebus (data, phonemes);
    }
    
    static List<Word> getRebus (Data data, List<Phoneme> phonemes) {
        int i = 0;
        List<Word> result = new ArrayList<Word> ();
        while (i < phonemes.size ()) {
            String phonemesAsString = getPhonemesAsString (phonemes, i);
            Word candidate = null;
            List<Phoneme> candidatePhonemes = null;
            for (Word attempt : data.getWords ()) {
                List<Phoneme> attemptPhonemes = PhonemesIndexer.wordToPhonemes (data, attempt);
                String attemptPhonemesAsString = getPhonemesAsString (attemptPhonemes, 0);
                if (phonemesAsString.startsWith (attemptPhonemesAsString) &&
                        (candidate == null || candidatePhonemes == null ||
                                candidatePhonemes.size () < attemptPhonemes.size ())){
                    candidate = attempt;
                    candidatePhonemes = PhonemesIndexer.wordToPhonemes (data, candidate);
                }
            }
            i+= candidate == null ? 1 : candidatePhonemes.size ();
            if (candidate != null) {
                result.add (candidate);
            }
        }
        return result;
    }

}
