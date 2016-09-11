package org.toilelibre.libe.rebus.model.word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.toilelibre.libe.rebus.model.fsm.FSM;
import org.toilelibre.libe.rebus.model.fsm.FSMPopulator;
import org.toilelibre.libe.rebus.model.fsm.Pair;

/**
 * Here, we define the WordsFindStruct, which is the main structure for finding
 * a conversion. We start from our map coming from the ImageIndexer
 *
 * @see org.toilelibre.libe.rebus.init.ImageIndexer
 * @author LiBe
 *
 */
public class WordIndexer {

    /**
     * We put each pair of Words from the dictionary in a FSM
     *
     * @param images
     * @return a WordsFindStruct object
     * @see org.toilelibre.libe.rebus.model.fsm.FSM
     * @see org.toilelibre.libe.rebus.objects.WordsFindStruct
     */
    public static FSM<Set<Pair<Word, Word>>, String> differencesToFSM (final Collection<Word> words) {
        // The new fsm is created here.
        final FSM<Set<Pair<Word, Word>>, String> fsm = new FSM<Set<Pair<Word, Word>>, String> ();

        // We consider each pair of different words
        for (final Word word1 : words) {
            for (final Word word2 : words) {
                if (word1 != word2) {
                    // We add the pair
                    FSMPopulator.addPath (fsm, word1, WordsDifference.difference (word1, word2), word2, WordsDifference.difference (word2, word1));
                }

            }
        }
        return fsm;
    }

    public static List<Word> index (final Set<String> keySet) {
        final List<Word> result = new ArrayList<Word> (keySet.size ());
        for (final String wordAsString : keySet) {
            result.add (new Word (wordAsString));
        }
        return result;
    }

}
