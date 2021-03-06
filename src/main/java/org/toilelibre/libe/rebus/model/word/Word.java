package org.toilelibre.libe.rebus.model.word;

import java.util.Arrays;

/**
 * Representation of the "Word" concept. A Word could be a String, but we need
 * several features like knowing a difference between two Words (with a
 * mathematical expression), or to know whether this word is a keyword or not
 * (is it in the dictionary ?)
 *
 * @author LiBe
 *
 */
public final class Word {

    private final String sortedLetters;
    private final String word;

    /**
     * We create the word but we also sort the letters to help the process of
     * comparison
     *
     * @param word1
     *            the String content
     */
    public Word (final String word1) {
        this.word = word1;
        // We sort the word here
        final char [] buffer = word1.toCharArray ();
        Arrays.sort (buffer);
        this.sortedLetters = String.valueOf (buffer);
    }

    /**
     * Useful method like in String.charAt ()
     *
     * @param index
     *            the index of the String
     * @return character
     * @see java.lang.String#charAt(int)
     */
    public char charAt (final int index) {
        return this.word.charAt (index);
    }

    @Override
    /**
     * Provides a simple comparison between the String contents
     */
    public boolean equals (final Object anObject) {
        if (!(anObject instanceof Word)) {
            return false;
        }
        return this.word.equals (((Word) anObject).word);
    }

    /**
     * Gets the word sorted alphabetically
     *
     * @return the word
     */
    public String getSortedLetters () {
        return this.sortedLetters;
    }

    public String getWord () {
        return this.word;
    }

    /**
     * Length of the word
     *
     * @return length of the String content
     */
    public int length () {
        return this.word.length ();
    }

    @Override
    /**
     * Prepends with a colon if it is a keyword
     */
    public String toString () {
        return this.word;
    }

}
