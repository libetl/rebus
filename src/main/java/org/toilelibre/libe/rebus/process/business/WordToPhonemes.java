package org.toilelibre.libe.rebus.process.business;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.toilelibre.libe.rebus.objects.structs.Phoneme;

public class WordToPhonemes {
    
    private static final Phoneme EMPTY = new Phoneme ("//");
    private static Map<Pattern, Phoneme> PHONEMES = new HashMap<Pattern, Phoneme> ();

    static {
        try {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (new File ("src/main/resources/eng_phone.txt"))));
            String line = br.readLine ();
            while (line != null) {
                String key = line.substring (0, line.indexOf ('='));
                String value = line.substring (line.indexOf ('=') + 1);
                PHONEMES.put (Pattern.compile (key), EMPTY.getPhoneme ().equals (value) ? EMPTY : new Phoneme (value));
                line = br.readLine ();
            }
            br.close ();
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public static List<Phoneme> wordToPhonemes (String word) {
        boolean found = true;
        Pattern candidate = null;
        int lengthOfCandidate = 0;
        Phoneme [] phonemePerLetter = new Phoneme [word.length ()];
        boolean [] foundPerLetter = new boolean [word.length ()];
        boolean [] trues = new boolean [word.length ()];
        for (int i = 0; i < trues.length; i++)
            trues [i] = true;
        while (!Arrays.equals (trues, foundPerLetter) && found) {
            found = false;
            while (!found) {
                for (Entry<Pattern, Phoneme> entry : PHONEMES.entrySet ()) {
                    Matcher matcher = entry.getKey ().matcher (word);
                    while (matcher.find ()) {
                        if (matcher.end () - matcher.start () > lengthOfCandidate && notInAlreadyDiscoveredPatterns (foundPerLetter, matcher.start (), matcher.end ())) {
                            found = true;
                            lengthOfCandidate = matcher.end () - matcher.start ();
                            candidate = entry.getKey ();
                        }
                    }
                }
                if (found) {
                    Matcher matcher = candidate.matcher (word);
                    while (matcher.find ()) {
                        for (int i = matcher.start (1); i < matcher.end (1); i++) {
                            foundPerLetter [i] = true;
                            phonemePerLetter [i] = PHONEMES.get (candidate);
                        }
                    }
                }
                candidate = null;
                lengthOfCandidate = 0;
            }
        }
        return removeDuplicates (Arrays.asList (phonemePerLetter));
    }

    private static List<Phoneme> removeDuplicates (List<Phoneme> list) {
        List<Phoneme> result = new ArrayList<Phoneme> ();
        if (list.size () <= 1) {
            return list;
        }
        Phoneme lastOne = list.get (0);
        result.add (list.get (0));
        for (int i = 1; i < list.size (); i++) {
            if (list.get (i) != lastOne && list.get (i) != EMPTY) {
                result.add (list.get (i));
            }
            lastOne = list.get (i);
        }
        return result;
    }

    private static boolean notInAlreadyDiscoveredPatterns (boolean [] phonemePerLetter, int start, int end) {
        for (int i = start; i < end; i++) {
            if (phonemePerLetter [i] == true) {
                return false;
            }
        }
        return true;
    }
}
