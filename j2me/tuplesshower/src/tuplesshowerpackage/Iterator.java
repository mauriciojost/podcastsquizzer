/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tuplesshowerpackage;

import java.util.Vector;
import persistencepackage.Tuple;

/**
 *
 * @author Mauricio
 */
public interface Iterator {
    public Tuple getNext();
    public Tuple getCurrent();
    public Tuple getPrevious();
    public void reinitialize();
    public Vector getVector();
    public void addNewTuple(Tuple tuple);
    public int getCurrentIndex();
}
