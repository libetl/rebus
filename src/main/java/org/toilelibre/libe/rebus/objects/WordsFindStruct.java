package org.toilelibre.libe.rebus.objects;

import java.util.Set;

import org.toilelibre.libe.rebus.objects.structs.FSM;
import org.toilelibre.libe.rebus.objects.structs.Pair;
import org.toilelibre.libe.rebus.objects.structs.Word;

/**
 * This struct is the owner of all FSMs where we can find deltas between words.
 * 
 * The main concept is that simple : replace a sample of letters by a
 * subtraction between two known words.
 * 
 * exemple : replace "the" by elephant - plane
 * 
 * A FSM is nothing but a forest with paths crossing each other.
 * 
 * It is made of data and transitions to other FSMs.
 * 
 * @author LiBe
 * @see org.toilelibre.libe.rebus.objects.structs.FSM
 */
public class WordsFindStruct extends FSM<Set<Pair<Word, Word>>, String> {

  public WordsFindStruct () {
  }

}
