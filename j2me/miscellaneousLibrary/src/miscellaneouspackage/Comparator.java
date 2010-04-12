/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package miscellaneouspackage;

/**
 *
 * @author Mauricio
 */
public interface Comparator{
    public int compare(Object x, Object y) throws Exception;
    public boolean isComparable(Object obj);
}
