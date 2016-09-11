package org.toilelibre.libe.rebus.model.fsm;

import java.util.Map;

/**
 * FSM means Finite State Machine
 * 
 * a FSM contains data on its root, and transitions to between 0 and n FSMs
 * 
 * @author LiBe
 * 
 * @param <T>
 *          the data
 * @param <U>
 *          the transitions
 */
public class FSM<T, U> {

  /**
   * Data. Could contain a List of Pairs... or something else.
   */
  private T                 data;

  /**
   * Like in each FSM, for a letter we can or can't find a transition
   */
  private Map<U, FSM<T, U>> transitions;

  public T getData () {
    return this.data;
  }

  public Map<U, FSM<T, U>> getTransitions () {
    return this.transitions;
  }

  public void setData (final T data) {
    this.data = data;
  }

  public void setTransitions (final Map<U, FSM<T, U>> transitions) {
    this.transitions = transitions;
  }

}
