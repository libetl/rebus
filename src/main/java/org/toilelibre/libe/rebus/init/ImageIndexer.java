package org.toilelibre.libe.rebus.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class transform a "images.txt" input into a relation between words and
 * pictures. It is useful for getting our dictionary and to display the pictures
 * in the JPanel
 * 
 * @author LiBe
 * 
 */
public class ImageIndexer {

  /**
   * This regexp is parsing each line
   */
  private static final String PARSE_REGEX     = "([^\t]+)[\t]+(.*)";
  /**
   * Sometimes several words are matching a picture. We have to read each word.
   */
  private static final String SPLIT_LIST_CHAR = " ";

  /**
   * Loads a "images.txt" in the class loader, then transform the input into a
   * Map<String, String>
   * 
   * @param cl
   *          class loader
   * @param filename
   *          file name
   * @return Map. The keys are the words and the values are the pictures
   * @throws IOException
   *           if the file could not be read
   */
  public static Map<String, String> index (final ClassLoader cl,
      final String filename) throws IOException {
    final Map<String, String> indexMap = new HashMap<String, String> ();

    // This method is supposed to deal with reading the file
    final InputStreamReader isr = new InputStreamReader (
        cl.getResourceAsStream (filename));
    final BufferedReader br = new BufferedReader (isr);

    String line = br.readLine ();

    while (line != null) {
      // We apply the pattern here
      final Matcher matcher = Pattern.compile (ImageIndexer.PARSE_REGEX)
          .matcher (line);
      matcher.find ();
      // We find the associations
      final String[] names = ImageIndexer.parseWords (matcher);
      final String image = ImageIndexer.parseImage (matcher);
      for (final String name : names) {
        indexMap.put (name, image);
      }
      line = br.readLine ();
    }

    return indexMap;
  }

  /**
   * Gets the name of the picture
   * 
   * @param matcher
   *          matcher
   * @return the name
   */
  private static String parseImage (final Matcher matcher) {
    return matcher.group (1);
  }

  /**
   * Gets the words
   * 
   * @param matcher
   *          matcher
   * @return the words
   */
  private static String[] parseWords (final Matcher matcher) {
    return matcher.group (2).split (ImageIndexer.SPLIT_LIST_CHAR);
  }

}
