package org.toilelibre.libe.rebus;

import org.toilelibre.libe.rebus.actions.BuildRebusFromSentence;
import org.toilelibre.libe.rebus.actions.LoadData;

public class RebusGeneratorProgram {

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
        LoadData.onlyEnglishRebusData ();
        System.out.println (RebusGeneratorProgram.argsJoin (BuildRebusFromSentence.getRebusFromSentence (RebusGeneratorProgram.argsJoin (args))));
    }
}
