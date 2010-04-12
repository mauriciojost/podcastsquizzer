package tuplesshowerpackage;

import java.util.Vector;
import persistencepackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class SequentialIterator implements Iterator {
    private Vector vector;
    private int currentIndex = -1;
    
    public SequentialIterator(Vector vector_orig) {
        this.vector = vector_orig;
        getVector();
    }
    
    public Tuple getNext() {
        getVector();
        if (vector.size()<=0) {
            return new Tuple("Empty.","Empty.","Empty.");
        }else{
            Tuple tuple;
            currentIndex = (currentIndex + 1) % vector.size();
            tuple = (Tuple)vector.elementAt(currentIndex);
            return tuple;
        }
    }

    public Tuple getPrevious() {
        getVector();
        if (vector.size()<=0) {
            return new Tuple("Empty.","Empty.","Empty.");
        }else{
            Tuple tuple;
            if (currentIndex<=0) {
                currentIndex = vector.size()-1;
            } else {
                currentIndex = currentIndex - 1;
            }
            
            tuple = (Tuple)vector.elementAt(currentIndex);
            return tuple;
        }
    }

    public void reinitialize() {
        currentIndex = -1;
    }

    public Tuple getCurrent() {
        getVector();
        if (vector.size()<=0) {
            return new Tuple("Empty.","Empty.","Empty.");
        }else{
            currentIndex = currentIndex<0?0:currentIndex;
            return (Tuple)vector.elementAt(currentIndex);
        }
    }

    public Vector getVector() {
        if (this.vector==null){
            this.vector = new Vector();
        }
        
        return this.vector;
    }

    public void addNewTuple(Tuple tuple) {
        getVector();
        vector.insertElementAt(tuple, currentIndex);
        vector.trimToSize();
        currentIndex = vector.indexOf(tuple);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
