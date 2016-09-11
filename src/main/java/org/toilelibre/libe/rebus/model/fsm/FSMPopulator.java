package org.toilelibre.libe.rebus.model.fsm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.toilelibre.libe.rebus.model.word.Word;

/**
 * Contains a method to add words and a method to find words in our FSM
 *
 * @author LiBe
 * @see org.toilelibre.libe.rebus.model.fsm.FSM
 */
public class FSMPopulator {

    private static final Pattern PATTERN_DIFF = Pattern.compile ("(\\- ([a-z0-9]+))?( )?(\\+ ([a-z0-9]+))?");

    /**
     * Add a new pair in the struct Needs a path and two words. This equation
     * must be true : w1 - w2 = path
     *
     * @param fsm
     *            the struct
     * @param pathNegative
     *            - [X] + Y the value in brackets
     * @param pathPositive
     *            - X + [Y] the value in brackets
     * @param w1
     *            first word of the pair
     * @param w2
     *            second word of the pair
     */
    private static void addOnePath (final FSM<Set<Pair<Word, Word>>, String> fsm, final String pathNegative, final String pathPositive, final Word w1, final Word w2) {

        FSM<Set<Pair<Word, Word>>, String> correctPath = fsm;

        // We build the pair
        final Pair<Word, Word> pair = new Pair<Word, Word> ();
        pair.setObj1 (w1);
        pair.setObj2 (w2);

        // We follow the correct path
        correctPath = FSMPopulator.goRecursive (fsm, "-", pathNegative);
        correctPath = FSMPopulator.goRecursive (fsm, "+", pathPositive);

        // Then we add the pair
        if (correctPath.getData () == null) {
            correctPath.setData (new HashSet<Pair<Word, Word>> ());
        }
        correctPath.getData ().add (pair);
    }

    /**
     * Adds a pair in the struct After this operation, we will be able to
     * replace the value of WordsDifference.difference (w1, w2) by :w1 - :w2
     *
     * This method is called when the stuct is under construction.
     *
     * @param wfs
     *            the struct
     * @param w1
     *            first word of the pair
     * @param w2
     *            second word of the pair
     */
    public static void addPath (final FSM<Set<Pair<Word, Word>>, String> wfs, final Word w1, final String difference1to2, final Word w2, final String difference2to1) {

        // We need to split the paths in two
        // {negative, positive} Strings before entering
        // addOnePath
        final Matcher matcher1 = FSMPopulator.PATTERN_DIFF.matcher (difference1to2);
        final Matcher matcher2 = FSMPopulator.PATTERN_DIFF.matcher (difference2to1);
        matcher1.find ();
        matcher2.find ();

        // There are two paths to add actually
        // :w1 - :w2 and :w2 - :w1

        String negative = matcher1.group (2);
        String positive = matcher1.group (5);
        FSMPopulator.addOnePath (wfs, negative, positive, w1, w2);
        negative = matcher2.group (2);
        positive = matcher2.group (5);
        FSMPopulator.addOnePath (wfs, negative, positive, w2, w1);
    }

    /**
     * Public method to find a good pair (subtraction) of words to replace a
     * path
     *
     * @param fsm
     *            the struct
     * @param path
     *            the path to explore in our struct
     * @param wordToAvoid
     *            we can specify a word we don't want to see to avoid things
     *            like :rhino + (:hornet - :rhino)
     * @param numLettersMissing
     *            if the path is incomplete, how many letters can we add in it ?
     * @param lengthDelta
     *            how much longer or shorter can be the path ?
     * @return The first element is a substraction between two keywords, The
     *         second element represent the remaining letters, which can be
     *         useful for further finds
     */
    public static String [] findNearestPair (final FSM<Set<Pair<Word, Word>>, String> fsm, final String path, final Word wordToAvoid, final int numLettersMissing, final int lengthDelta) {

        // If no path, do not waste time by adding a :earth - :heart
        // or a :lemon - :melon or a :plaster - :stapler
        if ("".equals (path)) {
            return new String [] { "", "" };
        }

        // Now, we retrieve each interesting path according to the two
        // integers specified (numLettersMissing and lengthDelta)
        final Map<String, Set<Pair<Word, Word>>> map = FSMPopulator.searchPairsByPath (fsm, path, wordToAvoid, numLettersMissing, lengthDelta);

        // We choose the best matching path to our path parameter
        final String bestPath = PathEvaluator.chooseBestPathDelta (new LinkedList<String> (map.keySet ()), path);

        // Here we get a set of Pairs. These are our matches for the
        // substraction
        final Set<Pair<Word, Word>> bestSet = map.get (bestPath);

        // If it is empty (which should not happen except if no match
        // has been found so far), we have to return the path.
        if (bestPath.length () == 0) {
            return new String [] { path, "" };
        }

        // We should pick one pair randomly in the set
        @SuppressWarnings ("unchecked")
        final Pair<Word, Word> pair = (Pair<Word, Word>) bestSet.toArray () [new Random ().nextInt (bestSet.size ())];

        // We compute the remaining part
        final String remaining = PathEvaluator.remainingPath (path, bestPath);

        // so now we can state that
        // path = (pair#1 - pair#2) + remaining
        return new String [] { "+ (" + pair.getObj1 () + " - " + pair.getObj2 () + " " + remaining + ")", remaining };
    }

