package org.toilelibre.libe.rebus.objects.structs;

public class Phoneme {

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
