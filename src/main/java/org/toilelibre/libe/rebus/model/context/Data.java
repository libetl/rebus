package org.toilelibre.libe.rebus.model.context;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.toilelibre.libe.rebus.model.fsm.FSM;
import org.toilelibre.libe.rebus.model.fsm.Pair;
import org.toilelibre.libe.rebus.model.images.ImageIndexer;
import org.toilelibre.libe.rebus.model.phonemes.Phoneme;
import org.toilelibre.libe.rebus.model.phonemes.PhonemesIndexer;
import org.toilelibre.libe.rebus.model.word.Word;
import org.toilelibre.libe.rebus.model.word.WordIndexer;

/**
 * Contains all concrete data of the program
 *
 * @author LiBe
 *
 */
public class Data {

    /**
     * Word <-> Picture Mapping
     */
    private final Map<String, String>                images;
    /**
     * Settings choosen by the user
     */
    private final Settings                           settings;
    /**
     * This struct is the owner of all FSMs where we can find deltas between
     * words.
     *
     * The main concept is that simple : replace a sample of letters by a
     * subtraction between two known words.
     *
     * exemple : replace "the" by elephant - plane
     *
     * A FSM is nothing but a forest with paths crossing each other.
     *
     * It is made of data and transitions to other FSMs.
     *
     */
    private final FSM<Set<Pair<Word, Word>>, String> sortedLettersTree;
    /**
     * Phonemes
     */
    private final Map<Pattern, Phoneme>              phonemes;
    /**
     * Patterns from phonemes as text
     */
    private final Map<String, Pattern>               phonemesFromText;

    private final Map<Word, List<Phoneme>>           words;

    /**
     * Must be called at the beginning of the program. It loads an instance of
     * the map and the FSM.
     */
    public Data (final String imageFileName, final String phoneticsFileName, final String phonemesFileName) {
        try {
            this.settings = new Settings ();
            this.images = ImageIndexer.index (Data.class.getClassLoader (), imageFileName);
            this.phonemes = PhonemesIndexer.index (phoneticsFileName);
            this.phonemesFromText = PhonemesIndexer.buildPatternsFromText (this.phonemes);
            this.words = PhonemesIndexer.wordsToPhoneme (this, WordIndexer.index (this.images.keySet ()));
            this.sortedLettersTree = WordIndexer.differencesToFSM (this.words.keySet ());
            PhonemesIndexer.readFile (this, phonemesFileName);
        } catch (final IOException e) {
            throw new RuntimeException (e);
        }
    }

    public Collection<Word> getWords () {
        return this.words.keySet ();
    }

    public Map<Word, List<Phoneme>> getWordsAndPhonemes () {
        return this.words;
    }

    public Map<Pattern, Phoneme> getPhonemes () {
        return this.phonemes;
    }

    public Map<String, String> getImages () {
        return this.images;
    }

    public Settings getSettings () {
        return this.settings;
    }

    public FSM<Set<Pair<Word, Word>>, String> getSortedLettersTree () {
        return this.sortedLettersTree;
    }

    public Map<String, Pattern> getPhonemesFromText () {
        return this.phonemesFromText;
    }
}
