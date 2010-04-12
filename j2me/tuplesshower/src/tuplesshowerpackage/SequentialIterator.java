package tuplesshowerpackage;

import canvaspackage.Word;
import java.util.Vector;
import miscellaneouspackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class SequentialIterator implements Iterator {
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    
    private Vector vector;
    private int currentIndex = 0;
    
    public SequentialIterator(Vector vector_orig, int t) {
        this.vector = vector_orig;
    }
    
    public Tuple getNext() {
        if (vector==null){
            return EMPTY_TUPLE;
        }
        if (vector.size()<=0) {
            return EMPTY_TUPLE;
        }else{
            Tuple tuple;
            //currentIndex = (currentIndex + 1) % vector.size();
            currentIndex=((currentIndex>=vector.size()-1)?currentIndex:currentIndex+1);
            
            tuple = (Tuple)vector.elementAt(currentIndex);
            return tuple;
        }
    }

    public Tuple getPrevious() {
        if (vector==null){
            return EMPTY_TUPLE;
        }
        if (vector.size()<=0) {
            return EMPTY_TUPLE;
        }else{
            Tuple tuple;
            /*if (currentIndex<=0) {
                currentIndex = vector.size()-1;
            } else {
                currentIndex = currentIndex - 1;
            }*/
            currentIndex=((currentIndex<=0)?0:currentIndex-1);
            
            tuple = (Tuple)vector.elementAt(currentIndex);
            return tuple;
        }
    }

    public void reinitialize() {
        currentIndex = 0;
    }

    public Tuple getCurrent() {
        if (vector==null){
            return EMPTY_TUPLE;
        }
        if (vector.size()<=0) {
            return EMPTY_TUPLE;
        }else{
            currentIndex = currentIndex<0?0:currentIndex;
            return (Tuple)vector.elementAt(currentIndex);
        }
    }

    public Vector getVector() {
        return this.vector;
    }

    public void addNewTuple(Tuple tuple) {
        if (vector==null){
            return;
        }
        vector.insertElementAt(tuple, currentIndex);
        vector.trimToSize();
        currentIndex = vector.indexOf(tuple);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
