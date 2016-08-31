package org.toilelibre.libe.rebus.init;

import java.util.Map;

import org.toilelibre.libe.rebus.objects.WordsFindStruct;
import org.toilelibre.libe.rebus.objects.structs.Word;
import org.toilelibre.libe.rebus.process.business.WordsFinder;

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
   * @see org.toilelibre.libe.rebus.objects.structs.FSM
   * @see org.toilelibre.libe.rebus.objects.WordsFindStruct
   */
  public static WordsFindStruct differencesToFSM (
      final Map<String, String> images) {
    final Object[] imagesSet = images.keySet ().toArray ();
    // The new fsm is created here.
    final WordsFindStruct fsm = new WordsFindStruct ();

    // We consider each pair of different words
    for (final Object word1S : imagesSet) {
      for (final Object word2S : imagesSet) {
        if (!word1S.equals (word2S)) {
          final Word word1 = new Word ((String) word1S);
          final Word word2 = new Word ((String) word2S);
          // These words come from the dictionary.
          // So they are keywords (there will be a ':' )
          word1.setKeyword (true);
          word2.setKeyword (true);
          // We add the pair
          WordsFinder.addPath (fsm, word1, word2);
        }

      }
    }
    return fsm;
  }

}
