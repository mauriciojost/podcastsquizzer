package tuplesshowerpackage;

import canvaspackage.Word;
import java.util.Random;
import java.util.Vector;
import miscellaneouspackage.ArrayAsNumbersComparator;
import miscellaneouspackage.Sorter;
import miscellaneouspackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class Shuffler{
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    private Vector indirectionVector;
    private Sorter sort;
    private int currentIndex=0;
    private Random rnd;
    private int size=0;
            
    public Shuffler(){    
        rnd = new Random();
        this.sort = new Sorter(new ArrayAsNumbersComparator(1));
    }
    
    private void createIndirectionVector(){
        this.indirectionVector = new Vector();

        for (int i=0;i<size;i++){
            Integer[] t = new Integer[2];
            t[0] = new Integer(i);
            t[1] = new Integer(rnd.nextInt()); 
            this.indirectionVector.addElement(t);
        } 
    }
    
    private void createNewSuffleList(Vector vector){
        if (size!=vector.size()){
            /* RESET */
            size = vector.size();
            createIndirectionVector();
            this.currentIndex=0;
            try {
                sort.sort(indirectionVector);
            } catch (Exception ex) {
                ex.printStackTrace();
            }        
        }   
    }
    
    public Tuple getRandomElement(Vector vec, int currIndex){
        if (vec==null)
            return EMPTY_TUPLE;
        if (vec.size()==0)
            return EMPTY_TUPLE;
        createNewSuffleList(vec);
        return (Tuple) vec.elementAt(getRandomAt(currIndex));
    }
    
    public Tuple getNextRandomElement(Vector vec){
        if (vec==null)
            return EMPTY_TUPLE;
        if (vec.size()==0)
            return EMPTY_TUPLE;
        createNewSuffleList(vec);
        currentIndex = (currentIndex+1)%vec.size();
        return (Tuple) vec.elementAt(getRandomAt(currentIndex));
    }
    
    public Tuple getPreviousRandomElement(Vector vec) {
        if (vec==null)
            return EMPTY_TUPLE;
        if (vec.size()==0)
            return EMPTY_TUPLE;
        createNewSuffleList(vec);
        currentIndex--;
        if (currentIndex<0)
            currentIndex = vec.size()-1;
        
        return (Tuple)vec.elementAt(getRandomAt(currentIndex));
    }
    
    private int getRandomAt(int index){
        Integer[] integArr = (Integer[])this.indirectionVector.elementAt(index);
        return integArr[0].intValue();
    }
    
    public Tuple getCurrent(Vector vec) {
        if (vec==null)
            return EMPTY_TUPLE;
        if (vec.size()==0)
            return EMPTY_TUPLE;
        createNewSuffleList(vec);
        currentIndex--;
        if (currentIndex<0)
            currentIndex = vec.size()-1;
        
        return (Tuple)vec.elementAt(getRandomAt(currentIndex));
    }

    
}
