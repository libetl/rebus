package org.toilelibre.libe.rebus.process;

import org.junit.Test;
import org.toilelibre.libe.rebus.objects.Data;
import org.toilelibre.libe.rebus.objects.structs.Word;

public class RebusFromWordTest {

    @Test
    public void justATest () {
        Data data = new Data ();
        data.init ();
        System.out.println (RebusFromWord.getRebus (data, new Word ("miracle")));
    }

}
