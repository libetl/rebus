package org.toilelibre.libe.rebus.actions;

import java.util.ArrayList;
import java.util.List;

import org.toilelibre.libe.rebus.model.context.Data;
import org.toilelibre.libe.rebus.model.phonemes.Phoneme;
import org.toilelibre.libe.rebus.model.phonemes.PhonemesIndexer;
import org.toilelibre.libe.rebus.model.word.Word;

public class BuildRebusFromSentence {

    private static String getPhonemesAsString (final List<Phoneme> phonemes, final int start) {
        return phonemes.subList (start, phonemes.size ()).toString ().replace ('[', ' ').replace (']', ' ').trim ();
    }

    public static String [] getRebusFromSentence (final Data data, final String sentence) {
        final List<Word> words = BuildRebusFromSentence.getWordsRebusFromSentence (data, sentence);
        final String [] result = new String [words.size ()];
        int i = 0;
        for (final Word word : words) {
            result [i++] = word.getWord ();
        }
        return result;
    }

    public static List<Word> getWordsRebusFromSentence (final Data data, final String sentence) {
        final List<Phoneme> phonemes = new ArrayList<Phoneme> ();
        final String [] wordsAsString = sentence.toLowerCase ().split (" ");
        for (final String wordAsString : wordsAsString) {
            phonemes.addAll (PhonemesIndexer.wordToPhonemes (data, new Word (wordAsString)));
        }
        return BuildRebusFromSentence.getRebus (data, phonemes);
    }

    static List<Word> getRebus (final Data data, final List<Phoneme> phonemes) {
        int i = 0;
        final List<Word> result = new ArrayList<Word> ();
        while (i < phonemes.size ()) {
            final String phonemesAsString = BuildRebusFromSentence.getPhonemesAsString (phonemes, i);
            Word candidate = null;
            List<Phoneme> candidatePhonemes = null;
            for (final Word attempt : data.getWords ()) {
                final List<Phoneme> attemptPhonemes = PhonemesIndexer.wordToPhonemes (data, attempt);
                final String attemptPhonemesAsString = BuildRebusFromSentence.getPhonemesAsString (attemptPhonemes, 0);
                if (phonemesAsString.startsWith (attemptPhonemesAsString) && (candidate == null || candidatePhonemes == null || candidatePhonemes.size () < attemptPhonemes.size ())) {
                    candidate = attempt;
                    candidatePhonemes = PhonemesIndexer.wordToPhonemes (data, candidate);
                }
            }
            i += candidate == null ? 1 : candidatePhonemes.size ();
            if (candidate != null) {
                result.add (candidate);
            }
        }
        return result;
    }

}
