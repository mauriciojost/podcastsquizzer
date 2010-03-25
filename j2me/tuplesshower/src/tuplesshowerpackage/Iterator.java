/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tuplesshowerpackage;

import persistencepackage.Tuple;

/**
 *
 * @author Mauricio
 */
public interface Iterator {
    public Tuple getNext();
    public Tuple getPrevious();
    public void reinitialize();
}
