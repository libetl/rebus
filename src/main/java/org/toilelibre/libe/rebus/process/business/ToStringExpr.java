package org.toilelibre.libe.rebus.process.business;

/**
 * Transforms one {negative, positive} path into a mathematical expression -
 * negative + positive
 * 
 * @author LiBe
 * 
 */
public class ToStringExpr {

  /**
   * @param negative
   *          letters to remove
   * @param positive
   *          letters to add
   * @return a mathematical expression
   */
  public static String buildPathText (final String negative,
      final String positive) {
    String currentPath = "";
    if (negative.length () > 0) {
      currentPath = "- " + negative;
    }
    if ((positive.length () > 0) && (negative.length () > 0)) {
      currentPath += " + " + positive;
    } else if (positive.length () > 0) {
      currentPath += "+ " + positive;
    }

    return currentPath;
  }
}
