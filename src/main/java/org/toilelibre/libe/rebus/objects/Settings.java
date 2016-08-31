package org.toilelibre.libe.rebus.objects;

/**
 * 
 * @author LiBe
 * 
 */
public class Settings {

  /**
   * Variation of the length of the words
   */
  private int lengthDelta    = 5;
  /**
   * Accuracy of the matchs
   */
  private int lettersMissing = 2;
  /**
   * Max cost that can be left for a word (weight of the remainders)
   */
  private int remainingCost  = 2;

  public int getLengthDelta () {
    return this.lengthDelta;
  }

  public int getLettersMissing () {
    return this.lettersMissing;
  }

  public int getRemainingCost () {
    return this.remainingCost;
  }

  public void setLengthDelta (final int lengthDelta) {
    this.lengthDelta = lengthDelta;
  }

  public void setLettersMissing (final int lettersMissing) {
    this.lettersMissing = lettersMissing;
  }

  public void setRemainingCost (final int remainingCost) {
    this.remainingCost = remainingCost;
  }

}
