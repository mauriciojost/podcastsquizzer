package tuplesshowerpackage;

import canvaspackage.Word;
import java.util.Vector;
import miscellaneouspackage.Tuple;

public class SequentialIterator implements Iterator {
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    
    public static final int MODE_SEQUENTIAL=0;
    public static final int MODE_RANDOM=1;
    public static final int AMOUNT_OF_MODES=2;
    public static final String[] MODE_NAMES= {"Sequential","Random"};
    
    private Vector vector;
    private int currentIndex = 0;
    private Shuffler shuffler;
    private int mode=MODE_SEQUENTIAL;
    
    public SequentialIterator(Vector vector_orig) {
        this.vector = vector_orig;
        this.shuffler = new Shuffler();
    }
    
    public Tuple getNext() {
        if (vector==null){
            return EMPTY_TUPLE;
        }
        if (vector.size()<=0) {
            return EMPTY_TUPLE;
        }else{
            currentIndex=((currentIndex>=vector.size()-1)?currentIndex:currentIndex+1);
            
            if (mode==MODE_SEQUENTIAL){
                return (Tuple)vector.elementAt(currentIndex);
            }else{
                return (Tuple)shuffler.getRandomElement(vector, currentIndex);
            }
        }
    }

    public Tuple getPrevious() {
        if (vector==null){
            return EMPTY_TUPLE;
        }
        if (vector.size()<=0) {
            return EMPTY_TUPLE;
        }else{
            currentIndex=((currentIndex<=0)?0:currentIndex-1);
            if (mode==MODE_SEQUENTIAL){
                return (Tuple)vector.elementAt(currentIndex);
            }else{
                return (Tuple)shuffler.getRandomElement(vector, currentIndex);
            }
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
            if (mode==MODE_SEQUENTIAL){
                return (Tuple)vector.elementAt(currentIndex);
            }else{
                return (Tuple)shuffler.getRandomElement(vector, currentIndex);
            }
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

    public String setMode(int mod) {
        if (mod==-1){
            mode = (mode+1)%AMOUNT_OF_MODES;
        }else{
            this.mode = mod;
        }
        return MODE_NAMES[mode];
    }
}
