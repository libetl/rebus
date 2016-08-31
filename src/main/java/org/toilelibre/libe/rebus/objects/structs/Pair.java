package org.toilelibre.libe.rebus.objects.structs;

/**
 * Very simple representation of a pair
 * 
 * @author LiBe
 * 
 * @param <T>
 *          Type of the first component
 * @param <U>
 *          Type of the second component
 */
public class Pair<T, U> {

  /**
   * First component
   */
  private T obj1;
  /**
   * Second component
   */
  private U obj2;

  public T getObj1 () {
    return this.obj1;
  }

  public U getObj2 () {
    return this.obj2;
  }

  public void setObj1 (final T obj1) {
    this.obj1 = obj1;
  }

  public void setObj2 (final U obj2) {
    this.obj2 = obj2;
  }

  @Override
  public String toString () {
    return "<" + this.obj1 + ", " + this.obj2 + ">";
  }
}
