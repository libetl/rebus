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
    
}