    /**
     * Main recursive method to find every interesting path
     *
     * To sum up, this method looks deeply into the FSM-Struct to look for every
     * set of pairs that could best fit our submitted letters (in the parameters
     * negative and positive). It avoids adding the pairs containing the
     * wordToAvois
     *
     * It can follow the paths with a [numLettersMissing] tolerance if no
     * transition is found.
     *
     * It takes every set of pairs when the currentPath is around the pathLength
     * (with a tolerance of lengthDelta)
     *
     * currentPathNegative and currentPathPositive are values used by recursion.
     * If called from outside, these values are ""
     *
     * @param fsm
     *            the FSM we are in. Can be a child of the WordsFindStruct, or
     *            the WordsFindStruct itself
     * @param wordToAvoid
     *            the word to avoid
     * @param negative
     *            remaining negative path to explore
     * @param positive
     *            remaining positive path to explore
     * @param numLettersMissing
     *            tolerance to incomplete paths
     * @param lengthDelta
     *            tolerance to paths lengths
     * @param pathLength
     *            current path length. Also is the degree of recursion
     * @param currentPathNegative
     *            recursion variable. Contains the current negative path
     * @param currentPathPositive
     *            recursion variable. Contains the current positive path
     * @return all the sets of pairs arranged by paths
     */
    private static Map<String, Set<Pair<Word, Word>>> findNearestPathsData (final FSM<Set<Pair<Word, Word>>, String> fsm, final Word wordToAvoid, final String negative, final String positive, final int numLettersMissing, final int lengthDelta, final int pathLength, final String currentPathNegative,
            final String currentPathPositive) {

        // We create some variables to prepare the body of the loop.
        final Map<String, Set<Pair<Word, Word>>> result = new HashMap<String, Set<Pair<Word, Word>>> ();
        final int currentLength = currentPathNegative.length () + currentPathPositive.length ();
        final String currentPath = ToStringExpr.buildPathText (currentPathNegative, currentPathPositive);

        // Can we use the pairs herein ?
        // We check if there are pairs and if the length of
        // the path fits
        if (fsm.getData () != null && pathLength >= currentLength - lengthDelta && pathLength <= currentLength + lengthDelta) {
            // Good ! Let's add every pair in our map. We copy the set.
            final Set<Pair<Word, Word>> value = new HashSet<Pair<Word, Word>> ();
            value.addAll (fsm.getData ());
            result.put (currentPath, value);
            // We need to remove any pair containing the wordToAvoid
            FSMPopulator.removePairsWithWordToAvoid (wordToAvoid, result.get (currentPath));
            // Maybe the set is empty after this removal, so let's remove the
            // association
            if (result.get (currentPath).isEmpty ()) {
                result.remove (currentPath);
            }
        }

        // Now time for recursion if there are transitions to explore
        if (fsm.getTransitions () != null) {
            for (final String s : fsm.getTransitions ().keySet ()) {
                // For each transition in this state of the FSM

                if (s.startsWith ("+") && positive.indexOf (s.charAt (1)) != -1) {
                    // We found a matching positive transition in the path
                    final String newPositive = positive.replaceFirst ("" + s.charAt (1), "");
                    result.putAll (FSMPopulator.findNearestPathsData (fsm.getTransitions ().get (s), wordToAvoid, negative, newPositive, numLettersMissing, lengthDelta, pathLength, currentPathNegative, currentPathPositive + s.charAt (1)));
                } else if (s.startsWith ("-") && negative.indexOf (s.charAt (1)) != -1) {
                    // We found a matching negative transition in the path
                    final String newNegative = negative.replaceFirst ("" + s.charAt (1), "");
                    result.putAll (FSMPopulator.findNearestPathsData (fsm.getTransitions ().get (s), wordToAvoid, newNegative, positive, numLettersMissing, lengthDelta, pathLength, currentPathNegative + s.charAt (1), currentPathPositive));
                } else if (s.startsWith ("-") && numLettersMissing > 0) {
                    // We can use the tolerance as a joker for a negative
                    // transition
                    result.putAll (FSMPopulator.findNearestPathsData (fsm.getTransitions ().get (s), wordToAvoid, negative, positive, numLettersMissing - 1, lengthDelta, pathLength, currentPathNegative + s.charAt (1), currentPathPositive));
                } else if (s.startsWith ("+") && numLettersMissing > 0) {
                    // We can use the tolerance as a joker for a positive
                    // transition
                    result.putAll (FSMPopulator.findNearestPathsData (fsm.getTransitions ().get (s), wordToAvoid, negative, positive, numLettersMissing - 1, lengthDelta, pathLength, currentPathNegative, currentPathPositive + s.charAt (1)));
                }
            }
        }

        return result;
    }

