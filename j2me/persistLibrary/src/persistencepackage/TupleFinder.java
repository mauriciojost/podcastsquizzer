/**
 *
 * @author Mauri
 */

package persistencepackage;

import java.util.Enumeration;
import java.util.Vector;

public class TupleFinder {
    private Vector vector;
    private int lastIndex=0;
    private TupleAsMarkComparator tupleComparator; 
    public TupleFinder(Vector vector){
        this.vector = vector;
        tupleComparator = new TupleAsMarkComparator(Tuple.INDEX_EXTRA);
    }
    
    
    public Tuple lookFor(int sec) throws Exception{
        Enumeration e;
        Tuple tuple;
        int index=0;
                
        e = vector.elements();
        
        while (e.hasMoreElements()){
            tuple = (Tuple)e.nextElement();
            if (areTimesEqual(tuple, sec,2)==true){
                lastIndex = index;
                return tuple;
            }
            index++;
        }
        throw new Exception("Tuple not found.");
    }
    
    
    public Tuple lookForMoreAppropriate(int sec_actual) throws Exception{
        Tuple currentT=null;
        int diff;
        
        vector.trimToSize();
    
        for(int i=vector.size()-1; i>=0;i--){
            currentT = (Tuple)vector.elementAt(i);
            try {
                diff = tupleComparator.compareTupleElementAsTimeMarks(sec_actual, currentT);
                if (diff<0){
                    lastIndex = (i+1)%vector.size();
                    return (Tuple)vector.elementAt(lastIndex);
                }
            } catch (Exception ex) { ex.printStackTrace();}
        }
        throw new Exception("Tuple not found.");
        
    }
    
    public int getLastIndex(){
        return lastIndex;
    }
    
    private boolean areTimesEqual(Tuple tuple, int sec, int index) {
        if (tuple!=null){
            try {
                return Parser.hours2sec(tuple.getElement(index)) == sec;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
