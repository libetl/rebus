package org.toilelibre.libe.rebus.process;

import java.util.Set;

import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.Settings;
import org.toilelibre.libe.rebus.objects.structs.FSM;
import org.toilelibre.libe.rebus.objects.structs.Pair;
import org.toilelibre.libe.rebus.objects.structs.Word;
import org.toilelibre.libe.rebus.process.business.WordEvaluator;
import org.toilelibre.libe.rebus.process.business.WordsFinder;

public class EquationFromWord {

  /**
   * Returns the cost of an equation (or a rebus)
   * 
   * @param equation
   *          the equation to consider
   * @return the cost
   */
  public static int getCost (final String equation) {
    return WordEvaluator.valueOfNotTranslated (equation);
  }

  /**
   * Transforms a word into an equation (an equation is also named a rebus)
   * 
   * @param data
   *          the data contains a FSM, the pictures map and the settings
   * @param word
   *          the word to transform
   * @return an equation
   */
  public static String getEquation (final Data data, final Word word) {
    return EquationFromWord.getEquation (data.getSettings (), data.getSortedLettersTree (),
        word);
  }

  /**
   * Transforms a word into an equation (an equation is also named a rebus)
   * 
   * @param settings
   *          settings choosen by the user
   * @param wfs
   *          the FSM
   * @param word
   *          the word
   * @return an equation
   */
  private static String getEquation (final Settings settings,
      final FSM<Set<Pair<Word, Word>>, String> wfs, final Word word) {

    // A Word can be seen as a remainder, let's put a + in front of it.
    String equation = "+ " + word.getWord ();

    // We need to find a good substraction
    String[] result = WordsFinder.findNearestPair (wfs, equation, word,
        settings.getLettersMissing (), settings.getLengthDelta ());

    // we replace the word by that substraction
    equation = equation.replace ("+ " + word.getWord (), result[0]);

    if (equation.startsWith ("+ (")) {
      // it worked, we can now remove the first +
      equation = equation.substring (3, equation.length () - 1);
    } else if (equation.startsWith ("+ ")) {
      // it did not work, maybe the settings were too stricts
      // we will soon bypass the loop and the word will appear
      // untouched.
      equation = equation.substring (2);
    }
    String remaining = result[1];

    // while the remaining cost is higher than expected,
    // transform it
    while (WordEvaluator.valueOfPath (remaining) > settings.getRemainingCost ()) {
      // Find a substraction
      result = WordsFinder.findNearestPair (wfs, remaining, word,
          settings.getLettersMissing (), settings.getLengthDelta ());
      // Replace the remaining by it
      equation = equation.replace (remaining, result[0]);
      // Pick up the new remaining cost
      remaining = result[1];
    }
    return equation;
  }

  /**
   * Splits a sentence in words, removing ponctuation marks
   * 
   * @param sentence
   *          the sentence typed by the user
   * @return array of words in String[]
   */
  public static String[] preparse (String sentence) {
    sentence = sentence.toLowerCase ().replaceAll ("[!?.,;:]", "");
    return sentence.split (" ");
  }
}
