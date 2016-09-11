package org.toilelibre.libe.rebus.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.structs.Phoneme;
import org.toilelibre.libe.rebus.objects.structs.Word;

public class PhonemesIndexer {

    private static class PhonemeMatch {

        private final int     lengthOfCandidateMatcher;
        private final int     lengthOfCandidateGroup;
        private final Pattern candidate;
        private final Matcher foundMatcher;
        
        private static final PhonemeMatch NONE = new PhonemeMatch (0, 0, null, null);

        public PhonemeMatch (int lengthOfCandidateMatcher, int lengthOfCandidateGroup, Pattern candidate, Matcher foundMatcher) {
            this.lengthOfCandidateMatcher = lengthOfCandidateMatcher;
            this.lengthOfCandidateGroup = lengthOfCandidateGroup;
            this.candidate = candidate;
            this.foundMatcher = foundMatcher;
        }

    }

    public static Map<Pattern, Phoneme> index (String phoneticRulesFilename) {
        Map<String, Phoneme> allPhonemes = new HashMap<String, Phoneme> ();
        Map<Pattern, Phoneme> phonemes = new HashMap<Pattern, Phoneme> ();
        try {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (new File (phoneticRulesFilename))));
            String line = br.readLine ();
            while (line != null) {
                String key = line.substring (0, line.indexOf ('='));
                String value = line.substring (line.indexOf ('=') + 1);
                if (!allPhonemes.containsKey (value)) {
                    allPhonemes.put (value, new Phoneme (value));
                }
                phonemes.put (Pattern.compile (key), Phoneme.EMPTY.getPhoneme ().equals (value) ? Phoneme.EMPTY : allPhonemes.get (value));
                line = br.readLine ();
            }
            br.close ();
        } catch (FileNotFoundException e) {
            throw new RuntimeException (e);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        return phonemes;
    }

    public static List<Phoneme> wordToPhonemes (Data data, Word word) {
        if (data.getWordsAndPhonemes () != null && data.getWordsAndPhonemes ().containsKey (word)) {
            return data.getWordsAndPhonemes ().get (word);
        }
        return wordToPhonemes (data.getPhonemes (), word.getWord ());
    }

    public static List<Phoneme> wordToPhonemes (Map<Pattern, Phoneme> knownPhonemes, String word) {
        Phoneme [] result = new Phoneme [word.length ()];

        PhonemeMatch phonemeMatch;
        do {
            phonemeMatch = 
                    extractPhonemeFromMatch (knownPhonemes, 
                            searchBestCandidate (knownPhonemes, word, result), result);
        } while (hasMissingPhoneme (result) && phonemeMatch != PhonemeMatch.NONE);
        
        return removeDuplicates (Arrays.asList (result));
    }

    private static PhonemeMatch extractPhonemeFromMatch (Map<Pattern, Phoneme> knownPhonemes, PhonemeMatch phonemeMatch, Phoneme [] target) {
        if (phonemeMatch.foundMatcher != null) {
            phonemeMatch.foundMatcher.reset ();
            while (phonemeMatch.foundMatcher.find ()) {
                if (inAlreadyDiscoveredPatterns (target, phonemeMatch.foundMatcher.start (), phonemeMatch.foundMatcher.end ())) {
                    continue;
                }
                for (int i = phonemeMatch.foundMatcher.start (1) ; i < phonemeMatch.foundMatcher.end (1) ; i++) {
                    target [i] = knownPhonemes.get (phonemeMatch.candidate);
                }
            }
        }
        return phonemeMatch;
    }

    private static boolean hasMissingPhoneme (Phoneme [] phonemePerLetter) {
        return Arrays.toString (phonemePerLetter).contains ("null");
    }

    private static PhonemeMatch searchBestCandidate (Map<Pattern, Phoneme> knownPhonemes, String word, Phoneme [] phonemePerLetter) {
        PhonemeMatch phonemeMatch = PhonemeMatch.NONE;
        for (Entry<Pattern, Phoneme> entry : knownPhonemes.entrySet ()) {
            Matcher matcher = entry.getKey ().matcher (word);
            while (matcher.find ()) {
                int lengthOfCurrentMatcher = entry.getKey ().pattern ().contains (".*") ? 0 : matcher.end () - matcher.start ();
                int lengthOfCurrentGroup = entry.getKey ().pattern ().contains (".*") ? 0 : matcher.end (1) - matcher.start (1);
                boolean worthIt = lengthOfCurrentGroup > phonemeMatch.lengthOfCandidateGroup || (lengthOfCurrentGroup == phonemeMatch.lengthOfCandidateGroup && lengthOfCurrentMatcher >= phonemeMatch.lengthOfCandidateMatcher);
                if (worthIt && !inAlreadyDiscoveredPatterns (phonemePerLetter, matcher.start (), matcher.end ())) {
                    phonemeMatch = new PhonemeMatch (matcher.end () - matcher.start (), matcher.end (1) - matcher.start (1), entry.getKey (), matcher);
                }
            }
        }
        return phonemeMatch;
    }

    private static List<Phoneme> removeDuplicates (List<Phoneme> list) {
        List<Phoneme> result = new ArrayList<Phoneme> ();
        if (list.size () <= 1) {
            return list;
        }
        Phoneme lastOne = list.get (0);
        result.add (list.get (0));
        for (int i = 1 ; i < list.size () ; i++) {
            if (list.get (i) != lastOne && list.get (i) != Phoneme.EMPTY) {
                if (list.get (i).startsWith (lastOne)) {
                    result.remove (result.size () - 1);
                    result.add (list.get (i));
                } else if (!lastOne.endsWith (list.get (i))) {
                    result.add (list.get (i));
                }
            }
            lastOne = list.get (i);
        }
        return result;
    }

    private static boolean inAlreadyDiscoveredPatterns (Phoneme [] phonemePerLetter, int start, int end) {
        for (int i = start ; i < end ; i++) {
            if (phonemePerLetter [i] != null) {
                return true;
            }
        }
        return false;
    }

    public static Map<Word, List<Phoneme>> wordsToPhoneme (Data data, List<Word> index) {
        Map<Word, List<Phoneme>> result = new HashMap<Word, List<Phoneme>> ();
        for (Word word : index) {
            result.put (word, PhonemesIndexer.wordToPhonemes (data, word));
        }
        return result;
    }

    public static void readFile (Data data, String fileName) {
        Properties properties = new Properties ();
        try {
            properties.load (new FileInputStream (new File (fileName)));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        for (Entry<Object, Object> line : properties.entrySet ()) {
            saveWordFromProperty (data, line);
        }
    }

    private static void saveWordFromProperty (Data data, Entry<Object, Object> line) {

        List<String> asString = Arrays.asList (line.getValue ().toString ().replace ('[', ' ').replace (']', ' ').trim ().split("\\s*,\\s*"));
        List<Phoneme> result = new ArrayList<Phoneme> (asString.size ());
        
        for (String phoneme : asString) {
            result.add (data.getPhonemes ().get (data.getPhonemesFromText ().get (phoneme.trim ())));
        }

        Word word = new Word (line.getKey ().toString ());
        data.getWordsAndPhonemes ().put (word, result);
        data.getImages ().put (word.getWord (), line.getKey ().toString ());
    }

    public static Map<String, Pattern> buildPatternsFromText (Map<Pattern, Phoneme> phonemes) {

        Map<String, Pattern> phonemeAsTextToPattern = new HashMap<String, Pattern> ();
        
        for (Entry<Pattern, Phoneme>  phonemeEntry : phonemes.entrySet ()) {
            phonemeAsTextToPattern.put (phonemeEntry.getValue ().getPhoneme (), phonemeEntry.getKey ());
        }
        
        return phonemeAsTextToPattern;
    }
}
