package org.toilelibre.libe.rebus.model.word;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains methods to evaluate costs and expressions with these rules : a vowel
 * costs 1 a consonant costs 5 a keyword is free
 *
 * @author LiBe
 *
 */
public class WordEvaluator {

    /**
     * Pattern to read a mathematical expression
     */
    public static final String PATTERN_DIFF = "(\\- ([a-z0-9]+))?( )?(\\+ ([a-z0-9]+))?";

    /**
     * Evaluates a sequence of letters
     *
     * @param letters
     *            only [a-z0-9]+, without any space
     * @return a value
     */
    private static int evaluate (final String letters) {
        int val = 0;
        for (int i = 0 ; i < letters.length () ; i++) {
            if (letters.charAt (i) == 'a' || letters.charAt (i) == 'e' || letters.charAt (i) == 'i' || letters.charAt (i) == 'o' || letters.charAt (i) == 'u' || letters.charAt (i) == 'y') {
                val++;
            } else if (Character.isLowerCase (letters.charAt (i)) || Character.isDigit (letters.charAt (i))) {
                val += 5;
            }
        }
        return val;
    }

    /**
     * Computes the remaining cost of an equation
     *
     * @param equation
     *            the equation
     * @return the cost
     */
    public static int valueOfNotTranslated (final String equation) {
        // We transform the equation into a sequence of letters
        // which are not in keywords
        String remainders = equation.replaceAll ("(:[a-z0-9]+)", "");
        remainders = remainders.replaceAll ("\\+", "").replaceAll ("\\-", "").replaceAll (" ", "").replaceAll ("\\(", "").replaceAll ("\\)", "");
        // Then we compute the cost
        return WordEvaluator.evaluate (remainders);
    }

    /**
     * Computes the cost of a path
     *
     * @param path
     *            the path
     * @return the cost
     */
    public static int valueOfPath (final String path) {
        // We read the path and pick up only the letters
        final Pattern pattern = Pattern.compile (WordEvaluator.PATTERN_DIFF);
        final Matcher matcher = pattern.matcher (path);
        matcher.find ();
        final String negative = matcher.group (2) == null ? "" : matcher.group (2);
        final String positive = matcher.group (5) == null ? "" : matcher.group (5);
        final String letters = positive + negative;
        // now it is time to compute
        return WordEvaluator.evaluate (letters);
    }

}
