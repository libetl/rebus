package org.toilelibre.libe.rebus.process;

import java.util.ArrayList;
import java.util.List;

import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.structs.Word;

public class RebusFromWord {

    public static List<Word> getRebus (Data data, Word word) {
        int i = 0;
        List<Word> result = new ArrayList<Word> ();
        while (i < word.getPhonemes ().size ()) {
            Word candidate = null;
            for (Word attempt : data.getWords ()) {
                if (word.getPhonemesAsString (i).startsWith (
                        attempt.getPhonemesAsString (0)) &&
                        (candidate == null || 
                        candidate.getPhonemes ().size () < attempt.getPhonemes ().size ())){
                    candidate = attempt;
                }
            }
            i+= candidate.getPhonemes ().size ();
            result.add (candidate);
        }
        return result;
    }

}
