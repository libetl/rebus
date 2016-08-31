package org.toilelibre.libe.rebus.process.business;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helps to find the best path in the FSM to find the best family of
 * substractions with the lowest remaining cost
 * 
 * A path is a mathematical expression, and a "key" to pair of words (for
 * substractions) in the FSM.
 * 
 * @author LiBe
 * 
 */
public class PathEvaluator {

  /**
   * Chooses the path such as path - reference has a low cost
   * 
   * @param paths
   *          list of paths
   * @return the nearest from reference
   */
  public static String chooseBestPathDelta (final List<String> paths,
      final String reference) {
    if (paths.size () == 0) { return ""; }

    // The following algorithm is a classic method to get a minimum value
    // So there is no need to comment it
    int min = WordEvaluator.valueOfPath (PathEvaluator.remainingPath (
        paths.get (0), reference));
    String valMin = paths.get (0);
    for (int i = 1 ; i < paths.size () ; i++) {
      final int newVal = WordEvaluator.valueOfPath (PathEvaluator
          .remainingPath (paths.get (i), reference));
      if (newVal < min) {
        valMin = paths.get (i);
        min = newVal;
      }
    }
    return valMin;
  }

  /**
   * When a path is choosed, this method is used to find the remaining cost in a
   * math expr.
   * 
   * @param path1
   *          path before transformation
   * @param path2
   *          path after
   * @return remaining path
   */
  public static String remainingPath (final String path1, final String path2) {

    // We will parse the paths to pick up the negative and
    // positive parts
    final Pattern pattern = Pattern.compile (WordEvaluator.PATTERN_DIFF);
    final Matcher matcher = pattern.matcher (path1);
    final Matcher matcherRef = pattern.matcher (path2);
    matcher.find ();
    matcherRef.find ();
    final String negativePath1 = (matcher.group (2) == null ? "" : matcher
        .group (2));
    final String positivePath1 = (matcher.group (5) == null ? "" : matcher
        .group (5));
    final String negativePath2 = (matcherRef.group (2) == null ? ""
        : matcherRef.group (2));
    final String positivePath2 = (matcherRef.group (5) == null ? ""
        : matcherRef.group (5));

    // Here we do a path1 - path2.
    // Therefore negative of path1 is to be combined with
    // positive of path2 and positive of path1 is to be
    // combined with negative of path2
    String negativePath = negativePath1 + positivePath2;
    String positivePath = positivePath1 + negativePath2;
    String newPositive = "";

    // We try to simplify both paths. If a letter is
    // present on each side, we have to remove it.
    for (int i = 0 ; i < positivePath.length () ; i++) {
      if (negativePath.indexOf (positivePath.charAt (i)) != -1) {
        negativePath = negativePath.replaceFirst ("" + positivePath.charAt (i),
            "");
      } else {
        newPositive += positivePath.charAt (i);
      }
    }
    positivePath = newPositive;
    return ToStringExpr.buildPathText (negativePath, positivePath);
  }

}
