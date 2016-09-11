package org.toilelibre.libe.rebus;

import org.toilelibre.libe.rebus.actions.BuildEquationFromSentence;
import org.toilelibre.libe.rebus.actions.LoadData;

public class EquationGeneratorProgram {

    public static String argsJoin (final String [] aArr) {
        final StringBuilder sbStr = new StringBuilder ();
        for (int i = 0, il = aArr.length ; i < il ; i++) {
            if (i > 0) {
                sbStr.append (" ");
            }
            sbStr.append (aArr [i]);
        }
        return sbStr.toString ();
    }

    public static void main (final String [] args) {
        LoadData.ensureEnglishDataLoaded ();
        System.out.println (
                BuildEquationFromSentence.getEquation (
                        EquationGeneratorProgram.argsJoin (args)));
    }
}
