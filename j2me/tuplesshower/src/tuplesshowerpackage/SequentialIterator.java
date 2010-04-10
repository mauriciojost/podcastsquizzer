package tuplesshowerpackage;

import java.util.Vector;
import persistencepackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class SequentialIterator implements Iterator {
    private Vector vector;
    private int current = -1;
    
    public SequentialIterator(Vector vector_orig) {
        this.vector = vector_orig;
    }
    
    public Tuple getNext() {
        if (vector.size()<=0) {
            return new Tuple("Empty.","Empty.","Empty.");
        }else{
            Tuple tuple;
            current = (current + 1) % vector.size();
            tuple = (Tuple)vector.elementAt(current);
            return tuple;
        }
    }

    public Tuple getPrevious() {
        if (vector.size()<=0) {
            return new Tuple("Empty.","Empty.","Empty.");
        }else{
            Tuple tuple;
            if (current<=0) {
                current = vector.size()-1;
            } else {
                current = current - 1;
            }
            
            tuple = (Tuple)vector.elementAt(current);
            return tuple;
        }
    }

    public void reinitialize() {
        current = -1;
    }

    public Tuple getCurrent() {
        if (vector.size()<=0) {
            return new Tuple("Empty.","Empty.","Empty.");
        }else{
            current = current<0?0:current;
            return (Tuple)vector.elementAt(current);
        }
    }

    public Vector getVector() {
        return this.vector;
    }

    public void addNewTuple(Tuple tuple) {
        if (vector==null){
            vector = new Vector();
        }
        vector.insertElementAt(tuple, current);
        vector.trimToSize();
        current = vector.indexOf(tuple);
    }
}
