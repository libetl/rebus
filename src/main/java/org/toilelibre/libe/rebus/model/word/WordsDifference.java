package org.toilelibre.libe.rebus.model.word;

class WordsDifference {
    /**
     * The tip is to use the sorted letters of the word. It enables it to be
     * more efficiently compared. The complexity of this method is now linear.
     *
     * @param otherWord
     *            the word to be compared with
     * @return the difference
     */
    static String difference (final Word word, final Word otherWord) {

        // With two cursors, we will compare letter by letter
        // these two words
        int i = 0;
        int j = 0;
        final StringBuffer addBuff = new StringBuffer ();
        final StringBuffer remBuff = new StringBuffer ();

        final String wordSortedLetters = word.getSortedLetters ();
        final String otherWordSortedLetters = otherWord.getSortedLetters ();

        // Reading both words
        while (i < wordSortedLetters.length () && j < otherWordSortedLetters.length ()) {

            if (wordSortedLetters.charAt (i) < otherWordSortedLetters.charAt (j)) {
                // The letter does not exist in the other word
                addBuff.append (wordSortedLetters.charAt (i));
                i++;
            } else if (wordSortedLetters.charAt (i) > otherWordSortedLetters.charAt (j)) {
                // The letter does not exist in this word
                remBuff.append (otherWordSortedLetters.charAt (j));
                j++;
            } else {
                // The letter exists on both words, nothing to append
                i++;
                j++;
            }
        }
        // Appending the remaining parts
        addBuff.append (wordSortedLetters.substring (i));
        remBuff.append (otherWordSortedLetters.substring (j));

        // Writing the difference in a mathematical expression
        return (remBuff.length () > 0 ? "- " + remBuff.toString () : "") + (addBuff.length () > 0 && remBuff.length () > 0 ? " + " + addBuff.toString () : "") + (addBuff.length () > 0 && remBuff.length () == 0 ? "+ " + addBuff.toString () : "");
    }
}
