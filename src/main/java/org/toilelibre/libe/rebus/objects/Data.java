package org.toilelibre.libe.rebus.objects;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.toilelibre.libe.rebus.init.ImageIndexer;
import org.toilelibre.libe.rebus.init.PhonemesIndexer;
import org.toilelibre.libe.rebus.init.WordIndexer;
import org.toilelibre.libe.rebus.objects.structs.FSM;
import org.toilelibre.libe.rebus.objects.structs.Pair;
import org.toilelibre.libe.rebus.objects.structs.Phoneme;
import org.toilelibre.libe.rebus.objects.structs.Word;

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
  private final Map<String, String> images;
  /**
   * Settings choosen by the user
   */
  private final Settings            settings;
  /**
   * This struct is the owner of all FSMs where we can find deltas between words.
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
  private final Map<Pattern, Phoneme> phonemes;

  private final Map<Word, List<Phoneme>> words;

  /**
   * Must be called at the beginning of the program. It loads an instance of the
   * map and the FSM.
   */
  public Data () {
    try {
      this.settings = new Settings ();
      this.images = ImageIndexer.index (Data.class.getClassLoader (),
          "images.txt");
      this.phonemes = PhonemesIndexer.index ("src/main/resources/eng_phone.txt");
      this.words = PhonemesIndexer.wordsToPhoneme (this, WordIndexer.index (this.images.keySet ()));
      this.sortedLettersTree = WordIndexer.differencesToFSM (this.words.keySet ());
      PhonemesIndexer.readFile (this, "src/main/resources/found.txt");
    } catch (final IOException e) {
      throw new RuntimeException (e);
    }
  }

  public Collection<Word> getWords () {
    return words.keySet ();
  }

  public Map<Word, List<Phoneme>> getWordsAndPhonemes () {
    return words;
  }

  public Map<Pattern, Phoneme> getPhonemes () {
    return phonemes;
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
}
