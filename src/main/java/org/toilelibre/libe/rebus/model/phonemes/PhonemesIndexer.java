package org.toilelibre.libe.rebus.model.phonemes;

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

import org.toilelibre.libe.rebus.model.context.Data;
import org.toilelibre.libe.rebus.model.word.Word;

public class PhonemesIndexer {

    private static class PhonemeMatch {

        private final int                 lengthOfCandidateMatcher;
        private final int                 lengthOfCandidateGroup;
        private final Pattern             candidate;
        private final Matcher             foundMatcher;

        private static final PhonemeMatch NONE = new PhonemeMatch (0, 0, null, null);

        public PhonemeMatch (final int lengthOfCandidateMatcher, final int lengthOfCandidateGroup, final Pattern candidate, final Matcher foundMatcher) {
            this.lengthOfCandidateMatcher = lengthOfCandidateMatcher;
            this.lengthOfCandidateGroup = lengthOfCandidateGroup;
            this.candidate = candidate;
            this.foundMatcher = foundMatcher;
        }

    }

    public static Map<Pattern, Phoneme> index (final String phoneticRulesFilename) {
        final Map<String, Phoneme> allPhonemes = new HashMap<String, Phoneme> ();
        final Map<Pattern, Phoneme> phonemes = new HashMap<Pattern, Phoneme> ();
        try {
            final BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (new File (phoneticRulesFilename))));
            String line = br.readLine ();
            while (line != null) {
                final String key = line.substring (0, line.indexOf ('='));
                final String value = line.substring (line.indexOf ('=') + 1);
                if (!allPhonemes.containsKey (value)) {
                    allPhonemes.put (value, new Phoneme (value));
                }
                phonemes.put (Pattern.compile (key), Phoneme.EMPTY.getPhoneme ().equals (value) ? Phoneme.EMPTY : allPhonemes.get (value));
                line = br.readLine ();
            }
            br.close ();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException (e);
        } catch (final IOException e) {
            throw new RuntimeException (e);
        }
        return phonemes;
    }

    public static List<Phoneme> wordToPhonemes (final Data data, final Word word) {
        if (data.getWordsAndPhonemes () != null && data.getWordsAndPhonemes ().containsKey (word)) {
            return data.getWordsAndPhonemes ().get (word);
        }
        return PhonemesIndexer.wordToPhonemes (data.getPhonemes (), word.getWord ());
    }

    public static List<Phoneme> wordToPhonemes (final Map<Pattern, Phoneme> knownPhonemes, final String word) {
        final Phoneme [] result = new Phoneme [word.length ()];

        PhonemeMatch phonemeMatch;
        do {
            phonemeMatch = PhonemesIndexer.extractPhonemeFromMatch (knownPhonemes, PhonemesIndexer.searchBestCandidate (knownPhonemes, word, result), result);
        } while (PhonemesIndexer.hasMissingPhoneme (result) && phonemeMatch != PhonemeMatch.NONE);

        return PhonemesIndexer.removeDuplicates (Arrays.asList (result));
    }

    private static PhonemeMatch extractPhonemeFromMatch (final Map<Pattern, Phoneme> knownPhonemes, final PhonemeMatch phonemeMatch, final Phoneme [] target) {
        if (phonemeMatch.foundMatcher != null) {
            phonemeMatch.foundMatcher.reset ();
            while (phonemeMatch.foundMatcher.find ()) {
                if (PhonemesIndexer.inAlreadyDiscoveredPatterns (target, phonemeMatch.foundMatcher.start (), phonemeMatch.foundMatcher.end ())) {
                    continue;
                }
                for (int i = phonemeMatch.foundMatcher.start (1) ; i < phonemeMatch.foundMatcher.end (1) ; i++) {
                    target [i] = knownPhonemes.get (phonemeMatch.candidate);
                }
            }
        }
        return phonemeMatch;
    }

    private static boolean hasMissingPhoneme (final Phoneme [] phonemePerLetter) {
        return Arrays.toString (phonemePerLetter).contains ("null");
    }

    private static PhonemeMatch searchBestCandidate (final Map<Pattern, Phoneme> knownPhonemes, final String word, final Phoneme [] phonemePerLetter) {
        PhonemeMatch phonemeMatch = PhonemeMatch.NONE;
        for (final Entry<Pattern, Phoneme> entry : knownPhonemes.entrySet ()) {
            final Matcher matcher = entry.getKey ().matcher (word);
            while (matcher.find ()) {
                final int lengthOfCurrentMatcher = entry.getKey ().pattern ().contains (".*") ? 0 : matcher.end () - matcher.start ();
                final int lengthOfCurrentGroup = entry.getKey ().pattern ().contains (".*") ? 0 : matcher.end (1) - matcher.start (1);
                final boolean worthIt = lengthOfCurrentGroup > phonemeMatch.lengthOfCandidateGroup || lengthOfCurrentGroup == phonemeMatch.lengthOfCandidateGroup && lengthOfCurrentMatcher >= phonemeMatch.lengthOfCandidateMatcher;
                if (worthIt && !PhonemesIndexer.inAlreadyDiscoveredPatterns (phonemePerLetter, matcher.start (), matcher.end ())) {
                    phonemeMatch = new PhonemeMatch (matcher.end () - matcher.start (), matcher.end (1) - matcher.start (1), entry.getKey (), matcher);
                }
            }
        }
        return phonemeMatch;
    }

    private static List<Phoneme> removeDuplicates (final List<Phoneme> list) {
        final List<Phoneme> result = new ArrayList<Phoneme> ();
        if (list.size () <= 1) {
            return list;
        }
        Phoneme lastOne = list.get (0);
        result.add (list.get (0));
        for (int i = 1 ; i < list.size () ; i++) {
            if (list.get (i) != lastOne && list.get (i) != Phoneme.EMPTY) {
                if (list.get (i).startsWith (lastOne) && lastOne != Phoneme.EMPTY) {
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

    private static boolean inAlreadyDiscoveredPatterns (final Phoneme [] phonemePerLetter, final int start, final int end) {
        for (int i = start ; i < end ; i++) {
            if (phonemePerLetter [i] != null) {
                return true;
            }
        }
        return false;
    }

    public static Map<Word, List<Phoneme>> wordsToPhoneme (final Data data, final List<Word> index) {
        final Map<Word, List<Phoneme>> result = new HashMap<Word, List<Phoneme>> ();
        for (final Word word : index) {
            result.put (word, PhonemesIndexer.wordToPhonemes (data, word));
        }
        return result;
    }

    public static void readFile (final Data data, final String fileName) {
        final Properties properties = new Properties ();
        try {
            properties.load (new FileInputStream (new File (fileName)));
        } catch (final FileNotFoundException e) {
        } catch (final IOException e) {
            throw new RuntimeException (e);
        }
        for (final Entry<Object, Object> line : properties.entrySet ()) {
            PhonemesIndexer.saveWordFromProperty (data, line);
        }
    }

    private static void saveWordFromProperty (final Data data, final Entry<Object, Object> line) {

        final List<String> asString = Arrays.asList (line.getValue ().toString ().replace ('[', ' ').replace (']', ' ').trim ().split ("\\s*,\\s*"));
        final List<Phoneme> result = new ArrayList<Phoneme> (asString.size ());

        for (final String phoneme : asString) {
            result.add (data.getPhonemes ().get (data.getPhonemesFromText ().get (phoneme.trim ())));
        }

        final Word word = new Word (line.getKey ().toString ());
        data.getWordsAndPhonemes ().put (word, result);
    }

    public static Map<String, Pattern> buildPatternsFromText (final Map<Pattern, Phoneme> phonemes) {

        final Map<String, Pattern> phonemeAsTextToPattern = new HashMap<String, Pattern> ();

        for (final Entry<Pattern, Phoneme> phonemeEntry : phonemes.entrySet ()) {
            phonemeAsTextToPattern.put (phonemeEntry.getValue ().getPhoneme (), phonemeEntry.getKey ());
        }

        return phonemeAsTextToPattern;
    }
}
