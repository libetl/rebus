package org.toilelibre.libe.rebus.objects.structs;

public class Phoneme {
    
    public static final Phoneme EMPTY = new Phoneme ("//");

    private final String phoneme;

    public Phoneme (String phoneme) {
        this.phoneme = phoneme;
    }

    public String getPhoneme () {
        return phoneme;
    }

    @Override
    public String toString () {
        return phoneme;
    }
    
    public boolean endsWith (Phoneme otherPhoneme) {
        return this.phoneme.replace ('/', ' ').trim ().endsWith (
                otherPhoneme.getPhoneme ().replace ('/', ' ').trim ()) &&
                otherPhoneme.getPhoneme ().indexOf (' ') == -1;
    }

    public boolean startsWith (Phoneme otherPhoneme) {
        return this.phoneme.replace ('/', ' ').trim ().startsWith (
                otherPhoneme.getPhoneme ().replace ('/', ' ').trim ()) &&
                otherPhoneme.getPhoneme ().indexOf (' ') == -1;
    }
    
}
