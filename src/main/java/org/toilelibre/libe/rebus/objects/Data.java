package org.toilelibre.libe.rebus.objects;

import java.io.IOException;
import java.util.Map;

import org.toilelibre.libe.rebus.init.ImageIndexer;
import org.toilelibre.libe.rebus.init.WordIndexer;

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
  private WordsFindStruct     wfs      = null;

  public Data () {
  }

  public Map<String, String> getImages () {
    return this.images;
  }

  public Settings getSettings () {
    return this.settings;
  }

  public WordsFindStruct getWfs () {
    return this.wfs;
  }

  /**
   * Must be called at the begining of the program. It loads an instance of the
   * map and the FSM.
   */
  public void init () {
    try {
      this.settings = new Settings ();
      this.images = ImageIndexer.index (Data.class.getClassLoader (),
          "images.txt");
      this.wfs = WordIndexer.differencesToFSM (this.images);
    } catch (final IOException e) {
      throw new RuntimeException (e);
    }
  }
}
