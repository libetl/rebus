package org.toilelibre.libe.rebus.model.phonemes;

public class Phoneme {

    public static final Phoneme EMPTY = new Phoneme ("//");

    private final String        phoneme;

    public Phoneme (final String phoneme) {
        this.phoneme = phoneme;
    }

    public String getPhoneme () {
        return this.phoneme;
    }

    @Override
    public String toString () {
        return this.phoneme;
    }

    public boolean endsWith (final Phoneme otherPhoneme) {
        return this.phoneme.replace ('/', ' ').trim ().endsWith (otherPhoneme.getPhoneme ().replace ('/', ' ').trim ()) && otherPhoneme.getPhoneme ().indexOf (' ') == -1;
    }

    public boolean startsWith (final Phoneme otherPhoneme) {
        return this.phoneme.replace ('/', ' ').trim ().startsWith (otherPhoneme.getPhoneme ().replace ('/', ' ').trim ()) && otherPhoneme.getPhoneme ().indexOf (' ') == -1;
    }

}