    /**
     * Browses a FSM with transitions of the same symbol
     *
     * @param fsm
     *            FSM to explore
     * @param symbol
     *            "+" or "-"
     * @param path
     *            path to explore
     * @return the children FSM, even empty
     */
    private static FSM<Set<Pair<Word, Word>>, String> goRecursive (FSM<Set<Pair<Word, Word>>, String> fsm, final String symbol, final String path) {
        if (path == null) {
            return fsm;
        }
        for (int i = 0 ; i < path.length () ; i++) {
            if (fsm.getTransitions () == null) {
                fsm.setTransitions (new HashMap<String, FSM<Set<Pair<Word, Word>>, String>> ());
            }
            if (fsm.getTransitions ().get (symbol + path.charAt (i)) == null) {
                fsm.getTransitions ().put (symbol + path.charAt (i), new FSM<Set<Pair<Word, Word>>, String> ());
            }
            fsm = fsm.getTransitions ().get (symbol + path.charAt (i));
        }
        return fsm;
    }

    /**
     * Removes every pair in the set containing the wordToAvoid
     *
     * @param wordToAvoid
     *            word to avoid
     * @param set
     *            the set containing the pairs
     */
    private static void removePairsWithWordToAvoid (final Word wordToAvoid, final Set<Pair<Word, Word>> set) {
        final List<Pair<Word, Word>> toRemove = new LinkedList<Pair<Word, Word>> ();
        for (final Pair<Word, Word> pair : set) {
            if (pair.getObj1 ().equals (wordToAvoid) || pair.getObj2 ().equals (wordToAvoid)) {
                toRemove.add (pair);
            }
        }
        for (final Pair<Word, Word> pair : toRemove) {
            set.remove (pair);
        }
    }

    /**
     * Method calling the main findNearestPathsData method
     *
     * @param wfs
     *            our struct
     * @param path
     *            the path in mathematical expression
     * @param wordToAvoid
     *            a word to avoid
     * @param numTransitionsMissing
     *            tolerance for paths
     * @param lengthDelta
     *            tolerance for path length
     * @return all the sets of pairs arranged by paths
     */
    private static Map<String, Set<Pair<Word, Word>>> searchPairsByPath (final FSM<Set<Pair<Word, Word>>, String> wfs, final String path, final Word wordToAvoid, final int numTransitionsMissing, final int lengthDelta) {

        // We create the negative and positive path variables here
        final Matcher matcher1 = FSMPopulator.PATTERN_DIFF.matcher (path);
        matcher1.find ();
        final String negative = matcher1.group (2) == null ? "" : matcher1.group (2);
        final String positive = matcher1.group (5) == null ? "" : matcher1.group (5);

        // We proceed to the call of the recursion method here
        final FSM<Set<Pair<Word, Word>>, String> fsm = wfs;
        return FSMPopulator.findNearestPathsData (fsm, wordToAvoid, negative, positive, numTransitionsMissing, lengthDelta, negative.length () + positive.length (), "", "");
    }
}
