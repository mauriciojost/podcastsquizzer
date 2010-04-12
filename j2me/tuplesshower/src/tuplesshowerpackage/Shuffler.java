package tuplesshowerpackage;

import canvaspackage.Word;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import miscellaneouspackage.ArrayAsNumbersComparator;
import miscellaneouspackage.Sorter;
import miscellaneouspackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class Shuffler implements Iterator {
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    private Vector vector;
    private Vector indirectionVector;
    private Sorter sort;
    private int currentIndex=0;
    
    public Shuffler(){    
        int indice=0;
        Random rnd = new Random();
        this.vector = vector;
        
        this.indirectionVector = new Vector();
        this.sort = new Sorter(new ArrayAsNumbersComparator(1));
        
        Enumeration iterator = vector.elements();
        
        Tuple tup;
        while(iterator.hasMoreElements()){
            tup = (Tuple)iterator.nextElement();
            Integer[] t = new Integer[2];
            t[0] = new Integer(indice);
            t[1] = new Integer(rnd.nextInt()); 
            this.indirectionVector.addElement(t);
            indice++;
        }
        try {
            sort.sort(indirectionVector);
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    public Tuple getNextRandomElement(Vector vec, ){
        
    }
    
    private int getRandomAt(int index){
        Integer[] integArr = (Integer[])this.indirectionVector.elementAt(index);
        return integArr[0].intValue();
    }
    
    public Tuple getNext() {
        if (vector==null)
            return EMPTY_TUPLE;
        if (vector.size()==0)
            return EMPTY_TUPLE;
        currentIndex=(currentIndex+1)%this.vector.size();
        return (Tuple)vector.elementAt(getRandomAt(currentIndex));
    }

    public Tuple getPrevious() {
        if (vector==null)
            return EMPTY_TUPLE;
        if (vector.size()==0)
            return EMPTY_TUPLE;
        currentIndex--;
        if (currentIndex<0)
            currentIndex = vector.size()-1;
        
        return (Tuple)vector.elementAt(getRandomAt(currentIndex));
    }

    public void reinitialize() {
        currentIndex=0;
    }

    public Tuple getCurrent() {
        if (vector==null)
            return EMPTY_TUPLE;
        if (vector.size()==0)
            return EMPTY_TUPLE;
        return (Tuple)vector.elementAt(getRandomAt(currentIndex));
    }

    public Vector getVector() {
        return this.vector;
    }

    public void addNewTuple(Tuple tuple) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }
    
}
