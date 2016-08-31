package org.toilelibre.libe.rebus.objects;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.toilelibre.libe.rebus.init.ImageIndexer;
import org.toilelibre.libe.rebus.init.WordIndexer;
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
  private Map<String, String> images   = null;
  /**
   * Settings choosen by the user
   */
  private Settings            settings = null;
  /**
   * The FSM
   */
  private WordsFindStruct     sortedLettersTree = null;

  private List<Word> words = null;

  public Data () {
  }

  public List<Word> getWords () {
    return words;
}

public Map<String, String> getImages () {
    return this.images;
  }

  public Settings getSettings () {
    return this.settings;
  }

  public WordsFindStruct getSortedLettersTree () {
    return this.sortedLettersTree;
  }

  /**
   * Must be called at the beginning of the program. It loads an instance of the
   * map and the FSM.
   */
  public void init () {
    try {
      this.settings = new Settings ();
      this.images = ImageIndexer.index (Data.class.getClassLoader (),
          "images.txt");
      this.words = WordIndexer.index (this.images.keySet ());
      this.sortedLettersTree = WordIndexer.differencesToFSM (this.words);
    } catch (final IOException e) {
      throw new RuntimeException (e);
    }
  }
}
